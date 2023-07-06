package com.bikkadit.electronic_store.dto;

import com.bikkadit.electronic_store.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;
    private String name;
    private String email;
    private String password;
    private String about;
    private String gender;
    private String imageName;


}
