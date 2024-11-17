package be.pxl.services.exceptions;

public class CommentPublishException extends RuntimeException {
    public CommentPublishException(String message) {
        super(message);
    }
}