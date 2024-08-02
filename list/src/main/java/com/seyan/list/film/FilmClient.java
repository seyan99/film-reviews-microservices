package com.seyan.list.film;


import com.seyan.list.responsewrapper.CustomResponseWrapper;
import com.seyan.list.dto.FilmInFilmListResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "film-service",
        url = "${application.config.film-url}"
)
public interface FilmClient {

    @GetMapping("/get-by-id-list")
    public CustomResponseWrapper<List<FilmInFilmListResponseDTO>> getFilmsByIdList(@RequestBody List<Long> filmIds);
}
