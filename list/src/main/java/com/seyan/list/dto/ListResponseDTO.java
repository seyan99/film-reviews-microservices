package com.seyan.list.dto;

import com.seyan.list.external.film.FilmPreviewResponseDTO;
import com.seyan.list.list.Privacy;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ListResponseDTO {
    Long id;
    Long userId;
    String username;
    String title;
    String description;
    Privacy privacy;
    int likeCount;
    int commentCount;
    List<FilmPreviewResponseDTO> firstFiveFilms;
    int filmsCount;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
