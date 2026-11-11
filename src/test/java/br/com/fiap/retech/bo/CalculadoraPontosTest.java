package br.com.fiap.retech.bo;
import br.com.fiap.retech.exception.RegraNegocioException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CalculadoraPontosTest {
    @Test void duasUnidadesDe120Geram240Pontos() {
        assertEquals(240,CalculadoraPontos.calcular(2,120));
    }
    @Test void atualizacaoDoDescarteUsaApenasDiferencaDePontos() {
        int saldoOriginal=240;
        int pontosAnteriores=240;
        int pontosAtualizados=CalculadoraPontos.calcular(3,120);
        assertEquals(360,CalculadoraPontos.novoSaldo(saldoOriginal,pontosAtualizados-pontosAnteriores));
    }
    @Test void excluirDescarteEstornaPontos() {
        assertEquals(0,CalculadoraPontos.novoSaldo(360,-360));
    }
    @Test void naoPermiteUltrapassarNumber6() {
        assertThrows(RegraNegocioException.class,()->CalculadoraPontos.calcular(999,9999));
    }
    @Test void impedeSaldoNegativo() {
        assertThrows(RegraNegocioException.class,()->CalculadoraPontos.novoSaldo(1,-2));
    }
}
