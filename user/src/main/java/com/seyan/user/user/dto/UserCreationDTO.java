package com.seyan.user.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {
    @NotNull(message = "Email should not be null")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be in format abc@mail.com", regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotNull(message = "Username should not be null")
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotNull(message = "Password should not be null")
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8)
    private String password;
}
