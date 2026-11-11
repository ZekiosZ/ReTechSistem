
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

        garantirSequencia(
                "SEQ_T_USUARIO",
                "T_USUARIO",
                "ID_USUARIO"
        );

        garantirSequencia(
                "SEQ_T_CATEGORIA_ELETRO",
                "T_CATEGORIA_ELETRO",
                "ID_CATEGORIA"
        );

        garantirSequencia(
                "SEQ_T_MAQUINA",
                "T_MAQUINA",
                "ID_MAQUINA"
        );

        garantirSequencia(
                "SEQ_T_INSTITUICAO",
                "T_INSTITUICAO",
                "ID_INSTITUICAO"
        );

        garantirSequencia(
                "SEQ_T_DESCARTE",
                "T_DESCARTE",
                "ID_DESCARTE"
        );
    }

    private void garantirSequencia(
            String nomeSequencia,
            String nomeTabela,
            String colunaId
    ) {

        Integer existe = jdbc.queryForObject(
                """
                SELECT COUNT(*)
                FROM USER_SEQUENCES
                WHERE SEQUENCE_NAME = ?
                """,
                Integer.class,
                nomeSequencia
        );

        if (existe != null && existe > 0) {
            System.out.println(
                    "Sequencia existente: " + nomeSequencia
            );
            return;
        }

        Long proximoId = jdbc.queryForObject(
                "SELECT NVL(MAX(" + colunaId +
                        "), 0) + 1 FROM " + nomeTabela,
                Long.class
        );

        jdbc.execute(
                "CREATE SEQUENCE " + nomeSequencia +
                        " START WITH " + proximoId +
                        " INCREMENT BY 1 NOCACHE NOCYCLE"
        );

        System.out.println(
                "Sequencia criada: " + nomeSequencia
        );
    }

}
