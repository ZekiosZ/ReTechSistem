package br.com.fiap.retech.dao;
import br.com.fiap.retech.dto.request.InstituicaoRequest;
import br.com.fiap.retech.model.Instituicao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class InstituicaoDAO extends OracleDAO {
    public InstituicaoDAO(NamedParameterJdbcTemplate db){super(db);}
    private static final RowMapper<Instituicao> M=(r,n)->new Instituicao(r.getLong("ID_INSTITUICAO"),
      r.getString("NOME_FANTASIA"),r.getString("CNPJ"),r.getString("TIPO_PARCEIRO"));
    public List<Instituicao> listar(){return db.query("SELECT * FROM T_INSTITUICAO ORDER BY ID_INSTITUICAO",Map.of(),M);}
    public Optional<Instituicao> buscar(long id){return db.query("SELECT * FROM T_INSTITUICAO WHERE ID_INSTITUICAO=:id",Map.of("id",id),M).stream().findFirst();}
    public Instituicao criar(InstituicaoRequest r){
      long id=proximoId("SEQ_T_INSTITUICAO");
      db.update("INSERT INTO T_INSTITUICAO(ID_INSTITUICAO,NOME_FANTASIA,CNPJ,TIPO_PARCEIRO) VALUES(:id,:nome,:cnpj,:tipo)",
        Map.of("id",id,"nome",r.nomeFantasia(),"cnpj",r.cnpj(),"tipo",r.tipoParceiro()));
      return buscar(id).orElseThrow();
    }
    public void atualizar(long id,InstituicaoRequest r){
      db.update("UPDATE T_INSTITUICAO SET NOME_FANTASIA=:nome,CNPJ=:cnpj,TIPO_PARCEIRO=:tipo WHERE ID_INSTITUICAO=:id",
        Map.of("id",id,"nome",r.nomeFantasia(),"cnpj",r.cnpj(),"tipo",r.tipoParceiro()));
    }
    public void excluir(long id){db.update("DELETE FROM T_INSTITUICAO WHERE ID_INSTITUICAO=:id",Map.of("id",id));}
}
