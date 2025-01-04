package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest commentRequest) {
        commentService.createComment(commentRequest);
        log.info("Creating new comment");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.getAllComments();
        log.info("Getting all comments");
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsForPost(postId);
        log.info("Getting comments for post with id: {}", postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.updateComment(commentId, commentRequest);
        log.info("Updating comment with id: {}", commentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
       boolean isDeleted = commentService.deleteComment(commentId);
         log.info("Deleting comment with id: {}", commentId);
       return isDeleted ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
