package com.bikkadit.electronic_store.service;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.payload.PageableResponse;

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
    PageableResponse<UserDto> searchUser(String keyword,int pageNumber, int pageSize, String sortBy, String sortDir);

    //get All Users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);


}
