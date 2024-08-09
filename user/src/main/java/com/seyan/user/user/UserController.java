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
import java.util.Optional;

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
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> updateUser(@RequestBody @Valid UserUpdateDTO dto, @PathVariable Long id) {
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
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("User has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> userDetails(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        UserResponseDTO response = userMapper.mapUserToUserResponseDTO(user);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("User details")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/", "/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getAll(@PathVariable Optional<Integer> pageNo) {
        Page<User> allUsers;
        if (pageNo.isPresent()) {
            allUsers = userService.getAllUsers(pageNo.get());
        } else {
            allUsers = userService.getAllUsers(1);
        }

        PageableUserResponseDTO response = userMapper.mapUsersPageToPageableUserResponseDTO(allUsers);
        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List of all users")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/{username}/following", "/{username}/following/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getFollowingUsers(
            @PathVariable("id") String username, @PathVariable Optional<Integer> pageNo) {

        Page<User> allUsers;
        if (pageNo.isPresent()) {
            allUsers = userService.getFollowingUsers(username, pageNo.get());
        } else {
            allUsers = userService.getFollowingUsers(username, 1);
        }

        PageableUserResponseDTO response = userMapper.mapUsersPageToPageableUserResponseDTO(allUsers);
        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Followings of user: %s", username))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/{username}/followers", "/{username}/followers/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getFollowersUsers(
            @PathVariable("id") String username, @PathVariable Optional<Integer> pageNo) {

        Page<User> allUsers;
        if (pageNo.isPresent()) {
            allUsers = userService.getFollowersUsers(username, pageNo.get());
        } else {
            allUsers = userService.getFollowersUsers(username, 1);
        }

        PageableUserResponseDTO response = userMapper.mapUsersPageToPageableUserResponseDTO(allUsers);
        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Followers of user: %s", username))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
