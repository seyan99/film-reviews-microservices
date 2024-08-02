package com.seyan.list.comment;


import com.seyan.list.dto.FilmInFilmListResponseDTO;
import com.seyan.list.responsewrapper.CustomResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "comment-service",
        url = "${application.config.comment-url}"
)
public interface CommentClient {

    @GetMapping("/latest-by-post")
    CustomResponseWrapper<List<CommentResponseDTO>> getLatestByPost(@RequestParam("postId") Long postId, @RequestParam("postType") String postType);
}
