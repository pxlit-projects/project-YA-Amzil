package be.pxl.services.services;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.exceptions.PostNotFoundException;
import be.pxl.services.exceptions.PostPublishException;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);


    // US1: Als redacteur wil ik nieuwe artikelen kunnen aanmaken, zodat ik nieuws en updates kan delen met de organisatie.
    // This method allows a user (editor) to create a new post. The post is initially set as a draft.
    @Override
    public void createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle()) // Title of the post
                .content(postRequest.getContent()) // Content of the post
                .author(postRequest.getAuthor()) // Author of the post
                .category(postRequest.getCategory()) // Category of the post (e.g., News, Updates)
                .status(PostStatus.DRAFT) // Initial status is DRAFT
                .creationDate(LocalDateTime.now()) // Timestamp when the post was created
                .lastUpdateDate(LocalDateTime.now()) // Timestamp when the post was last updated
                .build();
        // Save the post to the repository
        postRepository.save(post);
    }

    // US2: Als redacteur wil ik artikelen kunnen opslaan als concept, zodat ik er later aan kan verderwerken of kan wachten op goedkeuring.
    // This method allows the editor to update an existing post's content, title, and other details.
    // If a post already exists, its last update timestamp will also be updated.
    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest) {
        // Fetch the post from the repository using its ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id [" + postId + "]"));

        // Update the post details with the new data from the PostRequest
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setLastUpdateDate(LocalDateTime.now()); // Update the last updated timestamp
        postRepository.save(post); // Save the updated post

        // Return the updated post as a response DTO
        return mapToResponse(post);
    }

    // US3: Als redacteur wil ik de inhoud van een artikel kunnen bewerken, zodat ik fouten kan corrigeren en inhoud kan bijwerken.
    // This method is used to publish a post that is in draft status. Once published, the post status changes to PUBLISHED.
    @Override
    public PostResponse publishPost(Long postId) {
        // Fetch the post by its ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id [" + postId + "]"));

        // Check if the post is in draft status before publishing it
        if (post.getStatus() == PostStatus.DRAFT) {
            post.setStatus(PostStatus.PUBLISHED); // Change status to PUBLISHED
            post.setLastUpdateDate(LocalDateTime.now()); // Update the last updated timestamp
            postRepository.save(post); // Save the post after publishing it
        } else {
            // Throw an exception if the post is already published
            throw new PostPublishException("Post with id [" + postId + "] is already published. Only draft posts can be published.");
        }

        // Return the published post as a response DTO
        return mapToResponse(post);
    }

    // US4: Als gebruiker wil ik een overzicht van gepubliceerde artikelen kunnen zien, zodat ik op de hoogte blijf van het laatste nieuws.
    // This method retrieves all the posts that have been published.
    @Override
    public List<PostResponse> getAllPublishedPosts() {
        // Fetch all posts that have a status of PUBLISHED
        return postRepository.findByStatus(PostStatus.PUBLISHED).stream()
                .map(this::mapToResponse) // Map each post to a PostResponse DTO
                .toList();
    }

    // US5: Als gebruiker wil ik een overzicht kunnen zien van alle relevante posts
    // This method retrieves posts that match a specific filter in the content, category, or author fields.
    // US6: Als gebruiker wil ik posts kunnen filteren op basis van inhoud, auteur & categorie
    // This method is used to filter posts based on content, category, or author. It can be used by both editors and users.
    @Override
    public List<PostResponse> getRelevantPosts(String filter) {
        // Fetch posts where the content, category, or author matches the filter string
        return postRepository.findByContentContainingOrCategoryOrAuthor(filter, filter, filter).stream()
                .map(this::mapToResponse) // Map each relevant post to a PostResponse DTO
                .toList();
    }


    private PostResponse mapToResponse(Post post) {
        // Helper method to convert a Post entity to a PostResponse DTO
        return PostResponse.builder()
                .id(post.getId()) // Post ID
                .title(post.getTitle()) // Post title
                .content(post.getContent()) // Post content
                .author(post.getAuthor()) // Author of the post
                .status(String.valueOf(post.getStatus())) // Post status (DRAFT, PUBLISHED)
                .category(post.getCategory()) // Category of the post
                .creationDate(post.getCreationDate()) // Date when the post was created
                .lastUpdateDate(post.getLastUpdateDate()) // Last updated timestamp
                .build();
    }
}
