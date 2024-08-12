package com.seyan.list.external.film;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageableFilmPreviewResponseDTO {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private List<FilmPreviewResponseDTO> content;
}
