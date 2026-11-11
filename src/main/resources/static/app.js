
const painel = document.getElementById('painel-dinamico');
const menu = document.querySelector('.bottom-menu');

let conta = null;
let csrf = null;

function escapeHtml(valor) {
    return String(valor ?? '').replace(/[&<>"']/g, c => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    })[c]);
}

async function api(caminho, metodo = 'GET', dados) {
    let resposta;

    try {
        resposta = await fetch('/api/v1' + caminho, {
            method: metodo,
            credentials: 'same-origin',
            headers: {
                ...(dados === undefined
                    ? {}
                    : { 'Content-Type': 'application/json' }),

                ...(csrf && metodo !== 'GET'
                    ? { 'X-CSRF-Token': csrf }
                    : {})
            },
            body: dados === undefined
                ? undefined
                : JSON.stringify(dados)
        });

    } catch {
        throw new Error(
            'Não foi possível acessar o Spring Boot.'
        );
    }

    const texto = await resposta.text();

    let resultado = {};

    try {
        resultado = texto ? JSON.parse(texto) : {};
    } catch {
        // Resposta sem JSON.
    }

    if (!resposta.ok) {
        const erro =
            resultado.mensagem ||
            resultado.erro ||
            resultado.message ||
            `Erro HTTP ${resposta.status}`;

        if (resposta.status === 401 &&
            caminho !== '/auth/login') {
            conta = null;
            csrf = null;
            telaLogin();
        }

        throw new Error(erro);
    }

    return resultado;
}

function mostrarErro(erro) {
    const aviso = document.getElementById('aviso-retech');

    if (aviso) {
        aviso.textContent = erro?.message || String(erro);
    } else {
        alert(erro?.message || String(erro));
    }
}

function configurarMenu() {
    menu.innerHTML = `
    <button onclick="abrirTela('descarte')">
      Descartar aparelho
    </button>

    <button onclick="abrirTela('pontos')">
      Meus pontos
    </button>

    <button onclick="abrirTela('historico')">
      Histórico
    </button>

    <button onclick="abrirTela('senha')">
      Alterar senha
    </button>

    <button onclick="sair()">
      Sair
    </button>
  `;

    menu.style.display = conta ? 'flex' : 'none';
}

function aviso() {
    return `
    <p id="aviso-retech" style="color:#ffb0a7"></p>
  `;
}

function telaLogin(cadastro = false) {
    conta = null;
    csrf = null;

    configurarMenu();

    painel.innerHTML = `
    <h3>${cadastro ? 'Criar conta' : 'Entrar na ReTech'}</h3>

    <p>
      ${cadastro
        ? 'Cadastre-se para acumular pontos.'
        : 'Entre com seu CPF e sua senha.'}
    </p>

    <form id="form-auth">

      ${cadastro ? `
        <label>
          Nome completo
          <input
            name="nomeCompleto"
            maxlength="100"
            required
          >
        </label>
      ` : ''}

      <label>
        CPF
        <input
          name="cpf"
          inputmode="numeric"
          maxlength="11"
          placeholder="11 números"
          required
        >
      </label>

      <label>
        Senha
        <input
          name="senha"
          type="password"
          minlength="8"
          required
        >
      </label>

      <button type="submit">
        ${cadastro ? 'Cadastrar' : 'Entrar'}
      </button>

    </form>

    ${aviso()}

    <button type="button" id="alternar-auth">
      ${cadastro ? 'Já tenho conta' : 'Criar minha conta'}
    </button>
  `;

    document.getElementById('alternar-auth').onclick =
        () => telaLogin(!cadastro);

    document.getElementById('form-auth').onsubmit =
        async evento => {
            evento.preventDefault();

            const dados = Object.fromEntries(
                new FormData(evento.currentTarget).entries()
            );

            dados.cpf = dados.cpf.replace(/\D/g, '');

            if (dados.cpf.length !== 11) {
                return mostrarErro(
                    'O CPF deve ter 11 números.'
                );
            }

            try {
                conta = await api(
                    cadastro ? '/auth/cadastro' : '/auth/login',
                    'POST',
                    dados
                );

                csrf = conta.csrfToken;

                configurarMenu();

                await telaPontos();

            } catch (erro) {
                mostrarErro(erro);
            }
        };
}

async function atualizarConta() {
    conta = await api('/auth/me');
    csrf = conta.csrfToken;

    configurarMenu();
}

async function telaPontos() {
    try {
        await atualizarConta();

        painel.innerHTML = `
      <h3>
        Bem-vindo(a), ${escapeHtml(conta.nomeCompleto)}!
      </h3>

      <p>
        Seu saldo atual é de
        <strong>${Number(conta.pontosSaldo)} pontos</strong>.
      </p>

      <p>
        Cadastro ReTech nº ${Number(conta.idUsuario)}
      </p>

      <button onclick="abrirTela('descarte')">
        Registrar descarte
      </button>
    `;

    } catch (erro) {
        mostrarErro(erro);
    }
}

async function telaDescarte() {
    painel.innerHTML = `
    <h3>Novo descarte</h3>
    <p>Carregando informações do Oracle...</p>
  `;

    try {
        const [categorias, maquinas, instituicoes] =
            await Promise.all([
                api('/categorias'),
                api('/maquinas'),
                api('/instituicoes')
            ]);

        if (!categorias.length ||
            !maquinas.length ||
            !instituicoes.length) {

            painel.innerHTML = `
        <h3>Cadastros insuficientes</h3>
        <p>
          Precisamos de categorias, máquinas e
          instituições cadastradas no Oracle.
        </p>
      `;

            return;
        }

        const opcoes = (registros, campoId, legenda) =>
            registros.map(r => `
        <option value="${Number(r[campoId])}">
          ${escapeHtml(legenda(r))}
        </option>
      `).join('');

        painel.innerHTML = `
      <h3>Registrar descarte</h3>

      <form id="form-descarte">

        <label>
          Categoria
          <select name="idCategoria" required>
            ${opcoes(
            categorias,
            'idCategoria',
            r => `${r.nomeCategoria} (${r.pontosPorUnidade} pontos/un.)`
        )}
          </select>
        </label>

        <label>
          Máquina
          <select name="idMaquina" required>
            ${opcoes(
            maquinas,
            'idMaquina',
            r => r.bairroLocalizacao
        )}
          </select>
        </label>

        <label>
          Instituição
          <select name="idInstituicao" required>
            ${opcoes(
            instituicoes,
            'idInstituicao',
            r => r.nomeFantasia
        )}
          </select>
        </label>

        <label>
          Quantidade
          <input
            type="number"
            name="quantidade"
            min="1"
            max="999"
            value="1"
            required
          >
        </label>

        <button type="submit">
          Confirmar descarte
        </button>

      </form>

      ${aviso()}
    `;

        document.getElementById('form-descarte').onsubmit =
            async evento => {
                evento.preventDefault();

                const dados = Object.fromEntries(
                    new FormData(evento.currentTarget).entries()
                );

                try {
                    const descarte = await api(
                        '/descartes',
                        'POST',
                        {
                            idUsuario: conta.idUsuario,
                            idCategoria: Number(dados.idCategoria),
                            idMaquina: Number(dados.idMaquina),
                            idInstituicao: Number(dados.idInstituicao),
                            quantidade: Number(dados.quantidade)
                        }
                    );

                    await telaPontos();

                    painel.insertAdjacentHTML(
                        'beforeend',
                        `
              <p>
                Descarte nº ${Number(descarte.idDescarte)}
                registrado com sucesso!
                +${Number(descarte.pontosGerados)} pontos.
              </p>
            `
                    );

                } catch (erro) {
                    mostrarErro(erro);
                }
            };

    } catch (erro) {
        mostrarErro(erro);
    }
}

async function telaHistorico() {
    painel.innerHTML = `
    <h3>Histórico</h3>
    <p>Consultando o Oracle...</p>
  `;

    try {
        const dados = await api(
            '/descartes?idUsuario=' + conta.idUsuario
        );

        painel.innerHTML = `
      <h3>Meus descartes</h3>

      ${dados.length
            ? '<ul>' + dados.map(d => `
            <li>
              ${escapeHtml(
            String(d.dataRegistro || '').slice(0, 10)
        )}
              —
              ${Number(d.quantidade)} item(ns)
              —
              +${Number(d.pontosGerados)} pontos
            </li>
          `).join('') + '</ul>'

            : '<p>Você ainda não possui descartes.</p>'
        }
    `;

    } catch (erro) {
        mostrarErro(erro);
    }
}

function telaSenha() {
    painel.innerHTML = `
    <h3>Alterar senha</h3>

    <form id="form-senha">

      <label>
        Senha atual
        <input
          type="password"
          name="senhaAtual"
          required
        >
      </label>

      <label>
        Nova senha
        <input
          type="password"
          name="novaSenha"
          minlength="8"
          required
        >
      </label>

      <button type="submit">
        Salvar
      </button>

    </form>

    ${aviso()}
  `;

    document.getElementById('form-senha').onsubmit =
        async evento => {
            evento.preventDefault();

            const dados = Object.fromEntries(
                new FormData(evento.currentTarget).entries()
            );

            try {
                await api(
                    '/auth/trocar-senha',
                    'POST',
                    dados
                );

                painel.innerHTML = `
          <h3>Senha atualizada!</h3>
          <p>Você já pode usar sua nova senha.</p>
        `;

            } catch (erro) {
                mostrarErro(erro);
            }
        };
}

async function sair() {
    try {
        await api('/auth/logout', 'POST');
    } catch (erro) {
        console.warn(erro);
    }

    telaLogin();
}

function abrirTela(tela) {
    if (!conta) {
        return telaLogin();
    }

    if (tela === 'descarte') {
        return telaDescarte();
    }

    if (tela === 'historico') {
        return telaHistorico();
    }

    if (tela === 'senha') {
        return telaSenha();
    }

    return telaPontos();
}

function atualizarRelogio() {
    document.getElementById('clock').textContent =
        new Date().toLocaleTimeString(
            'pt-BR',
            {
                hour: '2-digit',
                minute: '2-digit'
            }
        );
}

atualizarRelogio();
setInterval(atualizarRelogio, 30000);

configurarMenu();

(async () => {
    try {
        await atualizarConta();
        await telaPontos();

    } catch {
        telaLogin();
    }
})();
