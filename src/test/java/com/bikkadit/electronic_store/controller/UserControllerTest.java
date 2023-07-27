package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.service.UserServiceI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@SpringBootTest(classes = UserControllerTest.class)
//to enable Autoconfiguration for mockMvc
@AutoConfigureMockMvc
@ContextConfiguration
@ComponentScan(basePackages = "electronic_store")
class UserControllerTest {

    @Mock
    private UserServiceI userServiceI;

    @InjectMocks
    private UserController userController;


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUserTest() throws Exception {
        String id = UUID.randomUUID().toString();
       UserDto user=new UserDto(id,"Sahil","sahil123@gmail.com","sahil99","I am developer","male","abc.png");

      when(userServiceI.createUser(user));

       ObjectMapper objectMapper=new ObjectMapper();
        String userAsString = objectMapper.writeValueAsString(user);


        mockMvc.perform(post("/")
                .content(userAsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andDo(print());


    }

    @Test
    void upadteUser() {
        String id = UUID.randomUUID().toString();
        UserDto userDto = UserDto.builder()
                .userId(id)
                .name("Sahil")
                .password("ss11")
                .gender("male")
                .about("I am developer")
                .imageName("abc.png")
                .build();

    }

    @Test
    void getSingleUSerById() {
        String id = UUID.randomUUID().toString();
        UserDto userDto = UserDto.builder()
                .userId(id)
                .name("Sahil")
                .password("ss11")
                .gender("male")
                .about("I am developer")
                .imageName("abc.png")
                .build();


    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void searchUser() {
    }

    @Test
    void getAllUSers() {
    }
}