package com.seyan.user.user;

import com.seyan.user.dto.*;
import com.seyan.user.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> createUser(@RequestBody @Valid UserCreationDTO dto) {
        User user = userService.createUser(dto);
        UserResponseDTO response = userMapper.mapUserToUserResponseDTO(user);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("User has been successfully created")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> updateUser(@RequestBody @Valid UserUpdateDTO dto, @PathVariable("id") Long id) {
        User user = userService.updateUser(dto, id);
        UserResponseDTO response = userMapper.mapUserToUserResponseDTO(user);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("User has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("User has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> userDetails(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        UserResponseDTO response = userMapper.mapUserToUserResponseDTO(user);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("User details")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<User> allUsers = userService.getAllUsers(page, size);
        PageableUserResponseDTO response = userMapper.mapUsersPageToPageableUserResponseDTO(allUsers);
        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List of all users")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getFollowingUsers(
            @PathVariable("id") Long userId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<User> allUsers = userService.getFollowingUsers(userId, page, size);
        PageableUserResponseDTO response = userMapper.mapUsersPageToPageableUserResponseDTO(allUsers);
        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Followings of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getFollowersUsers(
            @PathVariable("id") Long userId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<User> allUsers = userService.getFollowersUsers(userId, page, size);
        PageableUserResponseDTO response = userMapper.mapUsersPageToPageableUserResponseDTO(allUsers);
        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Followers of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
