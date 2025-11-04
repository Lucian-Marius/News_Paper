package it.aulab.news_paper.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    private Long id;
    @NotEmpty (message = "First name cannot be empty")
    private String firstName;
    @NotEmpty (message = "Last name cannot be empty")
    private String lastName;
    @NotEmpty (message = "Email cannot be empty")
    @Email
    private String email;
    @NotEmpty (message = "Password cannot be empty")
    private String password;
}