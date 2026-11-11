package br.com.fiap.retech.controller;
    import br.com.fiap.retech.bo.MaquinaBO;
    import br.com.fiap.retech.dto.request.MaquinaRequest;
    import br.com.fiap.retech.model.Maquina;
    import jakarta.validation.Valid;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;
    import java.net.URI;
    import java.util.List;
    @RestController
    @RequestMapping("/api/v1/maquinas")
    public class MaquinaController {
        private final MaquinaBO bo;
        public MaquinaController(MaquinaBO bo){this.bo=bo;}
        @GetMapping public List<Maquina> listar(){return bo.listar();}
        @GetMapping("/{id}") public Maquina buscar(@PathVariable long id){return bo.buscar(id);}
        @PostMapping public ResponseEntity<Maquina> criar(@Valid @RequestBody MaquinaRequest req){
            Maquina novo=bo.criar(req);
            return ResponseEntity.created(URI.create("/api/v1/maquinas/"+novo.idMaquina())).body(novo);
        }
        @PutMapping("/{id}") public Maquina atualizar(@PathVariable long id,@Valid @RequestBody MaquinaRequest req){return bo.atualizar(id,req);}
        @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable long id){bo.excluir(id);}
    }
