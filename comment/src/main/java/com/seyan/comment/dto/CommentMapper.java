package com.seyan.comment.dto;


import com.seyan.comment.comment.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CommentMapper {
    public PageableCommentResponseDTO mapCommentsPageToPageableCommentResponseDTO(Page<Comment> comments) {
        List<CommentResponseDTO> mapped = mapCommentToCommentResponseDTO(comments.getContent());

        return PageableCommentResponseDTO.builder()
                .content(mapped)
                .pageNo(comments.getNumber())
                .pageSize(comments.getSize())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    public Comment mapCommentCreationDTOToComment(CommentCreationDTO dto) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(dto, comment, getNullFieldNames(dto));
        return comment;
    }

    public Comment mapCommentUpdateDTOToComment(CommentUpdateDTO source, Comment destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    public CommentResponseDTO mapCommentToCommentResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUserId(),
                comment.getCommentDate(),
                comment.getIsEdited()
        );
    }

    public List<CommentResponseDTO> mapCommentToCommentResponseDTO(List<Comment> films) {
        if (films == null) {
            return null;
        }

        return films.stream()
                .map(this::mapCommentToCommentResponseDTO)
                .toList();
    }

    private String[] getNullFieldNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> fieldNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                fieldNames.add(pd.getName());
        }

        String[] result = new String[fieldNames.size()];
        return fieldNames.toArray(result);
    }
}
