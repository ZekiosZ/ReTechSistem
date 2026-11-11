package br.com.fiap.retech.exception;
import java.time.Instant;
import java.util.Map;
public record ErroApi(Instant instante, int status, String mensagem, String caminho, Map<String,String> campos) {}
