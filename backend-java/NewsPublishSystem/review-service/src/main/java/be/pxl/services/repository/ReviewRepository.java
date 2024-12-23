package be.pxl.services.repository;

import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
//    List<Review> findByPostId(Long postId);
//    List<Review> findByReviewerId(Long reviewerId);
//    List<Review> findByStatus(ReviewStatus status);
}
