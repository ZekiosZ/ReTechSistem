package br.com.fiap.retech.bo;
    import br.com.fiap.retech.dao.CategoriaEletroDAO;
    import br.com.fiap.retech.dto.request.CategoriaEletroRequest;
    import br.com.fiap.retech.exception.RegistroNaoEncontradoException;
    import br.com.fiap.retech.model.CategoriaEletro;
    import org.springframework.stereotype.Service;
    import java.util.List;
    @Service
    public class CategoriaEletroBO {
        private final CategoriaEletroDAO dao;
        public CategoriaEletroBO(CategoriaEletroDAO dao){this.dao=dao;}
        public List<CategoriaEletro> listar(){return dao.listar();}
        public CategoriaEletro buscar(long id){return dao.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Categoria nao encontrado"));}
        public CategoriaEletro criar(CategoriaEletroRequest req){return dao.criar(req);}
        public CategoriaEletro atualizar(long id,CategoriaEletroRequest req){buscar(id);dao.atualizar(id,req);return buscar(id);}
        public void excluir(long id){buscar(id);dao.excluir(id);}
    }
