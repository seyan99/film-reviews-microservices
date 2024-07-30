package com.seyan.comment.dto;


import com.seyan.reviewmonolith.comment.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CommentMapper {
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

    /*public PageableUserResponseDTO mapUsersPageToPageableUserResponseDTO(Page<User> usersPage) {
        List<User> listOfUsers = usersPage.getContent();
        List<UserProfileResponseDTO> userProfileResponseDTO = mapUserToUserProfileResponseDTO(listOfUsers);

        return PageableUserResponseDTO.builder()
                .content(userProfileResponseDTO)
                .pageNo(usersPage.getNumber())
                .pageSize(usersPage.getSize())
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .last(usersPage.isLast()).build();
    }*/

    /*public List<UserProfileResponseDTO> mapUserToUserProfileResponseDTO(List<User> users) {
        if (users == null) {
            return null;
        }

        List<UserProfileResponseDTO> list = users.stream()
                .map(this::mapUserToUserProfileResponseDTO)
                .toList();
        return list;
    }*/

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
