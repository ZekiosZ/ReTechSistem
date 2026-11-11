
package br.com.fiap.retech.controller;

import br.com.fiap.retech.bo.DescarteBO;
import br.com.fiap.retech.dao.AuthDAO;
import br.com.fiap.retech.dto.request.*;
import br.com.fiap.retech.model.Descarte;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/descartes")
public class DescarteController {

    private final DescarteBO bo;
    private final AuthDAO auth;

    public DescarteController(
            DescarteBO bo,
            AuthDAO auth
    ) {
        this.bo = bo;
        this.auth = auth;
    }

    @GetMapping
    public List<Descarte> listar(
            @RequestParam(required = false) Long idUsuario
    ) {
        return idUsuario == null
                ? bo.listar()
                : bo.listarPorUsuario(idUsuario);
    }

    @GetMapping("/{id}")
    public Descarte buscar(@PathVariable long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Descarte> criar(
            @Valid @RequestBody DescarteRequest req,
            HttpSession sessao
    ) {

        Long usuarioLogado = (Long) sessao.getAttribute(
                AuthController.ID_SESSAO
        );

        boolean administrador = "ADMIN".equals(
                auth.perfil(usuarioLogado)
        );

        if (!usuarioLogado.equals(req.idUsuario())
                && !administrador) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Nao e permitido descartar para outra conta"
            );
        }

        Descarte novo = bo.criar(req);

        return ResponseEntity.created(
                URI.create("/api/v1/descartes/" + novo.idDescarte())
        ).body(novo);
    }

    @PutMapping("/{id}")
    public Descarte atualizar(
            @PathVariable long id,
            @Valid @RequestBody AtualizarDescarteRequest req
    ) {
        return bo.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        bo.excluir(id);
    }
}
