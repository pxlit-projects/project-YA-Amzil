package be.pxl.services.services;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.exceptions.PostNotFoundException;
import be.pxl.services.exceptions.PostPublishException;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{

    private final PostRepository postRepository;

    @Override
    public void createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .category(postRequest.getCategory())
                .status(PostStatus.DRAFT)
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
    }

    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id [" + postId + "]"));
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setLastUpdateDate(LocalDateTime.now());
        postRepository.save(post);
        return mapToResponse(post);
    }

    @Override
    public PostResponse publishPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id [" + postId + "]"));
        if(post.getStatus() == PostStatus.DRAFT){
            post.setStatus(PostStatus.PUBLISHED);
            post.setLastUpdateDate(LocalDateTime.now());
            postRepository.save(post);
        } else {
            throw new PostPublishException("Post with id [" + postId + "] is already published. Only draft posts can be published.");
        }
        return mapToResponse(post);
    }

    @Override
    public List<PostResponse> getAllPublishedPosts() {
        return postRepository.findByStatus(PostStatus.PUBLISHED).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getRelevantPosts(String filter) {
        return postRepository.findByContentContainingOrCategoryOrAuthor(filter, filter, filter).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .status(String.valueOf(post.getStatus()))
                .category(post.getCategory())
                .creationDate(post.getCreationDate())
                .lastUpdateDate(post.getLastUpdateDate())
                .build();
    }
}
