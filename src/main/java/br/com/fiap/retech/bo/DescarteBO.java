package br.com.fiap.retech.bo;
import br.com.fiap.retech.dao.*;
import br.com.fiap.retech.dto.request.*;
import br.com.fiap.retech.exception.*;
import br.com.fiap.retech.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class DescarteBO {
    private final DescarteDAO descartes;
    private final UsuarioDAO usuarios;
    private final CategoriaEletroDAO categorias;
    private final MaquinaDAO maquinas;
    private final InstituicaoDAO instituicoes;
    public DescarteBO(DescarteDAO d, UsuarioDAO u, CategoriaEletroDAO c, MaquinaDAO m, InstituicaoDAO i) {
        this.descartes=d;this.usuarios=u;this.categorias=c;this.maquinas=m;this.instituicoes=i;
    }
    public List<Descarte> listar(){return descartes.listar();}
    public List<Descarte> listarPorUsuario(long id){usuario(id);return descartes.listarPorUsuario(id);}
    public Descarte buscar(long id){return descartes.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Descarte nao encontrado"));}
    private Usuario usuario(long id){return usuarios.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Usuario nao encontrado"));}
    private CategoriaEletro categoria(long id){return categorias.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Categoria nao encontrada"));}
    private void validarRelacionamentos(long maquina,long instituicao){
        if(maquinas.buscar(maquina).isEmpty()) throw new RegistroNaoEncontradoException("Maquina nao encontrada");
        if(instituicoes.buscar(instituicao).isEmpty()) throw new RegistroNaoEncontradoException("Instituicao nao encontrada");
    }
    // Incluir/descontar pontos somente na mesma transacao Oracle do descarte.
    @Transactional
    public Descarte criar(DescarteRequest req) {
        usuarios.bloquear(req.idUsuario());
        Usuario u=usuario(req.idUsuario());
        CategoriaEletro c=categoria(req.idCategoria());
        validarRelacionamentos(req.idMaquina(),req.idInstituicao());
        int pontos=CalculadoraPontos.calcular(req.quantidade(),c.pontosPorUnidade());
        int saldo=CalculadoraPontos.novoSaldo(u.pontosSaldo(),pontos);
        Descarte criado=descartes.criar(req.idUsuario(),req.idMaquina(),req.idCategoria(),req.idInstituicao(),req.quantidade(),pontos);
        usuarios.atualizarSaldo(u.idUsuario(),saldo);
        return criado;
    }
    @Transactional
    public Descarte atualizar(long id,AtualizarDescarteRequest req) {
        // Bloquear o descarte antes de bloquear o usuario evita edicoes concorrentes no mesmo ID.
        descartes.bloquear(id);
        Descarte antigo=buscar(id);
        usuarios.bloquear(antigo.idUsuario());
        Usuario u=usuario(antigo.idUsuario());
        CategoriaEletro c=categoria(req.idCategoria());
        validarRelacionamentos(req.idMaquina(),req.idInstituicao());
        int pontos=CalculadoraPontos.calcular(req.quantidade(),c.pontosPorUnidade());
        int saldo=CalculadoraPontos.novoSaldo(u.pontosSaldo(),pontos-antigo.pontosGerados());
        descartes.atualizar(id,req.idMaquina(),req.idCategoria(),req.idInstituicao(),req.quantidade(),pontos);
        usuarios.atualizarSaldo(u.idUsuario(),saldo);
        return buscar(id);
    }
    @Transactional
    public void excluir(long id) {
        descartes.bloquear(id);
        Descarte antigo=buscar(id);
        usuarios.bloquear(antigo.idUsuario());
        Usuario u=usuario(antigo.idUsuario());
        int saldo=CalculadoraPontos.novoSaldo(u.pontosSaldo(),-antigo.pontosGerados());
        descartes.excluir(id);
        usuarios.atualizarSaldo(u.idUsuario(),saldo);
    }
}
