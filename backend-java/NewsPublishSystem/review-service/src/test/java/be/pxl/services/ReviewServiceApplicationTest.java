package be.pxl.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.exceptions.ReviewNotFoundException;
import be.pxl.services.messaging.ReviewMessageProducer;
import be.pxl.services.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ReviewServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class ReviewServiceApplicationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationClient notificationClient;

    @MockBean
    private ReviewMessageProducer reviewMessageProducer;

    @Autowired
    private ObjectMapper objectMapper;

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
        reviewRepository.deleteAll();
    }

    @Test
    public void shouldApproveReview() throws Exception {
        Long postId = 1L;

        Review review = Review.builder()
                .postId(postId)
                .status(ReviewStatus.PENDING)
                .reviewer("Reviewer")
                .comment("Pending review")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);


        ReviewRequest reviewRequest = ReviewRequest.builder()
                .reviewer("Reviewer")
                .comment("Approved review")
                .reviewedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(notificationClient).sendNotification(Mockito.any());

        MvcResult result = mockMvc.perform(put("/api/review/approve/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isAccepted())
                .andReturn();

        ReviewResponse approvedReview = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewResponse.class);
        assertThat(approvedReview).isNotNull();
        assertThat(approvedReview.getStatus()).isEqualTo(ReviewStatus.APPROVED);

        Optional<Review> updatedReview = reviewRepository.findById(review.getId());
        assertTrue(updatedReview.isPresent());
        assertEquals(ReviewStatus.APPROVED, updatedReview.get().getStatus());
    }

    @Test
    public void shouldRejectReview() throws Exception {
        Long postId = 1L;

        Review review = Review.builder()
                .postId(postId)
                .status(ReviewStatus.PENDING)
                .reviewer("Reviewer")
                .comment("Pending review")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .reviewer("Reviewer")
                .comment("Rejected review")
                .reviewedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(notificationClient).sendNotification(Mockito.any());

        MvcResult result = mockMvc.perform(put("/api/review/reject/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isAccepted())
                .andReturn();

        ReviewResponse rejectedReview = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewResponse.class);
        assertThat(rejectedReview).isNotNull();
        assertThat(rejectedReview.getStatus()).isEqualTo(ReviewStatus.REJECTED);

        Optional<Review> updatedReview = reviewRepository.findById(review.getId());
        assertTrue(updatedReview.isPresent());
        assertEquals(ReviewStatus.REJECTED, updatedReview.get().getStatus());
    }

    @Test
    public void shouldPublishPost() throws Exception {

        Review review = Review.builder()
                .postId(1L)
                .status(ReviewStatus.APPROVED)
                .reviewer("Reviewer")
                .comment("Ready to publish")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.saveAndFlush(review);

        Mockito.doNothing().when(reviewMessageProducer).sendMessage(Mockito.any());

        MvcResult result = mockMvc.perform(put("/api/review/publish/{postId}", review.getId()))
                .andExpect(status().isAccepted())
                .andReturn();

        ReviewResponse publishedReview = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewResponse.class);
        assertThat(publishedReview).isNotNull();
        assertThat(publishedReview.getStatus()).isEqualTo(ReviewStatus.PUBLISHED);

        Optional<Review> updatedReview = reviewRepository.findById(review.getId());
        assertTrue(updatedReview.isPresent());
        assertEquals(ReviewStatus.PUBLISHED, updatedReview.get().getStatus());
    }

    @Test
    public void shouldRevisePost() throws Exception {

        Review review = Review.builder()
                .postId(1L)
                .status(ReviewStatus.REJECTED)
                .reviewer("Reviewer")
                .comment("Needs revision")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.saveAndFlush(review);

        Mockito.doNothing().when(reviewMessageProducer).sendMessage(Mockito.any());

        MvcResult result = mockMvc.perform(put("/api/review/revise/{postId}", review.getId()))
                .andExpect(status().isAccepted())
                .andReturn();

        ReviewResponse revisedReview = objectMapper.readValue(result.getResponse().getContentAsString(), ReviewResponse.class);
        assertThat(revisedReview).isNotNull();
        assertThat(revisedReview.getStatus()).isEqualTo(ReviewStatus.PENDING);

        Optional<Review> updatedReview = reviewRepository.findById(review.getId());
        assertTrue(updatedReview.isPresent());
        assertEquals(ReviewStatus.PENDING, updatedReview.get().getStatus());
    }

    @Test
    public void shouldGetAllReviews() throws Exception {
        Review review1 = Review.builder()
                .id(1L)
                .postId(1L)
                .status(ReviewStatus.APPROVED)
                .reviewer("Reviewer 1")
                .comment("Approved review")
                .reviewedAt(LocalDateTime.now())
                .build();

        Review review2 = Review.builder()
                .id(2L)
                .postId(2L)
                .status(ReviewStatus.REJECTED)
                .reviewer("Reviewer 2")
                .comment("Rejected review")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        MvcResult result = mockMvc.perform(get("/api/review"))
                .andExpect(status().isOk())
                .andReturn();

        List<ReviewResponse> reviews = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, ReviewResponse.class));
        assertThat(reviews).hasSize(2);
    }

    @Test
    public void shouldGetApprovedReviews() throws Exception {
        Review review1 = Review.builder()
                .id(1L)
                .postId(1L)
                .status(ReviewStatus.APPROVED)
                .reviewer("Reviewer 1")
                .comment("Approved review")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review1);

        MvcResult result = mockMvc.perform(get("/api/review/approved"))
                .andExpect(status().isOk())
                .andReturn();

        List<ReviewResponse> reviews = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, ReviewResponse.class));
        assertThat(reviews).hasSize(1);
    }

    @Test
    public void shouldGetRejectedReviews() throws Exception {
        Review review1 = Review.builder()
                .id(1L)
                .postId(1L)
                .status(ReviewStatus.REJECTED)
                .reviewer("Reviewer 1")
                .comment("Rejected review")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review1);

        MvcResult result = mockMvc.perform(get("/api/review/rejected"))
                .andExpect(status().isOk())
                .andReturn();

        List<ReviewResponse> reviews = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, ReviewResponse.class));
        assertThat(reviews).hasSize(1);
    }

    @Test
    public void shouldDeleteComment() throws Exception {

        Review review = Review.builder()
                .postId(1L)
                .status(ReviewStatus.APPROVED)
                .reviewer("Reviewer")
                .comment("Ready to publish")
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewRepository.saveAndFlush(review);

        mockMvc.perform(put("/api/review/publish/{postId}", review.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isAccepted());

        Long commentId = reviewRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/review/{reviewId}", commentId))
                .andExpect(status().isNoContent());

        assertTrue(reviewRepository.findById(commentId).isEmpty());
    }



    @Test
    public void shouldThrowReviewNotFoundException() throws Exception {
        Long nonExistentReviewId = 999L;

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .reviewer("Reviewer")
                .comment("Rejected review")
                .reviewedAt(LocalDateTime.now())
                .build();

        mockMvc.perform(put("/api/review/publish/{postId}", nonExistentReviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isNotFound());

        Exception exception = assertThrows(ReviewNotFoundException.class, () -> {
            reviewRepository.findById(nonExistentReviewId)
                    .orElseThrow(() -> {
                        throw new ReviewNotFoundException("Review not found for post id [" + nonExistentReviewId + "]");
                    });
        });

        String expectedMessage = "Review not found for post id [" + nonExistentReviewId + "]";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
