
package br.com.fiap.retech.config;

import br.com.fiap.retech.security.Senhas;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class InicializacaoBanco implements ApplicationRunner {

    private final JdbcTemplate jdbc;
    private final Senhas senhas;

    public InicializacaoBanco(JdbcTemplate jdbc, Senhas senhas) {
        this.jdbc = jdbc;
        this.senhas = senhas;
    }

    private boolean existeColuna(String coluna) {
        Integer total = jdbc.queryForObject(
                """
                SELECT COUNT(*)
                FROM USER_TAB_COLUMNS
                WHERE TABLE_NAME = 'T_USUARIO'
                AND COLUMN_NAME = ?
                """,
                Integer.class,
                coluna
        );

        return total != null && total > 0;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (!existeColuna("SENHA_HASH")) {
            jdbc.execute(
                    "ALTER TABLE T_USUARIO ADD (SENHA_HASH VARCHAR2(255))"
            );
        }

        if (!existeColuna("PERFIL")) {
            jdbc.execute(
                    """
                    ALTER TABLE T_USUARIO ADD (
                        PERFIL VARCHAR2(20)
                        DEFAULT 'CIDADAO' NOT NULL
                    )
                    """
            );
        }

        // Somente para a base didatica com CPFs ficticios.
        String[] cpfsTeste = {
                "11111111111",
                "22222222222",
                "33333333333",
                "44444444444",
                "55555555555"
        };

        for (String cpf : cpfsTeste) {
            jdbc.update(
                    """
                    UPDATE T_USUARIO
                    SET SENHA_HASH = ?
                    WHERE CPF = ?
                    AND SENHA_HASH IS NULL
                    """,
                    senhas.gerarHash("ReTech#2026Teste"),
                    cpf
            );
        }

        sincronizarSequencia();

        System.out.println(
                "ReTech: estrutura de login verificada."
        );
    }

    private void sincronizarSequencia() {

        Long maiorId = jdbc.queryForObject(
                "SELECT NVL(MAX(ID_USUARIO), 0) FROM T_USUARIO",
                Long.class
        );

        Long proximoId = jdbc.queryForObject(
                "SELECT SEQ_T_USUARIO.NEXTVAL FROM DUAL",
                Long.class
        );

        if (maiorId != null && proximoId != null
                && proximoId <= maiorId) {

            long incremento = maiorId - proximoId + 1;

            jdbc.execute(
                    "ALTER SEQUENCE SEQ_T_USUARIO INCREMENT BY "
                            + incremento
            );

            try {
                jdbc.queryForObject(
                        "SELECT SEQ_T_USUARIO.NEXTVAL FROM DUAL",
                        Long.class
                );
            } finally {
                jdbc.execute(
                        "ALTER SEQUENCE SEQ_T_USUARIO INCREMENT BY 1"
                );
            }
        }
    }
}
