package be.pxl.services.services;

import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;

import java.util.List;

public interface IPostService {
    // Method to create a post
    void createPost(PostRequest postRequest);

    // Method to update a post
    PostResponse updatePost(Long postId, PostRequest postRequest);;

    // Method to get all posts
    List<PostResponse> getAllPosts();

    // Method to get all published posts
    List<PostResponse> getAllPublishedPosts();

    // Method to get all draft and pending posts
    List<PostResponse> getAllDraftAndPendingPosts();
}
