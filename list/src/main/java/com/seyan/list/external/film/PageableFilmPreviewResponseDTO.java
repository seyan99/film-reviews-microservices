package com.seyan.list.external.film;

import java.util.List;

public class PageableFilmPreviewResponseDTO {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private List<FilmPreviewResponseDTO> content;
}
