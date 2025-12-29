package br.com.challenge.fictcred.validation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.challenge.fictcred.exceptions.ErrorObject;
import br.com.challenge.fictcred.exceptions.ErrorReponse;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorObject> errors = getErros(ex);
        ErrorReponse errorReponse = getErrorResponse(ex, status, errors);
        return new ResponseEntity<>(errorReponse, status);
    }

    private ErrorReponse getErrorResponse(MethodArgumentNotValidException ex, HttpStatus status, List<ErrorObject> errors){
        return new ErrorReponse("The request has empty fields", status.value(),
                status.getReasonPhrase(), ex.getBindingResult().getObjectName(), errors);
    }

    private List<ErrorObject> getErros(MethodArgumentNotValidException ex){
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorObject(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
                .collect(Collectors.toList());
    }
}