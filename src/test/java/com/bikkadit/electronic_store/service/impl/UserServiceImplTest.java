package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.repository.UserRepositoryI;
import net.bytebuddy.TypeCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

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
    void createUserTest() {
       Mockito.when(userRepositoryI.save(Mockito.any())).thenReturn(user);

        UserDto userDto1 = userServiceImpl.createUser(modelMapper.map(user, UserDto.class));

       Assertions.assertEquals("ss11",userDto1.getPassword());
    }

    @Test
    void updateUserTest() {
       String id = UUID.randomUUID().toString();
        userDto = UserDto.builder()
               .userId(id)
               .name("Veeraj")
               .password("vv11")
               .gender("male")
               .email("veeraj@gmail.com")
               .about("I am Developer")
               .imageName("abc.png")
               .build();


       Mockito.when(userRepositoryI.findById(id)).thenReturn(Optional.of(user));

       Mockito.when(userRepositoryI.save(Mockito.any())).thenReturn(user);

       UserDto userDto1 = userServiceImpl.updateUser(userDto, id);

       Assertions.assertEquals("Veeraj",userDto1.getName());

    }

    @Test
    void getUserByIdTest() {
       String userId = UUID.randomUUID().toString();

       Mockito.when(userRepositoryI.findById(userId)).thenReturn(Optional.of(user));

       UserDto user2 = userServiceImpl.getUserById(userId);

       Assertions.assertEquals("ss11",user2.getPassword());
    }

    @Test
    void getUserByEmailTest() {
       String email="sahil@gmail.com";

       Mockito.when(userRepositoryI.findUserByEmail(email)).thenReturn(Optional.of(user));

       UserDto user3 = userServiceImpl.getUserByEmail(email);

         String name="Sahil";
       Assertions.assertEquals(name,user3.getName());
    }

    @Test
    void deleteUserTest() {
       String userId = UUID.randomUUID().toString();

       Mockito.when(userRepositoryI.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.deleteUser(userId);

       Mockito.verify(userRepositoryI,times(1)).delete(user);
    }

    @Test
    void searchUserTest() {

      int  pageNumber=0;
      int pageSize=2;
      String sortBy="name";
      String sortDir="asc";
      String keyword="a";

      Sort sort= Sort.by("name").ascending();
      Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);

      Page<User> page=new PageImpl<>(list);

      Mockito.when(userRepositoryI.findUserByNameContaining(keyword,pageable)).thenReturn(page);

      PageableResponse<UserDto> searchedUser = userServiceImpl.searchUser(keyword, pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,searchedUser.getContent().size());

    }

    @Test
    void getAllUserTest() {
        int pageNumber=0;
        int pageSize=2;
        String sortBy="name";
        String sortDir="asc";

        Sort sort= Sort.by("name").ascending();

        Page<User> page= new PageImpl<>(list);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

       Mockito.when(userRepositoryI.findAll(pageable)).thenReturn(page);

        PageableResponse<UserDto> allUser = userServiceImpl.getAllUser(pageNumber, pageSize, sortBy, sortDir);

         Assertions.assertEquals(2,allUser.getContent().size());

    }

   @Test
   public void updateUserExceptionTest(){
       Assertions.assertThrows(ResourceNotFoundException.class,()-> userServiceImpl.updateUser(null,null));
   }

   @Test
   public void deleteUserExceptionTest(){
       Assertions.assertThrows(ResourceNotFoundException.class,()->userServiceImpl.deleteUser(null));
   }

   @Test
   public void getUserByUserIdExceptionTest(){
       Assertions.assertThrows(ResourceNotFoundException.class,()->userServiceImpl.getUserById(null));
   }

   @Test
   public void getUserByEmailExceptionTest(){
       Assertions.assertThrows(ResourceNotFoundException.class,()->userServiceImpl.getUserByEmail(null));

   }
}