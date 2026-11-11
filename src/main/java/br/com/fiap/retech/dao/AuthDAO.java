
package br.com.fiap.retech.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class AuthDAO extends OracleDAO {

    public AuthDAO(NamedParameterJdbcTemplate db) {
        super(db);
    }

    public record Credenciais(
            Long idUsuario,
            String senhaHash,
            String perfil
    ) {}

    public Optional<Credenciais> porCpf(String cpf) {

        return db.query(
                """
                SELECT ID_USUARIO, SENHA_HASH, PERFIL
                FROM T_USUARIO
                WHERE CPF = :cpf
                """,
                Map.of("cpf", cpf),
                (rs, n) -> new Credenciais(
                        rs.getLong("ID_USUARIO"),
                        rs.getString("SENHA_HASH"),
                        rs.getString("PERFIL")
                )
        ).stream().findFirst();
    }

    public boolean cpfExiste(String cpf) {

        Integer total = db.queryForObject(
                "SELECT COUNT(*) FROM T_USUARIO WHERE CPF = :cpf",
                Map.of("cpf", cpf),
                Integer.class
        );

        return total != null && total > 0;
    }

    public void atualizarSenha(Long id, String hash) {

        db.update(
                """
                UPDATE T_USUARIO
                SET SENHA_HASH = :senha
                WHERE ID_USUARIO = :id
                """,
                Map.of("id", id, "senha", hash)
        );
    }

    public String senhaPorUsuario(Long id) {

        return db.queryForObject(
                """
                SELECT SENHA_HASH
                FROM T_USUARIO
                WHERE ID_USUARIO = :id
                """,
                Map.of("id", id),
                String.class
        );
    }

    public String perfil(Long id) {

        return db.queryForObject(
                """
                SELECT PERFIL
                FROM T_USUARIO
                WHERE ID_USUARIO = :id
                """,
                Map.of("id", id),
                String.class
        );
    }
}
