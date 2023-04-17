package guru.springframework.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {

        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
            .map(fieldError -> {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                return errorMap;
            })
            .toList();

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<List<Map<String, String>>> handleJpaValidation(TransactionSystemException exception) {

        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();
        if (exception.getCause().getCause() instanceof ConstraintViolationException constraintViolationException) {
            List<Map<String, String>> errorList = constraintViolationException.getConstraintViolations().stream()
                .map(error -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(error.getPropertyPath().toString(), error.getMessage());
                    return errorMap;
                })
                .toList();
            return responseEntity.body(errorList);
        }
        return responseEntity.build();
    }

}
