package be.pxl.services.repository;

import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByPostId(Long postId);
    List<Review> findByStatus(ReviewStatus status);
}
