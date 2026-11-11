
package br.com.fiap.retech.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

@Component
public class Senhas {

    private static final int ITERACOES = 150000;
    private final SecureRandom random = new SecureRandom();

    public String gerarHash(String senha) {

        byte[] sal = new byte[16];
        random.nextBytes(sal);

        byte[] hash = derivar(senha, sal, ITERACOES);

        return "pbkdf2$" + ITERACOES + "$"
                + Base64.getEncoder().encodeToString(sal)
                + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public boolean conferir(String senha, String armazenado) {

        if (senha == null || armazenado == null) {
            return false;
        }

        try {
            String[] partes = armazenado.split("\\$");

            if (partes.length != 4 ||
                    !"pbkdf2".equals(partes[0])) {
                return false;
            }

            int iteracoes = Integer.parseInt(partes[1]);

            if (iteracoes < 100000 || iteracoes > 1000000) {
                return false;
            }

            byte[] sal = Base64.getDecoder()
                    .decode(partes[2]);

            byte[] esperado = Base64.getDecoder()
                    .decode(partes[3]);

            byte[] atual = derivar(senha, sal, iteracoes);

            return MessageDigest.isEqual(
                    esperado, atual
            );

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private byte[] derivar(
            String senha,
            byte[] sal,
            int iteracoes
    ) {

        char[] caracteres = senha.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(
                caracteres, sal, iteracoes, 256
        );

        try {
            return SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao gerar hash", e
            );
        } finally {
            spec.clearPassword();
            Arrays.fill(caracteres, '\0');
        }
    }
}
