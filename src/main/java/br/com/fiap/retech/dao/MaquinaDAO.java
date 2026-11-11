package br.com.fiap.retech.dao;
import br.com.fiap.retech.dto.request.MaquinaRequest;
import br.com.fiap.retech.model.Maquina;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class MaquinaDAO extends OracleDAO {
    public MaquinaDAO(NamedParameterJdbcTemplate db){super(db);}
    private static final RowMapper<Maquina> M=(r,n)->new Maquina(r.getLong("ID_MAQUINA"),
      r.getString("BAIRRO_LOCALIZACAO"),r.getString("CEP"),r.getString("STATUS_OPERACAO"));
    public List<Maquina> listar(){return db.query("SELECT * FROM T_MAQUINA ORDER BY ID_MAQUINA",Map.of(),M);}
    public Optional<Maquina> buscar(long id){return db.query("SELECT * FROM T_MAQUINA WHERE ID_MAQUINA=:id",Map.of("id",id),M).stream().findFirst();}
    public Maquina criar(MaquinaRequest r){
      long id=proximoId("SEQ_T_MAQUINA");
      db.update("INSERT INTO T_MAQUINA(ID_MAQUINA,BAIRRO_LOCALIZACAO,CEP,STATUS_OPERACAO) VALUES(:id,:bairro,:cep,:status)",
        Map.of("id",id,"bairro",r.bairroLocalizacao(),"cep",r.cep(),"status",r.statusOperacao()));
      return buscar(id).orElseThrow();
    }
    public void atualizar(long id,MaquinaRequest r){
      db.update("UPDATE T_MAQUINA SET BAIRRO_LOCALIZACAO=:bairro,CEP=:cep,STATUS_OPERACAO=:status WHERE ID_MAQUINA=:id",
        Map.of("id",id,"bairro",r.bairroLocalizacao(),"cep",r.cep(),"status",r.statusOperacao()));
    }
    public void excluir(long id){db.update("DELETE FROM T_MAQUINA WHERE ID_MAQUINA=:id",Map.of("id",id));}
}
