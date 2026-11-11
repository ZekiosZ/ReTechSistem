package br.com.fiap.retech.bo;
    import br.com.fiap.retech.dao.MaquinaDAO;
    import br.com.fiap.retech.dto.request.MaquinaRequest;
    import br.com.fiap.retech.exception.RegistroNaoEncontradoException;
    import br.com.fiap.retech.model.Maquina;
    import org.springframework.stereotype.Service;
    import java.util.List;
    @Service
    public class MaquinaBO {
        private final MaquinaDAO dao;
        public MaquinaBO(MaquinaDAO dao){this.dao=dao;}
        public List<Maquina> listar(){return dao.listar();}
        public Maquina buscar(long id){return dao.buscar(id).orElseThrow(()->new RegistroNaoEncontradoException("Maquina nao encontrado"));}
        public Maquina criar(MaquinaRequest req){return dao.criar(req);}
        public Maquina atualizar(long id,MaquinaRequest req){buscar(id);dao.atualizar(id,req);return buscar(id);}
        public void excluir(long id){buscar(id);dao.excluir(id);}
    }
