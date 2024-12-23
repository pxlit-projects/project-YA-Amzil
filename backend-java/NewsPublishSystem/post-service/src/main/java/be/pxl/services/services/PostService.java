package be.pxl.services.services;

//import be.pxl.services.client.NotificationClient;
//import be.pxl.services.domain.NotificationRequest;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.exceptions.PostNotFoundException;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository postRepository;
//    private final NotificationClient notificationClient;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    // US1
    @Override
    public void createPost(PostRequest postRequest) {
        log.info("Adding new post: {}", postRequest.getTitle());

        // check if the title already exists
//        if (postRepository.existsByTitle(postRequest.getTitle())) {
//            log.error("Post with title [{}] already exists", postRequest.getTitle());
//            throw new IllegalArgumentException("Post with title [" + postRequest.getTitle() + "] already exists");
//        }

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .status((postRequest.getStatus() != null ? postRequest.getStatus() : PostStatus.DRAFT))
                .createAt(postRequest.getCreateAt())
                .updateAt(postRequest.getUpdateAt())
                .build();
        postRepository.save(post);
        log.info("Post added successfully: {}", post.getTitle());

//        NotificationRequest notificationRequest = NotificationRequest.builder()
//                .message("Post created")
//                .sender(post.getAuthor())
//                .build();
//        notificationClient.sendNotification(notificationRequest);
    }

    // US2 - US3
    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest) {
        log.info("Updating post with id: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id [" + postId + "]"));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(postRequest.getAuthor());
        post.setStatus(postRequest.getStatus());
        post.setUpdateAt(postRequest.getUpdateAt());
        postRepository.save(post);
        log.info("Post updated successfully: {}", post.getTitle());
        return mapToResponse(post);
    }

    @Override
    public PostResponse getPost(Long postId) {
        log.info("Getting post with id: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id [" + postId + "]"));
        return mapToResponse(post);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        log.info("Getting all posts");
        return postRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<PostResponse> getAllDraftPosts() {
        log.info("Getting all draft posts");
        return postRepository.findByStatus(PostStatus.DRAFT).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // US4
    @Override
    public List<PostResponse> getAllPublishedPosts() {
        log.info("Getting all published posts");
        return postRepository.findByStatus(PostStatus.PUBLISHED).stream()
        .map(this::mapToResponse)
        .toList();
    }

    // US4
    @Override
    public List<PostResponse> getAllDraftAndPendingPosts() {
        log.info("Getting all draft and pending posts");
        return postRepository.findByStatusIn(List.of(PostStatus.DRAFT, PostStatus.PENDING)).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .status(post.getStatus())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .build();
    }
}
