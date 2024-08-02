package com.seyan.comment.filmlist;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "list-service",
        url = "${application.config.list-url}"
)
public interface FilmListClient {
    @PutMapping("/add-comment")
    void addListComment(@RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId);

    @DeleteMapping("/delete-comment")
    void deleteListComment(@RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId);
}
