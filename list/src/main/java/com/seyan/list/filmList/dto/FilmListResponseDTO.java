package com.seyan.list.filmList.dto;

import com.seyan.reviewmonolith.filmList.Privacy;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilmListResponseDTO {
    Long id;
    Long userId;
    String title;
    String description;
    Privacy privacy;
    int likeCount;
    int commentCount;
    List<FilmInFilmListResponseDTO> films;
    Integer filmsCount;
}
