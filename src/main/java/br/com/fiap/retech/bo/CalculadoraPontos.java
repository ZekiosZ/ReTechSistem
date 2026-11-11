package br.com.fiap.retech.bo;
import br.com.fiap.retech.exception.RegraNegocioException;
public final class CalculadoraPontos {
    private CalculadoraPontos() {}
    public static int calcular(int quantidade, int pontosPorUnidade) {
        if (quantidade < 1 || quantidade > 999 || pontosPorUnidade < 0 || pontosPorUnidade > 9999)
            throw new RegraNegocioException("Quantidade ou pontuacao fora do limite da tabela");
        long total=(long)quantidade*pontosPorUnidade;
        if (total>999_999) throw new RegraNegocioException("Pontos do descarte excedem NUMBER(6)");
        return (int)total;
    }
    public static int novoSaldo(int saldoAtual,int delta) {
        long saldo=(long)saldoAtual+delta;
        if(saldo<0 || saldo>999_999) throw new RegraNegocioException("Saldo de pontos fora do intervalo permitido");
        return (int)saldo;
    }
}
