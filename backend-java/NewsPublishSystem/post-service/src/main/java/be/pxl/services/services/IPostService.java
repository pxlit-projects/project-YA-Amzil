package be.pxl.services.services;

import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;

import java.util.List;

public interface IPostService {
    // Method to create a post
    void createPost(PostRequest postRequest);

    // Method to get all posts
    List<PostResponse> getAllPosts();

    // Method to update a post
    PostResponse updatePost(Long postId, PostRequest postRequest);

    // Method to publish a post
    PostResponse publishPost(Long postId);

    // Method to get all published posts
    List<PostResponse> getAllPublishedPosts();

    List<PostResponse> getAllDraftAndPendingPosts();

    // Method to get relevant posts
    // List<PostResponse> getRelevantPosts(String content, String category, String author);
}
