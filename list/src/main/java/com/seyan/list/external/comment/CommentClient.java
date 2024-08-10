package com.seyan.list.external.comment;


import com.seyan.list.responsewrapper.CustomResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(
        name = "comment-service",
        url = "${application.config.comment-url}"
)
public interface CommentClient {

    @GetMapping("/latest-by-post")
    CustomResponseWrapper<List<CommentResponseDTO>> getLatestByPost(@RequestParam("postId") Long postId, @RequestParam("postType") String postType);

    @GetMapping("/list/{listId}/page/{pageNo}")
    public CustomResponseWrapper<PageableCommentResponseDTO> getAllByList(
            @PathVariable Long listId, @PathVariable int pageNo);
}
