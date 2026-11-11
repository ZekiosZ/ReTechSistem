package br.com.fiap.retech.model;
    import java.time.LocalDateTime;
    public record Usuario(Long idUsuario, String nomeCompleto, String cpf, int pontosSaldo, LocalDateTime dataCadastro) {}
