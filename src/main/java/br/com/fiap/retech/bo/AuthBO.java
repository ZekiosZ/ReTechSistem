
package br.com.fiap.retech.bo;

import br.com.fiap.retech.dao.AuthDAO;
import br.com.fiap.retech.dto.request.*;
import br.com.fiap.retech.exception.RegraNegocioException;
import br.com.fiap.retech.model.Usuario;
import br.com.fiap.retech.security.Senhas;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthBO {

    private final AuthDAO dao;
    private final UsuarioBO usuarios;
    private final Senhas senhas;

    public AuthBO(
            AuthDAO dao,
            UsuarioBO usuarios,
            Senhas senhas
    ) {
        this.dao = dao;
        this.usuarios = usuarios;
        this.senhas = senhas;
    }

    public Usuario entrar(LoginContaRequest req) {

        AuthDAO.Credenciais credenciais =
                dao.porCpf(req.cpf())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "CPF ou senha incorretos"
                                )
                        );

        if (!senhas.conferir(
                req.senha(),
                credenciais.senhaHash()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "CPF ou senha incorretos"
            );
        }

        return usuarios.buscar(credenciais.idUsuario());
    }

    @Transactional
    public Usuario cadastrar(CadastroContaRequest req) {

        if (dao.cpfExiste(req.cpf())) {
            throw new RegraNegocioException(
                    "CPF ja cadastrado"
            );
        }

        Usuario usuario = usuarios.criar(
                new UsuarioRequest(
                        req.nomeCompleto().trim(),
                        req.cpf()
                )
        );

        dao.atualizarSenha(
                usuario.idUsuario(),
                senhas.gerarHash(req.senha())
        );

        return usuario;
    }

    @Transactional
    public void trocarSenha(Long id, TrocarSenhaRequest req) {

        String hashAtual = dao.senhaPorUsuario(id);

        if (!senhas.conferir(
                req.senhaAtual(),
                hashAtual
        )) {
            throw new RegraNegocioException(
                    "Senha atual incorreta"
            );
        }

        dao.atualizarSenha(
                id,
                senhas.gerarHash(req.novaSenha())
        );
    }

    public Usuario buscar(Long id) {
        return usuarios.buscar(id);
    }

    public String perfil(Long id) {
        return dao.perfil(id);
    }
}
