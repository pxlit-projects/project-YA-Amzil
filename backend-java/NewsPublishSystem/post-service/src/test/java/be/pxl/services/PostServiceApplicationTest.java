package be.pxl.services;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.PostResponse;
import be.pxl.services.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PostServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class PostServiceApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Container
    private static MySQLContainer<?> sqlContainer = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();
    }

    @Test
    public void shouldCreatePost() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated());

        assertThat(postRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldUpdatePost() throws Exception {
        // First, create a post
        PostRequest postRequest = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        MvcResult result = mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long postId = postRepository.findAll().get(0).getId();

        // Now, update the post
        PostRequest updatedRequest = PostRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .author("Updated Author")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PUBLISHED)
                .build();

        MvcResult updateResult = mockMvc.perform(put("/api/post/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isAccepted())
                .andReturn();

        PostResponse updatedPost = objectMapper.readValue(updateResult.getResponse().getContentAsString(), PostResponse.class);
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getContent()).isEqualTo("Updated Content");
        assertThat(updatedPost.getAuthor()).isEqualTo("Updated Author");
        assertThat(updatedPost.getStatus()).isEqualTo(PostStatus.PUBLISHED);
    }

    @Test
    public void shouldGetPost() throws Exception {
        // First, create a post
        PostRequest postRequest = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        MvcResult result = mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long postId = postRepository.findAll().get(0).getId();

        // Now, fetch the post
        MvcResult getResult = mockMvc.perform(get("/api/post/{postId}", postId))
                .andExpect(status().isOk())
                .andReturn();

        PostResponse fetchedPost = objectMapper.readValue(getResult.getResponse().getContentAsString(), PostResponse.class);
        assertThat(fetchedPost.getTitle()).isEqualTo("Title 1");
        assertThat(fetchedPost.getContent()).isEqualTo("Content 1");
        assertThat(fetchedPost.getAuthor()).isEqualTo("Author 1");
    }

    @Test
    public void shouldGetAllPosts() throws Exception {
        // Create multiple posts
        PostRequest postRequest1 = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        PostRequest postRequest2 = PostRequest.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PENDING)
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest2)))
                .andExpect(status().isCreated());

        // Fetch all posts
        MvcResult getResult = mockMvc.perform(get("/api/post"))
                .andExpect(status().isOk())
                .andReturn();

        List<PostResponse> posts = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class));
        assertThat(posts).hasSize(2);
    }

//    @Test
//    public void shouldDeletePost() throws Exception {
//        // First, create a post
//        PostRequest postRequest = PostRequest.builder()
//                .title("Title 1")
//                .content("Content 1")
//                .author("Author 1")
//                .createAt(LocalDateTime.now())
//                .updateAt(LocalDateTime.now())
//                .status(PostStatus.DRAFT)
//                .build();
//
//        MvcResult result = mockMvc.perform(post("/api/post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postRequest)))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        Long postId = postRepository.findAll().get(0).getId();
//
//        // Now, delete the post
//        mockMvc.perform(delete("/api/post/{postId}", postId))
//                .andExpect(status().isNoContent());
//
//        assertThat(postRepository.findAll()).isEmpty();
//    }

    @Test
    public void shouldUpdatePostStatus() throws Exception {
        // First, create a post
        PostRequest postRequest = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        MvcResult result = mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long postId = postRepository.findAll().get(0).getId();

        // Now, update the post status
        mockMvc.perform(patch("/api/post/{postId}/status", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PostStatus.PUBLISHED.name()))
                .andExpect(status().isAccepted());


        Post updatedPost = postRepository.findById(postId).get();
        assertThat(updatedPost.getStatus()).isEqualTo(PostStatus.PUBLISHED);
    }

    @Test
    public void shouldGetAllDraftPosts() throws Exception {
        // Create multiple posts with different statuses
        PostRequest postRequest1 = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        PostRequest postRequest2 = PostRequest.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PENDING)
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest2)))
                .andExpect(status().isCreated());

        // Fetch all draft posts
        MvcResult getResult = mockMvc.perform(get("/api/post/draft"))
                .andExpect(status().isOk())
                .andReturn();

        List<PostResponse> posts = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class));
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getStatus()).isEqualTo(PostStatus.DRAFT);
    }

    @Test
    public void shouldGetAllPendingPosts() throws Exception {
        // Create multiple posts with different statuses
        PostRequest postRequest1 = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        PostRequest postRequest2 = PostRequest.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PENDING)
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest2)))
                .andExpect(status().isCreated());

        // Fetch all pending posts
        MvcResult getResult = mockMvc.perform(get("/api/post/pending"))
                .andExpect(status().isOk())
                .andReturn();

        List<PostResponse> posts = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class));
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getStatus()).isEqualTo(PostStatus.PENDING);
    }

    @Test
    public void shouldGetAllPublishedPosts() throws Exception {
        // Create multiple posts with different statuses
        PostRequest postRequest1 = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PUBLISHED)
                .build();

        PostRequest postRequest2 = PostRequest.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PENDING)
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest2)))
                .andExpect(status().isCreated());

        // Fetch all published posts
        MvcResult getResult = mockMvc.perform(get("/api/post/published"))
                .andExpect(status().isOk())
                .andReturn();

        List<PostResponse> posts = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class));
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getStatus()).isEqualTo(PostStatus.PUBLISHED);
    }

    @Test
    public void shouldGetAllDraftAndPendingPosts() throws Exception {
        // Create multiple posts with different statuses
        PostRequest postRequest1 = PostRequest.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.DRAFT)
                .build();

        PostRequest postRequest2 = PostRequest.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PENDING)
                .build();

        PostRequest postRequest3 = PostRequest.builder()
                .title("Title 3")
                .content("Content 3")
                .author("Author 3")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(PostStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest3)))
                .andExpect(status().isCreated());

        // Fetch all draft and pending posts
        MvcResult getResult = mockMvc.perform(get("/api/post/draft-pending"))
                .andExpect(status().isOk())
                .andReturn();

        List<PostResponse> posts = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class));
        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getStatus()).isIn(PostStatus.DRAFT, PostStatus.PENDING);
        assertThat(posts.get(1).getStatus()).isIn(PostStatus.DRAFT, PostStatus.PENDING);
    }
}


