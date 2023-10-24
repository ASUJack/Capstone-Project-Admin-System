package dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.InputMismatchException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request)
    {
        String error = "Malformed JSON request";
        AdminApiError aae = new AdminApiError(HttpStatus.BAD_REQUEST, error, ex);
        return new ResponseEntity<>(aae, aae.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex)
    {
        AdminApiError aae = new AdminApiError(HttpStatus.NOT_FOUND);
        aae.setMessage(ex.getMessage());
        return new ResponseEntity<>(aae, aae.getStatus());
    }

    @ExceptionHandler(InputMismatchException.class)
    protected ResponseEntity<Object> handleInputMismatch(InputMismatchException ex)
    {
        AdminApiError aae = new AdminApiError(HttpStatus.PRECONDITION_FAILED, "Request body data mismatch", ex);
        return new ResponseEntity<>(aae, aae.getStatus());
    }

}
