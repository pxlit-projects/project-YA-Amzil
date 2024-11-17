package be.pxl.services.exceptions;

public class ReviewCreationException extends RuntimeException {
    public ReviewCreationException(String message) {
        super(message);
    }
}