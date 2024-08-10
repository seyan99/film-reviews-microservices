package com.seyan.film.dto.film;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public class PageableFilmPreviewResponseDTO {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private List<FilmPreviewResponseDTO> content;
}
