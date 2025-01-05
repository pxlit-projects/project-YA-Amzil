package be.pxl.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.exceptions.CommentNotFoundException;
import be.pxl.services.repository.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CommentServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class CommentServiceApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

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
        commentRepository.deleteAll();
    }

    @Test
    public void shouldCreateComment() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .postId(1L)
                .author("Author 1")
                .content("Content 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());

        assertThat(commentRepository.findAll()).hasSize(1);

        Comment createdComment = commentRepository.findAll().get(0);
        assertEquals("Content 1", createdComment.getContent());
        assertEquals("Author 1", createdComment.getAuthor());
    }

    @Test
    public void shouldUpdateComment() throws Exception {
        // First, create a comment
        CommentRequest commentRequest = CommentRequest.builder()
                .postId(1L)
                .author("Author 1")
                .content("Content 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        MvcResult result = mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long commentId = commentRepository.findAll().get(0).getId();

        // Now, update the comment
        CommentRequest updatedRequest = CommentRequest.builder()
                .content("Updated Content")
                .updateAt(LocalDateTime.now())
                .build();

        MvcResult updateResult = mockMvc.perform(put("/api/comment/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isAccepted())
                .andReturn();

        CommentResponse updatedComment = objectMapper.readValue(updateResult.getResponse().getContentAsString(), CommentResponse.class);
        assertEquals("Updated Content", updatedComment.getContent());
    }

//    @Test
//    public void shouldGetComment() throws Exception {
//        // First, create a comment
//        CommentRequest commentRequest = CommentRequest.builder()
//                .postId(1L)
//                .author("Author 1")
//                .content("Content 1")
//                .createAt(LocalDateTime.now())
//                .updateAt(LocalDateTime.now())
//                .build();
//
//        MvcResult result = mockMvc.perform(post("/api/comment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(commentRequest)))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        Long commentId = commentRepository.findAll().get(0).getId();
//
//        // Now, fetch the comment
//        MvcResult getResult = mockMvc.perform(get("/api/comment/{commentId}", commentId))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CommentResponse fetchedComment = objectMapper.readValue(getResult.getResponse().getContentAsString(), CommentResponse.class);
//        assertEquals("Content 1", fetchedComment.getContent());
//        assertEquals("Author 1", fetchedComment.getAuthor());
//
//        Optional<Comment> optionalComment = commentRepository.findById(commentId);
//        assertTrue(optionalComment.isPresent());
//        Comment comment = optionalComment.get();
//        assertEquals("Content 1", comment.getContent());
//        assertEquals("Author 1", comment.getAuthor());
//    }

    @Test
    public void shouldGetAllComments() throws Exception {
        // Create multiple comments
        CommentRequest commentRequest1 = CommentRequest.builder()
                .postId(1L)
                .author("Author 1")
                .content("Content 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        CommentRequest commentRequest2 = CommentRequest.builder()
                .postId(1L)
                .author("Author 2")
                .content("Content 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest2)))
                .andExpect(status().isCreated());

        // Fetch all comments
        MvcResult getResult = mockMvc.perform(get("/api/comment"))
                .andExpect(status().isOk())
                .andReturn();

        List<CommentResponse> comments = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, CommentResponse.class));
        assertThat(comments).hasSize(2);
        assertEquals("Content 1", comments.get(0).getContent());
        assertEquals("Content 2", comments.get(1).getContent());
    }

    @Test
    public void shouldGetCommentsForPost() throws Exception {
        // Create multiple comments for different posts
        CommentRequest commentRequest1 = CommentRequest.builder()
                .postId(1L)
                .author("Author 1")
                .content("Content 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        CommentRequest commentRequest2 = CommentRequest.builder()
                .postId(2L)
                .author("Author 2")
                .content("Content 2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest2)))
                .andExpect(status().isCreated());

        // Fetch comments for a specific post
        MvcResult getResult = mockMvc.perform(get("/api/comment/post/{postId}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        List<CommentResponse> comments = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, CommentResponse.class));
        assertThat(comments).hasSize(1);
        assertEquals("Content 1", comments.get(0).getContent());
    }

    @Test
    public void shouldDeleteComment() throws Exception {
        // First, create a comment
        CommentRequest commentRequest = CommentRequest.builder()
                .postId(1L)
                .author("Author 1")
                .content("Content 1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());

        Long commentId = commentRepository.findAll().get(0).getId();

        // Now, delete the comment
        mockMvc.perform(delete("/api/comment/{commentId}", commentId))
                .andExpect(status().isNoContent());

        assertTrue(commentRepository.findById(commentId).isEmpty());
    }

    @Test
    public void shouldThrowCommentNotFoundException() throws Exception {
        // Attempt to update a comment with a non-existent ID
        Long nonExistentCommentId = 999L;

        CommentRequest updatedRequest = CommentRequest.builder()
                .content("Updated Content")
                .updateAt(LocalDateTime.now())
                .build();

        mockMvc.perform(put("/api/comment/{commentId}", nonExistentCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isNotFound());

        // Verify that the exception message is as expected
        Exception exception = assertThrows(CommentNotFoundException.class, () -> {
            commentRepository.findById(nonExistentCommentId)
                    .orElseThrow(() -> new CommentNotFoundException("Comment not found with id [" + nonExistentCommentId + "]"));
        });

        String expectedMessage = "Comment not found with id [" + nonExistentCommentId + "]";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

//    @Test
//    public void shouldVerifyCommentDeletion() throws Exception {
//        // First, create a comment
//        CommentRequest commentRequest = CommentRequest.builder()
//                .postId(1L)
//                .author("Author 1")
//                .content("Content 1")
//                .createAt(LocalDateTime.now())
//                .updateAt(LocalDateTime.now())
//                .build();
//
//        mockMvc.perform(post("/api/comment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(commentRequest)))
//                .andExpect(status().isCreated());
//
//        Long commentId = commentRepository.findAll().get(0).getId();
//
//        // Now, delete the comment
//        mockMvc.perform(delete("/api/comment/{commentId}", commentId))
//                .andExpect(status().isNoContent());
//
//        verify(commentRepository, times(1)).deleteById(commentId);
//    }
}


