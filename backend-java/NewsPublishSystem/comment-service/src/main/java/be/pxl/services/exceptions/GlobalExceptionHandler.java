package be.pxl.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends RuntimeException>, HttpStatus> exceptionStatusMap = Map.of(
            CommentCreationException.class, HttpStatus.UNPROCESSABLE_ENTITY,
            CommentDeletionException.class, HttpStatus.FORBIDDEN,
            CommentNotFoundException.class, HttpStatus.NOT_FOUND,
            CommentPublishException.class, HttpStatus.FORBIDDEN,
            CommentUpdateException.class, HttpStatus.CONFLICT
    );

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlePostException(RuntimeException ex) {
        HttpStatus status = exceptionStatusMap.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ex.getMessage(), status);
    }
}
