package br.com.fiap.retech.bo;
    import br.com.fiap.retech.dao.InstituicaoDAO;
    import br.com.fiap.retech.dto.request.InstituicaoRequest;
    import br.com.fiap.retech.exception.RegistroNaoEncontradoException;
    import br.com.fiap.retech.model.Instituicao;
    import org.springframework.stereotype.Service;
    import java.util.List;
    @Service
    public class InstituicaoBO {
        private final InstituicaoDAO dao;
        public InstituicaoBO(InstituicaoDAO dao){this.dao=dao;}
        public List<Instituicao> listar(){return dao.listar();}
        public Instituicao buscar(long id){return dao.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Instituicao nao encontrado"));}
        public Instituicao criar(InstituicaoRequest req){return dao.criar(req);}
        public Instituicao atualizar(long id,InstituicaoRequest req){buscar(id);dao.atualizar(id,req);return buscar(id);}
        public void excluir(long id){buscar(id);dao.excluir(id);}
    }
