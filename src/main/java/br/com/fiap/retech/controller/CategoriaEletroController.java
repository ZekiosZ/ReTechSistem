package br.com.fiap.retech.controller;
    import br.com.fiap.retech.bo.CategoriaEletroBO;
    import br.com.fiap.retech.dto.request.CategoriaEletroRequest;
    import br.com.fiap.retech.model.CategoriaEletro;
    import jakarta.validation.Valid;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;
    import java.net.URI;
    import java.util.List;
    @RestController
    @RequestMapping("/api/v1/categorias")
    public class CategoriaEletroController {
        private final CategoriaEletroBO bo;
        public CategoriaEletroController(CategoriaEletroBO bo){this.bo=bo;}
        @GetMapping public List<CategoriaEletro> listar(){return bo.listar();}
        @GetMapping("/{id}") public CategoriaEletro buscar(@PathVariable long id){return bo.buscar(id);}
        @PostMapping public ResponseEntity<CategoriaEletro> criar(@Valid @RequestBody CategoriaEletroRequest req){
            CategoriaEletro novo=bo.criar(req);
            return ResponseEntity.created(URI.create("/api/v1/categorias/"+novo.idCategoria())).body(novo);
        }
        @PutMapping("/{id}") public CategoriaEletro atualizar(@PathVariable long id,@Valid @RequestBody CategoriaEletroRequest req){return bo.atualizar(id,req);}
        @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable long id){bo.excluir(id);}
    }
