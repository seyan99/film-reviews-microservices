package com.seyan.list.external.film;


import com.seyan.list.responsewrapper.CustomResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(
        name = "film-service",
        url = "${application.config.film-url}"
)
public interface FilmClient {

    @GetMapping("/get-by-id-list")
    public CustomResponseWrapper<List<FilmPreviewResponseDTO>> getFilmsByIdList(@RequestBody List<Long> filmIds);

    @GetMapping("/from-list/page/{pageNo}")
    public CustomResponseWrapper<PageableFilmPreviewResponseDTO> getFilmsFromList(
            @PathVariable(required = false) int pageNo, @RequestBody List<Long> filmIds);
}
