package be.pxl.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends RuntimeException>, HttpStatus> exceptionStatusMap = Map.of(
            ReviewCreationException.class, HttpStatus.UNPROCESSABLE_ENTITY,
            ReviewDeletionException.class, HttpStatus.FORBIDDEN,
            ReviewNotFoundException.class, HttpStatus.NOT_FOUND,
            ReviewPublishException.class, HttpStatus.FORBIDDEN,
            ReviewUpdateException.class, HttpStatus.CONFLICT
    );

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlePostException(RuntimeException ex) {
        HttpStatus status = exceptionStatusMap.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ex.getMessage(), status);
    }
}
