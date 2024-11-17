package be.pxl.services.exceptions;

public class ReviewDeletionException extends RuntimeException {
    public ReviewDeletionException(String message) {
        super(message);
    }
}