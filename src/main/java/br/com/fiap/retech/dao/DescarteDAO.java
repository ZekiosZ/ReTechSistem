package br.com.fiap.retech.dao;
import br.com.fiap.retech.model.Descarte;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class DescarteDAO extends OracleDAO {
    public DescarteDAO(NamedParameterJdbcTemplate db){super(db);}
    private static final RowMapper<Descarte> M=(r,n)->new Descarte(
      r.getLong("ID_DESCARTE"),r.getLong("ID_USUARIO"),r.getLong("ID_MAQUINA"),
      r.getLong("ID_CATEGORIA"),r.getLong("ID_INSTITUICAO"),
      r.getTimestamp("DATA_REGISTRO").toLocalDateTime(),r.getInt("QUANTIDADE"),r.getInt("PONTOS_GERADOS"));
    public List<Descarte> listar(){return db.query("SELECT * FROM T_DESCARTE ORDER BY ID_DESCARTE DESC",Map.of(),M);}
    public List<Descarte> listarPorUsuario(long id){return db.query("SELECT * FROM T_DESCARTE WHERE ID_USUARIO=:id ORDER BY ID_DESCARTE DESC",Map.of("id",id),M);}
    public Optional<Descarte> buscar(long id){return db.query("SELECT * FROM T_DESCARTE WHERE ID_DESCARTE=:id",Map.of("id",id),M).stream().findFirst();}
    public Descarte criar(long usuario,long maquina,long categoria,long instituicao,int quantidade,int pontos){
      long id=proximoId("SEQ_T_DESCARTE");
      var p=new MapSqlParameterSource().addValue("id",id).addValue("usuario",usuario).addValue("maquina",maquina)
         .addValue("categoria",categoria).addValue("instituicao",instituicao)
         .addValue("quantidade",quantidade).addValue("pontos",pontos);
      db.update("INSERT INTO T_DESCARTE(ID_DESCARTE,ID_USUARIO,ID_MAQUINA,ID_CATEGORIA,ID_INSTITUICAO,"+
        "DATA_REGISTRO,QUANTIDADE,PONTOS_GERADOS) VALUES(:id,:usuario,:maquina,:categoria,:instituicao,SYSDATE,:quantidade,:pontos)",p);
      return buscar(id).orElseThrow();
    }
    public void atualizar(long id,long maquina,long categoria,long instituicao,int quantidade,int pontos){
      var p=new MapSqlParameterSource().addValue("id",id).addValue("maquina",maquina)
         .addValue("categoria",categoria).addValue("instituicao",instituicao)
         .addValue("quantidade",quantidade).addValue("pontos",pontos);
      db.update("UPDATE T_DESCARTE SET ID_MAQUINA=:maquina,ID_CATEGORIA=:categoria,ID_INSTITUICAO=:instituicao,"+
        "QUANTIDADE=:quantidade,PONTOS_GERADOS=:pontos WHERE ID_DESCARTE=:id",p);
    }
    public void excluir(long id){db.update("DELETE FROM T_DESCARTE WHERE ID_DESCARTE=:id",Map.of("id",id));}
    public void bloquear(long id){db.query("SELECT ID_DESCARTE FROM T_DESCARTE WHERE ID_DESCARTE=:id FOR UPDATE",Map.of("id",id),(r,n)->r.getLong(1));}
}
