
package br.com.fiap.retech.controller;

import br.com.fiap.retech.bo.AuthBO;
import br.com.fiap.retech.dto.request.*;
import br.com.fiap.retech.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public static final String ID_SESSAO =
            "RETECH_USUARIO_ID";

    public static final String CSRF_SESSAO =
            "RETECH_CSRF";

    private final AuthBO bo;
    private final SecureRandom random = new SecureRandom();

    public AuthController(AuthBO bo) {
        this.bo = bo;
    }

    public record ContaView(
            Long idUsuario,
            String nomeCompleto,
            int pontosSaldo,
            String perfil,
            String csrfToken
    ) {}

    private ContaView conta(
            Usuario usuario,
            HttpSession sessao
    ) {
        return new ContaView(
                usuario.idUsuario(),
                usuario.nomeCompleto(),
                usuario.pontosSaldo(),
                bo.perfil(usuario.idUsuario()),
                (String) sessao.getAttribute(CSRF_SESSAO)
        );
    }

    private ContaView iniciarSessao(
            Usuario usuario,
            HttpServletRequest request
    ) {

        HttpSession antiga = request.getSession(false);

        if (antiga != null) {
            antiga.invalidate();
        }

        HttpSession sessao = request.getSession(true);

        sessao.setMaxInactiveInterval(30 * 60);

        sessao.setAttribute(
                ID_SESSAO,
                usuario.idUsuario()
        );

        byte[] bytes = new byte[24];
        random.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        sessao.setAttribute(CSRF_SESSAO, token);

        return conta(usuario, sessao);
    }

    @PostMapping("/login")
    public ContaView login(
            @Valid @RequestBody LoginContaRequest req,
            HttpServletRequest request
    ) {
        Usuario usuario = bo.entrar(req);
        return iniciarSessao(usuario, request);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ContaView> cadastrar(
            @Valid @RequestBody CadastroContaRequest req,
            HttpServletRequest request
    ) {
        Usuario usuario = bo.cadastrar(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(iniciarSessao(usuario, request));
    }

    @GetMapping("/me")
    public ContaView me(HttpSession sessao) {

        Long id = (Long) sessao.getAttribute(ID_SESSAO);

        return conta(bo.buscar(id), sessao);
    }

    @PostMapping("/trocar-senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void trocarSenha(
            @Valid @RequestBody TrocarSenhaRequest req,
            HttpSession sessao
    ) {
        Long id = (Long) sessao.getAttribute(ID_SESSAO);
        bo.trocarSenha(id, req);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession sessao) {
        sessao.invalidate();
    }
}
