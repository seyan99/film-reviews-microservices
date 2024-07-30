package com.seyan.comment.filmlist;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "list-service",
        url = "${application.config.list-url}"
)
public interface FilmListClient {

}
