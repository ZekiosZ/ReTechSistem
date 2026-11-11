package br.com.fiap.retech.controller;
import br.com.fiap.retech.bo.DescarteBO;
import br.com.fiap.retech.dto.request.*;
import br.com.fiap.retech.model.Descarte;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api/v1/descartes")
public class DescarteController {
    private final DescarteBO bo;
    public DescarteController(DescarteBO bo){this.bo=bo;}
    @GetMapping public List<Descarte> listar(@RequestParam(required=false) Long idUsuario){
        return idUsuario==null?bo.listar():bo.listarPorUsuario(idUsuario);
    }
    @GetMapping("/{id}") public Descarte buscar(@PathVariable long id){return bo.buscar(id);}
    @PostMapping public ResponseEntity<Descarte> criar(@Valid @RequestBody DescarteRequest r){
        Descarte novo=bo.criar(r);
        return ResponseEntity.created(URI.create("/api/v1/descartes/"+novo.idDescarte())).body(novo);
    }
    @PutMapping("/{id}") public Descarte atualizar(@PathVariable long id,@Valid @RequestBody AtualizarDescarteRequest r){return bo.atualizar(id,r);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id){bo.excluir(id);}
}
