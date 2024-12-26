package be.pxl.services.services;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;

import java.util.List;

public interface IPostService {
    // Method to create a post
    void createPost(PostRequest postRequest);

    // Method to update a post
    PostResponse updatePost(Long postId, PostRequest postRequest);

    // Method to update a post status
    PostResponse updatePostStatus(Long postId, PostStatus postStatus);

    //Method to get one post
    PostResponse getPost(Long postId);

    // Method to get all posts
    List<PostResponse> getAllPosts();

    //Method to get all draft posts
    List<PostResponse> getAllDraftPosts();

    // Method to get all pending posts
    List<PostResponse> getAllPendingPosts();

    // Method to get all published posts
    List<PostResponse> getAllPublishedPosts();

    // Method to get all draft and pending posts
    List<PostResponse> getAllDraftAndPendingPosts();
}
