package com.seyan.user.user.dto;


import com.seyan.reviewmonolith.user.User;
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
public class UserMapper {
    public User mapUserCreationDTOToUser(UserCreationDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    public User mapUserUpdateDTOToUser(UserUpdateDTO source, User destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    public UserResponseDTO mapUserToUserResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    public PageableUserResponseDTO mapUsersPageToPageableUserResponseDTO(Page<User> usersPage) {
        List<User> listOfUsers = usersPage.getContent();
        List<UserResponseDTO> userResponseDTO = mapUserToUserResponseDTO(listOfUsers);

        return PageableUserResponseDTO.builder()
                .content(userResponseDTO)
                .pageNo(usersPage.getNumber())
                .pageSize(usersPage.getSize())
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .last(usersPage.isLast()).build();
    }

    public List<UserResponseDTO> mapUserToUserResponseDTO(List<User> users) {
        if (users == null) {
            return null;
        }

        List<UserResponseDTO> list = users.stream()
                .map(this::mapUserToUserResponseDTO)
                .toList();
        return list;
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
