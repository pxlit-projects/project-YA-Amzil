package be.pxl.services.services;

import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;

import java.util.List;

public interface IPostService {
    void createPost(PostRequest postRequest);
    PostResponse updatePost(Long postId, PostRequest postRequest);
    PostResponse publishPost(Long postId);
    List<PostResponse> getAllPublishedPosts();
    List<PostResponse> getRelevantPosts(String filter);
}
