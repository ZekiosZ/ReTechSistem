package br.com.fiap.retech.dao;
import br.com.fiap.retech.dto.response.ImpactoResumo;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;
@Repository
public class ImpactoDAO extends OracleDAO {
    public ImpactoDAO(NamedParameterJdbcTemplate db){super(db);}
    private long total(String sql){Long x=db.queryForObject(sql,Map.of(),Long.class);return x==null?0:x;}
    public ImpactoResumo resumo(){return new ImpactoResumo(
        total("SELECT COUNT(*) FROM T_DESCARTE"),
        total("SELECT COALESCE(SUM(QUANTIDADE),0) FROM T_DESCARTE"),
        total("SELECT COUNT(DISTINCT ID_USUARIO) FROM T_DESCARTE"),
        total("SELECT COALESCE(SUM(PONTOS_GERADOS),0) FROM T_DESCARTE"));}
}
