package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.service.CategoryServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @MockBean
    private CategoryServiceI categoryServiceI;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category;

    @BeforeEach
    public void setUp(){
        String categoryId1 = UUID.randomUUID().toString();
        category= Category.builder()
                .categoryId(categoryId1)
                .title("Laptops")
                .categoryDescription("Laptops available with discount")
                .coverImage("abc.png")
                .build();
    }

    @Test
    void createCategoryTest() throws Exception {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryServiceI.createCategory(Mockito.any())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(category))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());




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
    void updateCategory() {
    }

    @Test
    void getSingleCategory() {
    }

    @Test
    void deleteCategory() {
    }

    @Test
    void getAllCategories() {
    }

    @Test
    void searchCategories() {
    }

    @Test
    void uploadCategoryImage() {
    }

    @Test
    void downloadCategoryImage() {
    }

    @Test
    void createProductWithCategory() {
    }

    @Test
    void testUpdateCategory() {
    }

    @Test
    void getAllProductsOfCategories() {
    }
}