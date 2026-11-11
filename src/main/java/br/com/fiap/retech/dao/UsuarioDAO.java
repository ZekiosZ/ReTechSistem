package br.com.fiap.retech.dao;
import br.com.fiap.retech.dto.request.UsuarioRequest;
import br.com.fiap.retech.model.Usuario;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public class UsuarioDAO extends OracleDAO {
    public UsuarioDAO(NamedParameterJdbcTemplate db) { super(db); }
    private static final RowMapper<Usuario> M=(r,n)->new Usuario(r.getLong("ID_USUARIO"),r.getString("NOME_COMPLETO"),
       r.getString("CPF"),r.getInt("PONTOS_SALDO"),r.getTimestamp("DATA_CADASTRO").toLocalDateTime());
    public List<Usuario> listar() {return db.query("SELECT * FROM T_USUARIO ORDER BY ID_USUARIO", Map.of(),M);}
    public Optional<Usuario> buscar(long id) {return db.query("SELECT * FROM T_USUARIO WHERE ID_USUARIO=:id",Map.of("id",id),M).stream().findFirst();}
    public Usuario criar(UsuarioRequest r) {
        long id=proximoId("SEQ_T_USUARIO");
        db.update("INSERT INTO T_USUARIO(ID_USUARIO,NOME_COMPLETO,CPF,PONTOS_SALDO,DATA_CADASTRO) VALUES (:id,:nome,:cpf,0,SYSDATE)",
          Map.of("id",id,"nome",r.nomeCompleto(),"cpf",r.cpf()));
        return buscar(id).orElseThrow();
    }
    public void atualizar(long id,UsuarioRequest r) {
        db.update("UPDATE T_USUARIO SET NOME_COMPLETO=:nome,CPF=:cpf WHERE ID_USUARIO=:id",
          Map.of("id",id,"nome",r.nomeCompleto(),"cpf",r.cpf()));
    }
    public void bloquear(long id) {
        db.query("SELECT ID_USUARIO FROM T_USUARIO WHERE ID_USUARIO=:id FOR UPDATE",Map.of("id",id),(r,n)->r.getLong(1));
    }
    public void atualizarSaldo(long id,int novoSaldo) {
        db.update("UPDATE T_USUARIO SET PONTOS_SALDO=:saldo WHERE ID_USUARIO=:id",Map.of("id",id,"saldo",novoSaldo));
    }
    public void excluir(long id){db.update("DELETE FROM T_USUARIO WHERE ID_USUARIO=:id",Map.of("id",id));}
}
