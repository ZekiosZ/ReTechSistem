function atualizarRelogio() {
    const agora = new Date();
    const horas = String(agora.getHours()).padStart(2, "0");
    const minutos = String(agora.getMinutes()).padStart(2, "0");
    document.getElementById("clock").textContent = `${horas}:${minutos}`;
}

setInterval(atualizarRelogio, 1000);
atualizarRelogio();

function abrirTela(tipo) {
    const painel = document.getElementById("painel-dinamico");

    if (tipo === "descarte") {
        painel.innerHTML = `
      <h3>Novo descarte</h3>
      <label>Nome do usuário</label>
      <input type="text" placeholder="Digite o nome do cidadão" />

      <label>Categoria do eletrônico</label>
      <select>
        <option>Celular</option>
        <option>Notebook</option>
        <option>Tablet</option>
        <option>Bateria</option>
      </select>

      <label>Quantidade</label>
      <input type="number" min="1" value="1" />

      <button onclick="simularDescarte()">Confirmar descarte</button>
    `;
    }

    if (tipo === "pontos") {
        painel.innerHTML = `
      <h3>Consulta de pontos</h3>
      <p><strong>Usuário:</strong> José Rafael</p>
      <p><strong>Saldo atual:</strong> 150 pontos</p>
      <ul>
        <li>Celular descartado: +50 pontos</li>
        <li>Bateria encaminhada: +20 pontos</li>
        <li>Notebook para reuso: +80 pontos</li>
      </ul>
    `;
    }

    if (tipo === "historico") {
        painel.innerHTML = `
      <h3>Histórico recente</h3>
      <ul>
        <li>03/11/2026 - Celular - Reuso - 50 pontos</li>
        <li>05/11/2026 - Bateria - Reciclagem - 20 pontos</li>
        <li>09/11/2026 - Notebook - Doação - 80 pontos</li>
      </ul>
    `;
    }
}

function simularDescarte() {
    const painel = document.getElementById("painel-dinamico");
    painel.innerHTML = `
    <h3>Descarte registrado com sucesso</h3>
    <p>Seu eletrônico foi recebido pela ReTech.</p>
    <p><strong>Pontos gerados:</strong> 50</p>
    <p><strong>Destino:</strong> Reuso</p>
  `;
}