package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.exceptions.CommentNotFoundException;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    // US10
    @Override
    public void createComment(CommentRequest commentRequest) {
        log.info("Creating new comment");
        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .author(commentRequest.getAuthor())
                .content(commentRequest.getContent())
                .createAt(commentRequest.getCreateAt())
                .updateAt(commentRequest.getUpdateAt())
                .build();
        commentRepository.save(comment);
        log.info("Comment created successfully: {}", comment.getContent());
    }

    @Override
    public List<CommentResponse> getAllComments() {
        log.info("Getting all comments");
        return commentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // US11
    @Override
    public List<CommentResponse> getCommentsForPost(Long postId) {
        log.info("Getting comments for post with id: {}", postId);
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // US12
    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        log.info("Updating comment with id: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + commentId + "]"));


        comment.setContent(commentRequest.getContent());
        comment.setUpdateAt(commentRequest.getUpdateAt());
        commentRepository.save(comment);
        log.info("Comment updated successfully: {}", comment.getContent());
        return mapToResponse(comment);
    }

    // US12
    @Override
    public boolean deleteComment(Long commentId) {
        log.info("Deleting comment with id: {}", commentId);
        return commentRepository.findById(commentId)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return true;
                })
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + commentId + "]"));
    }

    @Override
    public boolean deleteCommentsForPost(Long postId) {
        log.info("Deleting comments for post with id: {}", postId);
        List<Comment> comments = commentRepository.findByPostId(postId);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException("Comments not found for post with id [" + postId + "]");
        }
        commentRepository.deleteAll(comments);
        return true;
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
