package be.pxl.services.services;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;

import java.util.List;

public interface ICommentService {
    // Method to create a comment
    void createComment(CommentRequest commentRequest);

    // Method get all comments
    List<CommentResponse> getAllComments();

    // Method to get all comments for a post
    List<CommentResponse> getCommentsForPost(Long postId);

    // Method to edit a comment
    CommentResponse updateComment(Long commentId, CommentRequest commentRequest);

    // Method to delete a comment
    boolean deleteComment(Long commentId);
}
