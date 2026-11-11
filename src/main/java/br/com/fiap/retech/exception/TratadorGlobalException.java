package br.com.fiap.retech.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
@RestControllerAdvice
public class TratadorGlobalException {
    private ResponseEntity<ErroApi> resposta(HttpStatus status, String msg, HttpServletRequest req, Map<String,String> campos) {
        return ResponseEntity.status(status).body(new ErroApi(Instant.now(),status.value(),msg,req.getRequestURI(),campos));
    }
    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<ErroApi> naoEncontrado(RegistroNaoEncontradoException e, HttpServletRequest r) {
        return resposta(HttpStatus.NOT_FOUND,e.getMessage(),r,Map.of());
    }
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroApi> negocio(RegraNegocioException e, HttpServletRequest r) {
        return resposta(HttpStatus.CONFLICT,e.getMessage(),r,Map.of());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApi> validacao(MethodArgumentNotValidException e, HttpServletRequest r) {
        Map<String,String> campos=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x->campos.put(x.getField(),x.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST,"Dados invalidos",r,campos);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroApi> json(HttpMessageNotReadableException e, HttpServletRequest r) {
        return resposta(HttpStatus.BAD_REQUEST,"JSON invalido",r,Map.of());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroApi> constraint(DataIntegrityViolationException e, HttpServletRequest r) {
        return resposta(HttpStatus.CONFLICT,"Registro vinculado, duplicado ou fora dos limites do banco",r,Map.of());
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErroApi> oracle(DataAccessException e, HttpServletRequest r) {
        return resposta(HttpStatus.SERVICE_UNAVAILABLE,"Falha ao acessar Oracle",r,Map.of());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroApi> geral(Exception e, HttpServletRequest r) {
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,"Erro interno na API",r,Map.of());
    }
}
