package be.pxl.services.controller;

import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Update an existing post
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        PostResponse updatedPost = postService.updatePost(postId, postRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedPost);
    }

    // Publish a draft post
    @PutMapping("/{postId}/publish")
    public ResponseEntity<PostResponse> publishPost(@PathVariable Long postId) {
        PostResponse publishedPost = postService.publishPost(postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(publishedPost);
    }

    // Get all published posts
    @GetMapping("/published")
    public ResponseEntity<List<PostResponse>> getAllPublishedPosts() {
        List<PostResponse> publishedPosts = postService.getAllPublishedPosts();
        return ResponseEntity.status(HttpStatus.OK).body(publishedPosts);
    }

    // Get relevant posts based on a filter (e.g., title/content search)
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> getRelevantPosts(@RequestParam("filter") String filter) {
        List<PostResponse> relevantPosts = postService.getRelevantPosts(filter);
        return ResponseEntity.status(HttpStatus.OK).body(relevantPosts);
    }
}
