package com.bikkadit.electronic_store.dto;

import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    //@Email(message = "Invalid emailid !!")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid emailId !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message="Minimum 8 characters required and must contain one uppercase, one lowercase and one number")
    private String password;

    @NotBlank(message="write something about yourself !!")
    private String about;

    @Size(min=4, max = 6,message = "Invalid gender !!")
    private String gender;

    @ImageNameValid
    private String imageName;


}
