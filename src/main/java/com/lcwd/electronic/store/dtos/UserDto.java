package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    //@NotEmpty

    @Size(min = 3, max = 20, message = "Username must be min of 3 and max of 15 characters !!")
    private String name;

    @Pattern(regexp = AppConstants.EMAIL_REGEX, message = "Please enter valid email id !!")
    @NotBlank(message = "Email is required !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender !!")
    private String gender;


    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImageNameValid(message = "Image is required !!")
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
