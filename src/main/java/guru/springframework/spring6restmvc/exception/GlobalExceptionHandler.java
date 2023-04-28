package guru.springframework.spring6restmvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @deprecated (14.04.2024, moved to ResponseStatus approach)
     */
    @Deprecated(forRemoval = true)
    @ExceptionHandler(value = { NotFoundException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

        log.error("I'm in the global exception handler!");

        String bodyOfResponse = "Entity not found!";
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {

        String message = "Incorrect argument!";
        return handleExceptionInternal(ex, ex.getMessage() != null ? ex.getMessage() : message,
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
