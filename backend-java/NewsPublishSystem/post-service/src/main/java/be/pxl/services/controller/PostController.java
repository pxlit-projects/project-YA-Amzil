package be.pxl.services.controller;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.createPost(postRequest);
        log.info("Creating new post");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Get one post
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse post = postService.getPost(postId);
        log.info("Getting post with id: {}", postId);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    // Update an existing post
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        PostResponse updatedPost = postService.updatePost(postId, postRequest);
        log.info("Updating post with id: {}", postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedPost);
    }

    // Get all posts
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> getAllPosts = postService.getAllPosts();
        log.info("Getting all posts");
        return ResponseEntity.status(HttpStatus.OK).body(getAllPosts);
    }

    // Get all draft posts
    @GetMapping("/draft")
    public ResponseEntity<List<PostResponse>> getAllDraftPosts() {
        List<PostResponse> draftPosts = postService.getAllDraftPosts();
        log.info("Getting all draft posts");
        return ResponseEntity.status(HttpStatus.OK).body(draftPosts);
    }

    // Get all pending posts
    @GetMapping("/pending")
    public ResponseEntity<List<PostResponse>> getAllPendingPosts() {
        List<PostResponse> pendingPosts = postService.getAllPendingPosts();
        log.info("Getting all pending posts");
        return ResponseEntity.status(HttpStatus.OK).body(pendingPosts);
    }

    // Get all published posts
    @GetMapping("/published")
    public ResponseEntity<List<PostResponse>> getAllPublishedPosts() {
        List<PostResponse> publishedPosts = postService.getAllPublishedPosts();
        log.info("Getting all published posts");
        return ResponseEntity.status(HttpStatus.OK).body(publishedPosts);
    }

    // Get all draft and pending posts
    @GetMapping("/draft-pending")
    public ResponseEntity<List<PostResponse>> getAllDraftAndPendingPosts() {
        List<PostResponse>  draftAndPendingPosts = postService.getAllDraftAndPendingPosts();
        log.info("Getting all draft and pending posts");
        return ResponseEntity.status(HttpStatus.OK).body(draftAndPendingPosts);
    }
}
