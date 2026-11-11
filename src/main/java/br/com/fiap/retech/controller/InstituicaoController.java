package br.com.fiap.retech.controller;
    import br.com.fiap.retech.bo.InstituicaoBO;
    import br.com.fiap.retech.dto.request.InstituicaoRequest;
    import br.com.fiap.retech.model.Instituicao;
    import jakarta.validation.Valid;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;
    import java.net.URI;
    import java.util.List;
    @RestController
    @RequestMapping("/api/v1/instituicoes")
    public class InstituicaoController {
        private final InstituicaoBO bo;
        public InstituicaoController(InstituicaoBO bo){this.bo=bo;}
        @GetMapping public List<Instituicao> listar(){return bo.listar();}
        @GetMapping("/{id}") public Instituicao buscar(@PathVariable long id){return bo.buscar(id);}
        @PostMapping public ResponseEntity<Instituicao> criar(@Valid @RequestBody InstituicaoRequest req){
            Instituicao novo=bo.criar(req);
            return ResponseEntity.created(URI.create("/api/v1/instituicoes/"+novo.idInstituicao())).body(novo);
        }
        @PutMapping("/{id}") public Instituicao atualizar(@PathVariable long id,@Valid @RequestBody InstituicaoRequest req){return bo.atualizar(id,req);}
        @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable long id){bo.excluir(id);}
    }
