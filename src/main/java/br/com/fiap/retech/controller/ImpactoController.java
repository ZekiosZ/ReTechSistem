package br.com.fiap.retech.controller;
import br.com.fiap.retech.dao.ImpactoDAO;
import br.com.fiap.retech.dto.response.ImpactoResumo;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/impacto")
public class ImpactoController {
    private final ImpactoDAO dao;
    public ImpactoController(ImpactoDAO dao){this.dao=dao;}
    @GetMapping public ImpactoResumo resumo(){return dao.resumo();}
}
