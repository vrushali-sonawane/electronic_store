package com.bikkadit.electronic_store.service;

import com.bikkadit.electronic_store.dto.UserDto;

import java.util.List;

public interface UserServiceI {

    //create User
    UserDto createUser(UserDto userDto);

    //update User
    UserDto updateUser(UserDto userDto, String userId);

    //get single user
    UserDto getUserById(String userId);

    //get single user
    UserDto getUserByEmail(String email);

    //delete user
    void deleteUser(String userId);

    //search user
    List<UserDto> searchUser(String keyword);

    //get All Users
    List<UserDto> getAllUser();


}
