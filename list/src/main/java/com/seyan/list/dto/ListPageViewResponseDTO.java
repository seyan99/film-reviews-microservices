package com.seyan.list.dto;

import com.seyan.list.external.comment.PageableCommentResponseDTO;
import com.seyan.list.external.film.PageableFilmPreviewResponseDTO;

public record ListPageViewResponseDTO(
        ListResponseDTO list,
        PageableFilmPreviewResponseDTO films,
        PageableCommentResponseDTO comments
) {
}
