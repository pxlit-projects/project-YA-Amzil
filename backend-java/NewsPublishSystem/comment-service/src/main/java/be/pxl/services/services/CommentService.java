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
    public void createComment(Long postId, Long userId, CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(commentRequest.getContent())
                .creationDate(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
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
    public CommentResponse editComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + commentId + "]"));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User [" + userId + "] is not the author of the comment");
        }

        comment.setContent(newContent);
        comment.setEditDate(LocalDateTime.now());
        commentRepository.save(comment);

        return mapToResponse(comment);
    }

    /**
     * US12: Marks a user's own comment as deleted, so they can remove their contributions when needed.
     */
    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + commentId + "]"));

        // Ensure that only the owner of the comment can delete it
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User [" + userId + "] is not the author of the comment");
        }

        comment.setDeleted(true); // Mark the comment as deleted
        commentRepository.save(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .creationDate(comment.getCreationDate())
                .editDate(comment.getEditDate())
                .isDeleted(comment.isDeleted())
                .build();
    }
}
