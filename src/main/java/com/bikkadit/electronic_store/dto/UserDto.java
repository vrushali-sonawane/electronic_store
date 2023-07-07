package com.bikkadit.electronic_store.dto;

import com.bikkadit.electronic_store.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min=3,max=20,message="Invalid name !!")
    private String name;

    @Email(message = "Invalid emailid !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @NotBlank(message="write something about yourself !!")
    private String about;

    @Size(min=4, max = 6,message = "Invalid gender !!")
    private String gender;

    private String imageName;


}
