package com.seyan.user.user;

import com.seyan.user.dto.UserCreationDTO;
import com.seyan.user.dto.UserMapper;
import com.seyan.user.dto.UserUpdateDTO;
import com.seyan.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<User> getAllUsers(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 25);
        return userRepository.findAll(pageable);
    }

    public List<User> getFollowingUsers(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided ID: %s", id)
        ));

        return userRepository.findAllById(user.followingUsers);
    }

    public Page<User> getFollowingUsers(String username, int pageNo) {
        User user = getUserByUsername(username);

        Pageable pageable = PageRequest.of(pageNo - 1, 25);
        return userRepository.findAllByIdIn(user.followingUsers, pageable);
    }

    public List<User> getFollowersUsers(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided ID: %s", id)
        ));

        return userRepository.findAllById(user.followersUsers);
    }

    public Page<User> getFollowersUsers(String username, int pageNo) {
        User user = getUserByUsername(username);

        Pageable pageable = PageRequest.of(pageNo - 1, 25);
        return userRepository.findAllByIdIn(user.followersUsers, pageable);
    }

    public Page<User> getFollowersUsers(Long id, int pageNo, int pageSize) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("No user found with the provided ID: %s", id)
        ));

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return userRepository.findAllByIdIn(user.followersUsers, pageable);
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
}
