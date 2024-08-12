package com.seyan.list.dto;

import com.seyan.list.list.Privacy;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
    //List<CommentResponseDTO> comments;
    int commentCount;
    //List<FilmInFilmListResponseDTO> films;
    int filmsCount;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
