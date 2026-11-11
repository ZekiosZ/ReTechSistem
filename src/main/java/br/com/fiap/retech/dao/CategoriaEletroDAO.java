package br.com.fiap.retech.dao;
import br.com.fiap.retech.dto.request.CategoriaEletroRequest;
import br.com.fiap.retech.model.CategoriaEletro;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class CategoriaEletroDAO extends OracleDAO {
    public CategoriaEletroDAO(NamedParameterJdbcTemplate db){super(db);}
    private static final RowMapper<CategoriaEletro> M=(r,n)->new CategoriaEletro(
      r.getLong("ID_CATEGORIA"),r.getString("NOME_CATEGORIA"),r.getInt("PONTOS_POR_UNIDADE"));
    public List<CategoriaEletro> listar(){return db.query("SELECT * FROM T_CATEGORIA_ELETRO ORDER BY ID_CATEGORIA",Map.of(),M);}
    public Optional<CategoriaEletro> buscar(long id){return db.query("SELECT * FROM T_CATEGORIA_ELETRO WHERE ID_CATEGORIA=:id",Map.of("id",id),M).stream().findFirst();}
    public CategoriaEletro criar(CategoriaEletroRequest r){
       long id=proximoId("SEQ_T_CATEGORIA_ELETRO");
       db.update("INSERT INTO T_CATEGORIA_ELETRO(ID_CATEGORIA,NOME_CATEGORIA,PONTOS_POR_UNIDADE) VALUES(:id,:nome,:pontos)",
         Map.of("id",id,"nome",r.nomeCategoria(),"pontos",r.pontosPorUnidade()));
       return buscar(id).orElseThrow();
    }
    public void atualizar(long id,CategoriaEletroRequest r){
       db.update("UPDATE T_CATEGORIA_ELETRO SET NOME_CATEGORIA=:nome,PONTOS_POR_UNIDADE=:pontos WHERE ID_CATEGORIA=:id",
         Map.of("id",id,"nome",r.nomeCategoria(),"pontos",r.pontosPorUnidade()));
    }
    public void excluir(long id){db.update("DELETE FROM T_CATEGORIA_ELETRO WHERE ID_CATEGORIA=:id",Map.of("id",id));}
}
