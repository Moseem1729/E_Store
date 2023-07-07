package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    //@NotEmpty

    @Size(min = 3, max = 15, message = "Username must be min of 3 and max of 15 characters !!")
    private String name;

    @Email(message = "Invalid User Email !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender !!")
    private String gender;


    @NotBlank(message = "About is required !!")
    private String about;

    private String imageName;

    @Override
    public String toString() {
        return "UserDto{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", about='" + about + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
