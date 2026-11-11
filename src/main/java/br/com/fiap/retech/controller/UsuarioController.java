package br.com.fiap.retech.controller;
    import br.com.fiap.retech.bo.UsuarioBO;
    import br.com.fiap.retech.dto.request.UsuarioRequest;
    import br.com.fiap.retech.model.Usuario;
    import jakarta.validation.Valid;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;
    import java.net.URI;
    import java.util.List;
    @RestController
    @RequestMapping("/api/v1/usuarios")
    public class UsuarioController {
        private final UsuarioBO bo;
        public UsuarioController(UsuarioBO bo){this.bo=bo;}
        @GetMapping public List<Usuario> listar(){return bo.listar();}
        @GetMapping("/{id}") public Usuario buscar(@PathVariable long id){return bo.buscar(id);}
        @PostMapping public ResponseEntity<Usuario> criar(@Valid @RequestBody UsuarioRequest req){
            Usuario novo=bo.criar(req);
            return ResponseEntity.created(URI.create("/api/v1/usuarios/"+novo.idUsuario())).body(novo);
        }
        @PutMapping("/{id}") public Usuario atualizar(@PathVariable long id,@Valid @RequestBody UsuarioRequest req){return bo.atualizar(id,req);}
        @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable long id){bo.excluir(id);}
    }
