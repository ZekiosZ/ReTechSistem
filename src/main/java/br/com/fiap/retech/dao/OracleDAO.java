package br.com.fiap.retech.dao;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.Map;
public abstract class OracleDAO {
    protected final NamedParameterJdbcTemplate db;
    protected OracleDAO(NamedParameterJdbcTemplate db) { this.db=db; }
    protected long proximoId(String sequence) {
        // Somente constantes internas, nunca valores enviados pelo usuario.
        Long id=db.queryForObject("SELECT "+sequence+".NEXTVAL FROM DUAL",Map.of(),Long.class);
        if (id==null) throw new IllegalStateException("Sequence nao gerou identificador");
        return id;
    }
}
