package de.kaufland.ksilence.api;

import de.kaufland.ksilence.exception.EmptyParameterException;
import de.kaufland.ksilence.exception.EntityExistsException;
import de.kaufland.ksilence.exception.EntityNotFoundException;
import de.kaufland.ksilence.model.Error;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

    public ResponseEntity<Error> error(HttpServletRequest request, Exception e, HttpStatus httpStatus) {
        Error error = new Error();
        error.setError(httpStatus.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setException(e.getClass().getCanonicalName());
        error.setUrl(request.getRequestURI());
        error.setStatusCode(httpStatus.value());
        LoggerFactory.getLogger(e.getStackTrace()[0].getClassName()).error(e.getMessage());
        return new ResponseEntity<Error>(error, httpStatus);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<Error> defaultErrorHandler(HttpServletRequest request, Exception e) {
        return error(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = EmptyParameterException.class)
    public ResponseEntity<Error> handleEmptyParameter(HttpServletRequest request, Exception e) {
        return error(request, e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Error> handleEntityNotFound(HttpServletRequest request, Exception e) {
        return error(request, e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseEntity<Error> handleEntityExists(HttpServletRequest request, Exception e) {
        return error(request, e, HttpStatus.CONFLICT);
    }

}
