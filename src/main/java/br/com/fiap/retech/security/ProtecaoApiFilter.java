
package br.com.fiap.retech.security;

import br.com.fiap.retech.controller.AuthController;
import br.com.fiap.retech.dao.AuthDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@Order(1)
public class ProtecaoApiFilter extends OncePerRequestFilter {

    private final AuthDAO auth;

    public ProtecaoApiFilter(AuthDAO auth) {
        this.auth = auth;
    }

    private void negar(
            HttpServletResponse resp,
            int status,
            String mensagem
    ) throws IOException {

        resp.setStatus(status);
        resp.setContentType(
                "application/json;charset=UTF-8"
        );

        resp.getWriter().write(
                "{\"erro\":\"" + mensagem + "\"}"
        );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse resp,
            FilterChain chain
    ) throws ServletException, IOException {

        String caminho = req.getRequestURI()
                .substring(req.getContextPath().length());

        String metodo = req.getMethod();

        if (!caminho.startsWith("/api/v1/")) {
            chain.doFilter(req, resp);
            return;
        }

        if (metodo.equals("POST") &&
                (caminho.equals("/api/v1/auth/login") ||
                        caminho.equals("/api/v1/auth/cadastro"))) {

            chain.doFilter(req, resp);
            return;
        }

        HttpSession sessao = req.getSession(false);

        Long id = sessao == null
                ? null
                : (Long) sessao.getAttribute(
                AuthController.ID_SESSAO
        );

        if (id == null) {
            negar(resp, 401, "Login necessario");
            return;
        }

        boolean escrita =
                !metodo.equals("GET") &&
                        !metodo.equals("HEAD") &&
                        !metodo.equals("OPTIONS");

        if (escrita) {

            String esperado = (String) sessao.getAttribute(
                    AuthController.CSRF_SESSAO
            );

            String enviado = req.getHeader("X-CSRF-Token");

            if (esperado == null || enviado == null ||
                    !MessageDigest.isEqual(
                            esperado.getBytes(StandardCharsets.UTF_8),
                            enviado.getBytes(StandardCharsets.UTF_8)
                    )) {

                negar(resp, 403, "Token de seguranca invalido");
                return;
            }
        }

        boolean admin = "ADMIN".equals(auth.perfil(id));

        if (caminho.startsWith("/api/v1/auth/")) {
            chain.doFilter(req, resp);
            return;
        }

        if (caminho.equals("/api/v1/impacto") &&
                metodo.equals("GET")) {

            chain.doFilter(req, resp);
            return;
        }

        if (caminho.startsWith("/api/v1/categorias") ||
                caminho.startsWith("/api/v1/maquinas") ||
                caminho.startsWith("/api/v1/instituicoes")) {

            if (metodo.equals("GET") || admin) {
                chain.doFilter(req, resp);
                return;
            }
        }

        if (caminho.startsWith("/api/v1/usuarios")) {

            if (admin) {
                chain.doFilter(req, resp);
                return;
            }

            String proprio = "/api/v1/usuarios/" + id;

            if (caminho.equals(proprio) &&
                    metodo.equals("GET")) {

                chain.doFilter(req, resp);
                return;
            }
        }

        if (caminho.startsWith("/api/v1/descartes")) {

            if (admin) {
                chain.doFilter(req, resp);
                return;
            }

            if (caminho.equals("/api/v1/descartes")) {

                if (metodo.equals("POST")) {
                    chain.doFilter(req, resp);
                    return;
                }

                String filtro = req.getParameter("idUsuario");

                if (metodo.equals("GET") &&
                        String.valueOf(id).equals(filtro)) {

                    chain.doFilter(req, resp);
                    return;
                }
            }
        }

        negar(resp, 403, "Acesso nao autorizado");
    }
}
