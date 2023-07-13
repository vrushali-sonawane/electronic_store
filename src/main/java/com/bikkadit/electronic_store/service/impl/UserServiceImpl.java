package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.repository.UserRepositoryI;
import com.bikkadit.electronic_store.service.UserServiceI;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserServiceI {

    @Autowired
    private UserRepositoryI userRepositoryI;

   @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Initiating dao call to create user");
        //to get Unique String for every user
       String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
      // User user= dtoToEntity(userDto);
       User user= modelMapper.map(userDto,User.class);
        User saveUser = userRepositoryI.save(user);
       //UserDto usetDto1= entityToDto(saveUser);
        UserDto userDto1=modelMapper.map(saveUser,UserDto.class);
        log.info("Completing dao call to create user");
        return userDto1;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        log.info("Initiating dao call to update user:{}"+userId);
      User user=  userRepositoryI.findById(userId).orElseThrow(() ->
              new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());

       User updatedUser= userRepositoryI.save(user);
      // UserDto userDto1=entityToDto(updatedUser);
        UserDto userDto1=modelMapper.map(updatedUser, UserDto.class);
        log.info("Completing dao call to update user:{}"+userId);
        return userDto1;
    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("Initiating dao call to get single user:{}"+userId);
       User user= userRepositoryI.findById(userId).orElseThrow(() ->
               new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
       // UserDto userDto1=  entityToDto(user);
        UserDto userDto1=modelMapper.map(user, UserDto.class);
        log.info("Initiating dao call to get single user:{}"+userId);
        return userDto1;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Initiating dao call to get user record:{}"+email);
      User user=  userRepositoryI.findUserByEmail(email).orElseThrow(() ->
              new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
      //UserDto userDto2= entityToDto(user);
        UserDto  userDto2=modelMapper.map(user, UserDto.class);
        log.info("Completing dao call to get user record:{}"+email);
        return userDto2;
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Initiating dao call to delete user record:{}"+userId);
      User user=  userRepositoryI.findById(userId).orElseThrow(()->
              new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        log.info("Completing dao call to delete user record:{}"+userId);
      userRepositoryI.delete(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Initiating dao call to get users record:{}"+keyword);
        List<User> users=userRepositoryI.findUserByNameContaining(keyword);
        List<UserDto> userDtos = users.stream()
                .map((user) -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        log.info("Initiating dao call to get users record:{}"+keyword);
        return userDtos;
    }

    @Override
    public List<UserDto> getAllUser() {
        log.info("Initiating dao call to get all users record");
        List<User> users= userRepositoryI.findAll();
        List<UserDto> userDtos=  users.stream()
              .map((user) -> modelMapper.map(user, UserDto.class))
              .collect(Collectors.toList());
        log.info("Completed dao call to get all users record");
          return userDtos;
    }

//    public User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();
//
//       User user= modelMapper.map(userDto,User.class);
//
//        return user;
//    }
//
//    public UserDto entityToDto(User user) {
//        UserDto userDto = UserDto.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .gender(user.getGender())
//                .imageName(user.getImageName())
//                .build();
//
//      UserDto userDto=  modelMapper.map(user,UserDto.class);
//        return userDto;
//    }

}
