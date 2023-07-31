package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.service.UserServiceI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@SpringBootTest
//to enable Autoconfiguration for mockMvc
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserServiceI userServiceI;

    @Autowired
    private UserController userController;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    public void setUp() {

        String id = UUID.randomUUID().toString();
        user = User.builder()
                .userId(id)
                .name("Sahil")
                .email("sahil@gmail.com")
                .password("Sahil123")
                .gender("male")
                .about("I am developer")
                .imageName("abc.png")
                .build();
    }

    @Test
    void createUserTest() throws Exception {

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userServiceI.createUser(userDto)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
                //.andExpect(jsonPath("$.name").exists());


    }

    private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Test
    void upadteUserTest() throws Exception {
        String userId = UUID.randomUUID().toString();

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userServiceI.updateUser(userDto,userId)).thenReturn(userDto);

        mockMvc.perform(
                  MockMvcRequestBuilders.put("/users/" +userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
                //.andExpect(jsonPath("$.name").exists());
    }

    @Test
    void getSingleUSerById() throws Exception {
        String userId = UUID.randomUUID().toString();

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userServiceI.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" +userId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());

    }


    @Test
    void getUserByEmail() throws Exception {
        String email="sahil@gmail.com";

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userServiceI.getUserByEmail(email)).thenReturn(userDto);

        mockMvc.perform(
                         MockMvcRequestBuilders.get("/users/email/" +email)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists());


    }

    @Test
    void deleteUser() throws Exception {
        String userId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void searchUser() {


    }

    @Test
    void getAllUSers() {
    }
}