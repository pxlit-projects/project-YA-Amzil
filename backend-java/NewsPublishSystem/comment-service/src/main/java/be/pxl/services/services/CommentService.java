package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.exceptions.CommentNotFoundException;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    /**
     * US10: Adds a new comment to a post, allowing users to share their opinions or ask questions.
     */
    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .author(commentRequest.getAuthor())
                .content(commentRequest.getContent())
                .createAt(commentRequest.getCreateAt())
                .updateAt(commentRequest.getUpdateAt())
                .build();

        commentRepository.save(comment);

        return mapToResponse(comment);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * US11: Retrieves all comments for a specific post, so users can gain insight into the opinions or questions of others.
     */
    @Override
    public List<CommentResponse> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * US12: Edits a user's own comment, allowing them to correct or modify their contributions.
     */
    @Override
    public CommentResponse updateComment(Long commentId,  CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + commentId + "]"));


        comment.setContent(commentRequest.getContent());
        comment.setUpdateAt(LocalDateTime.now());
        commentRepository.save(comment);

        return mapToResponse(comment);
    }

    /**
     * US12: Marks a user's own comment as deleted, so they can remove their contributions when needed.
     */
    @Override
    public boolean deleteComment(Long commentId) {
       return commentRepository.findById(commentId)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return true;
                })
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + commentId + "]"));
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .createAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .build();
    }
}
