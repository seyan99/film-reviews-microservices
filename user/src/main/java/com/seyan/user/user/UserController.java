package com.seyan.user.user;

import com.seyan.user.dto.UserCreationDTO;
import com.seyan.user.dto.UserMapper;
import com.seyan.user.dto.UserResponseDTO;
import com.seyan.user.dto.UserUpdateDTO;
import com.seyan.user.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<User> createUser() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


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

    /*@PatchMapping("/{id}/update-phone-number")
    public ResponseEntity<CustomResponseWrapper<UserResponseDTO>> updatePhoneNumber(@RequestParam String phoneNumber, @PathVariable("id") Long userId) {
        User updated = userService.updatePhoneNumber(phoneNumber, userId);
        UserResponseDTO response = userMapper.mapUserToUserResponseDTO(updated);
        CustomResponseWrapper<UserResponseDTO> wrapper = CustomResponseWrapper.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("User's phone number has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }*/

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

    /*@GetMapping("/search-by-birth-date")
    public ResponseEntity<CustomResponseWrapper<List<UserResponseDTO>>> searchByBirthDate(@RequestParam LocalDate from, @RequestParam  LocalDate to) {
        List<User> users = userService.searchUsersByBirthDate(from, to);
        List<UserResponseDTO> response = userMapper.mapUserToUserResponseDTO(users);
        CustomResponseWrapper<List<UserResponseDTO>> wrapper = CustomResponseWrapper.<List<UserResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of users by birth date from " + from + " to " + to)
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }*/

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
    public ResponseEntity<CustomResponseWrapper<List<UserResponseDTO>>> getAll() {
        List<User> allUsers = userService.getAllUsers();
        List<UserResponseDTO> response = userMapper.mapUserToUserResponseDTO(allUsers);
        CustomResponseWrapper<List<UserResponseDTO>> wrapper = CustomResponseWrapper.<List<UserResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of all users")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    /*@GetMapping("/pageable")
    public ResponseEntity<CustomResponseWrapper<PageableUserResponseDTO>> getPageable(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PageableUserResponseDTO allUsersPageable = userService.getAllUsersPageable(pageNo, pageSize);

        CustomResponseWrapper<PageableUserResponseDTO> wrapper = CustomResponseWrapper.<PageableUserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List of all users")
                .data(allUsersPageable)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }*/

    /*api/username
    api/username/films(watched)
    api/username/films/diary
    api/username/films/reviews

    api/username/watchlist
    api/username/lists

    api/username/likes/films
    api/username/likes/reviews
    api/username/likes/lists

    api/username/following
    api/username/followers
    api/username/blocked*/
}
