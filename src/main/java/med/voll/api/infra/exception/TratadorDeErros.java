package med.voll.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.domain.ValidacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(DadosErrosValidacao::new).toList());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarErroRegrasDeNegocio(ValidacaoException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private record DadosErrosValidacao(String campo, String mensagem) {
        private DadosErrosValidacao(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
