package com.lcwd.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    private String name;

    private String email;

    private String password;

    private String gender;

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
