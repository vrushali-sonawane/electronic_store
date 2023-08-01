package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.FileServiceI;
import com.bikkadit.electronic_store.service.UserServiceI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.http.MediaType;


import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
//to enable Autoconfiguration for mockMvc
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserServiceI userServiceI;

    @MockBean
    private FileServiceI fileServiceI;

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
    void deleteUserTest() throws Exception {
        String userId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void searchUserTest() throws Exception {


        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Samar")
                .email("samar@gmail.com").password("samar123").about("developer").imageName("abc.png").build();
        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Veeraj")
                .email("veeraj@gmail.com").password("veeraj123").about("developer").imageName("xyz.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Sandip")
                .email("sandip@gmail.com").password("sandip123").about("developer").imageName("def.png").build();

        UserDto userDto4 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Kumar")
                .email("kumar@gmail.com").password("kumar123").about("developer").imageName("wds.png").build();


        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3,userDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setTotalpages(100);
        pageableResponse.setLastpage(false);

        String keyword="a";

        Mockito.when(userServiceI.searchUser(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search/"+keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());



    }

    @Test
    void getAllUSersTest() throws Exception {

        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Samar")
                .email("samar@gmail.com").password("samar123").about("developer").imageName("abc.png").build();
        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Veeraj")
                .email("veeraj@gmail.com").password("veeraj123").about("developer").imageName("xyz.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Sandip")
                .email("sandip@gmail.com").password("sandip123").about("developer").imageName("def.png").build();

        UserDto userDto4 = UserDto.builder().userId(UUID.randomUUID().toString()).name("Kumar")
                .email("kumar@gmail.com").password("kumar123").about("developer").imageName("wds.png").build();


        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3,userDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setTotalpages(100);
        pageableResponse.setLastpage(false);

        Mockito.when(userServiceI.getAllUser(Mockito.anyInt(),
                Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());



    }

    @Test
    void uploadUserImageTest() throws Exception {
        String fileName="abc.png";
        String filePath="image/users";
        String userId=UUID.randomUUID().toString();

        Mockito.when(fileServiceI.uploadFile(Mockito.any(),Mockito.anyString())).thenReturn(fileName);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userServiceI.getUserById(Mockito.anyString())).thenReturn(userDto);
        userDto.setImageName(fileName);

        Mockito.when(userServiceI.updateUser(userDto,userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/image/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());


    }

    @Test
    void serveUserImageTest() {

    }
}