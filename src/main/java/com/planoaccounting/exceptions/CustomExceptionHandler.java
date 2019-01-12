package com.planoaccounting.exceptions;

import com.planoaccounting.PlanoAccountingConstants;
import com.planoaccounting.model.EndResponse;
import com.planoaccounting.validation.FrameworkError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ValidationErrorsException.class)
    public ResponseEntity<EndResponse> handleValidationErrors(ValidationErrorsException ex, WebRequest request) {
        EndResponse endResponse = new EndResponse();
        endResponse.setValidationErrors(ex.getErrors());

        return new ResponseEntity<EndResponse>(endResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<EndResponse> handleUnexpectedExceptions(UnexpectedException ex, WebRequest request) {
        EndResponse endResponse = new EndResponse();
        endResponse.getErrors().add(new FrameworkError(PlanoAccountingConstants.ERR, ex.getMessage()));

        return new ResponseEntity<EndResponse>(endResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<EndResponse> handleAlreadyExistingData(ClientAlreadyExistsException ex, WebRequest request) {
        EndResponse endResponse = new EndResponse();
        endResponse.getErrors().add(new FrameworkError(PlanoAccountingConstants.ALE, ex.getMessage()));

        return new ResponseEntity<EndResponse>(endResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<EndResponse> handleNoSuchElementExceptions(NoSuchElementException ex, WebRequest request) {
        EndResponse endResponse = new EndResponse();
        endResponse.getErrors().add(new FrameworkError(PlanoAccountingConstants.NSE, ex.getMessage()));

        return new ResponseEntity<EndResponse>(endResponse, HttpStatus.NOT_FOUND);
    }
}
