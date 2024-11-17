package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    /**
     * US10: Endpoint to add a new comment.
     */
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestParam Long postId, @RequestParam Long userId, @RequestBody CommentRequest commentRequest) {
        commentService.createComment(postId, userId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * US11: Endpoint to fetch comments for a given post.
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * US12: Endpoint to edit a user's comment.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> editComment(@PathVariable Long commentId, @RequestParam Long userId, @RequestParam String newContent) {
        CommentResponse updatedComment = commentService.editComment(commentId, userId, newContent);
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }

    /**
     * US12: Endpoint to delete a user's comment.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
