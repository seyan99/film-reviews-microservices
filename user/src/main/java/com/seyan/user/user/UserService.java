package com.seyan.user.user;

import com.seyan.user.dto.PageableUserResponseDTO;
import com.seyan.user.dto.UserCreationDTO;
import com.seyan.user.dto.UserMapper;
import com.seyan.user.dto.UserUpdateDTO;
import com.seyan.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(UserCreationDTO dto) {
        User user = userMapper.mapUserCreationDTOToUser(dto);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided ID: %s", id)
        ));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided username: %s", username)
        ));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //todo make pageable
    public List<User> getFollowingUsers(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided ID: %s", id)
        ));

        return userRepository.findAllById(user.followingUsers);
    }

    //todo make pageable
    public List<User> getFollowersUsers(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided ID: %s", id)
        ));

        return userRepository.findAllById(user.followersUsers);
    }

    public User updateUser(UserUpdateDTO dto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("Cannot update user:: No user found with the provided ID: %s", id)
        ));
        User mapped = userMapper.mapUserUpdateDTOToUser(dto, user);
        return userRepository.save(mapped);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("Cannot delete user:: No user found with the provided ID: %s", id)));
        userRepository.deleteById(id);
    }

    public PageableUserResponseDTO getAllUsersPageable(int pageNo, int pageSize) {
        return null;
    }
}
