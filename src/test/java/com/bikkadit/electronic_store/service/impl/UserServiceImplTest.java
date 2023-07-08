package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.repository.UserRepositoryI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
 class UserServiceImplTest {

    @MockBean
    private UserRepositoryI userRepositoryI;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserServiceImpl userServiceImpl;
    User user;
    User user1;
    UserDto userDto;

    List<User> list;


    @BeforeEach
    public void setUp(){
       String id1 = UUID.randomUUID().toString();
       user = User.builder()
               .userId(id1)
                .name("Sahil")
                .password("ss11")
                .gender("male")
                .email("sahil@gmail.com")
                .about("I am Developer")
                .imageName("abc.png")
                .build();
       String id2= UUID.randomUUID().toString();

         user1 = User.builder()
                 .userId(id2)
                .name("Veeraj")
                .password("vv11")
                .gender("male")
                .email("veeraj@gmail.com")
                .about("I am Developer")
                .imageName("abc.png")
                .build();
         list=new ArrayList<>();
         list.add(user);
         list.add(user1);


    }

    @Test
    void createUser() {
       Mockito.when(userRepositoryI.save(Mockito.any())).thenReturn(user);

        UserDto userDto1 = userServiceImpl.createUser(modelMapper.map(user, UserDto.class));

        assertEquals("ss11",userDto1.getPassword());
    }

    @Test
    void updateUser() {
       String id = UUID.randomUUID().toString();

       Mockito.when(userRepositoryI.findById(id)).thenReturn(Optional.of(user));

       Mockito.when(userRepositoryI.save(Mockito.any())).thenReturn(user);

       UserDto userDto1 = userServiceImpl.updateUser(modelMapper.map(user, UserDto.class), id);

       assertEquals("Sahil",userDto1.getName());

    }

    @Test
    void getUserById() {
       String userId = UUID.randomUUID().toString();

       Mockito.when(userRepositoryI.findById(userId)).thenReturn(Optional.of(user));

       UserDto user2 = userServiceImpl.getUserById(userId);

       assertEquals("ss11",user2.getPassword());
    }

    @Test
    void getUserByEmail() {
       String email="sahil@gmail.com";

       Mockito.when(userRepositoryI.findUserByEmail(email)).thenReturn(Optional.of(user));

       UserDto user3 = userServiceImpl.getUserByEmail(email);

         String name="Sahil";
       assertEquals(name,user3.getName());
    }

    @Test
    void deleteUser() {
       String userId = UUID.randomUUID().toString();

       Mockito.when(userRepositoryI.findById(userId)).thenReturn(Optional.of(user));

       userServiceImpl.deleteUser(userId);

    }

    @Test
    void searchUser() {

       String keyword="s";
       Mockito.when(userRepositoryI.findAll()).thenReturn(list);

       List<UserDto> userDtos = userServiceImpl.searchUser(keyword);

       assertNotNull(userDtos);

    }

    @Test
    void getAllUser() {

       Mockito.when(userRepositoryI.findAll()).thenReturn(list);

       List<UserDto> allUser = userServiceImpl.getAllUser();

       int size = allUser.size();

       assertEquals(2,size);

    }
}