package br.com.fiap.retech.bo;
    import br.com.fiap.retech.dao.UsuarioDAO;
    import br.com.fiap.retech.dto.request.UsuarioRequest;
    import br.com.fiap.retech.exception.RegistroNaoEncontradoException;
    import br.com.fiap.retech.model.Usuario;
    import org.springframework.stereotype.Service;
    import java.util.List;
    @Service
    public class UsuarioBO {
        private final UsuarioDAO dao;
        public UsuarioBO(UsuarioDAO dao){this.dao=dao;}
        public List<Usuario> listar(){return dao.listar();}
        public Usuario buscar(long id){return dao.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Usuario nao encontrado"));}
        public Usuario criar(UsuarioRequest req){return dao.criar(req);}
        public Usuario atualizar(long id,UsuarioRequest req){buscar(id);dao.atualizar(id,req);return buscar(id);}
        public void excluir(long id){buscar(id);dao.excluir(id);}
    }
