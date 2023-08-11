package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.entity.Product;
import com.bikkadit.electronic_store.service.ProductServiceI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    private ProductServiceI productServiceI;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    Product product;

    @BeforeEach
    public void init(){

        String productId= UUID.randomUUID().toString();
         product = Product.builder()
                .productId(productId)
                .title("Samsung A34")
                .discountedPrice(3000.00)
                .price(20000.00)
                .quantity(100)
                .live(true)
                .addedDate(new Date())
                .stock(true)
                .description("This mobile  has many fetures")
                .productImage("abc.png")
                .build();
    }

    @Test
    void createProductTest() throws Exception {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productServiceI.createProduct(Mockito.any())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());


    }

    private String convertObjectToJsonString (Object product){
        try{
            return new ObjectMapper().writeValueAsString(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Test
    void updateProductTest() throws Exception {
        String productId=UUID.randomUUID().toString();
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productServiceI.updateProduct(Mockito.any(),Mockito.anyString())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());


    }

    @Test
    void deleteProductTest() throws Exception {
        String productId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/"+productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getSingleProductTest() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getAllLive() {
    }

    @Test
    void searchProducts() {
    }

    @Test
    void uploadProductImage() {
    }

    @Test
    void downloadProductImage() {
    }
}