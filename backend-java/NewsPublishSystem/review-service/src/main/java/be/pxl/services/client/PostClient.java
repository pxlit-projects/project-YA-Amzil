package be.pxl.services.client;

import be.pxl.services.domain.dto.PostRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "post-service")
public interface PostClient {

    @GetMapping("/api/post/{postId}")
    PostRequest getPost(@PathVariable Long postId);

//    @PutMapping("/api/post/{postId}")
//    void updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest);
}


