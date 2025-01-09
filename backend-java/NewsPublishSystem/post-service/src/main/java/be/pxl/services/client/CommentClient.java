package be.pxl.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "comment-service")
public interface CommentClient {

    @DeleteMapping("/api/comment/post/{postId}")
    boolean deleteCommentsForPost(@PathVariable Long postId);
}
