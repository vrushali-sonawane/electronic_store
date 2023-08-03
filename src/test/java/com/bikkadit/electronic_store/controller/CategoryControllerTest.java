package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.entity.Product;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.CategoryServiceI;
import com.bikkadit.electronic_store.service.ProductServiceI;
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

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
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

    @MockBean
    private ProductServiceI productServiceI;

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
    void updateCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();
        CategoryDto categoryDto=CategoryDto.builder()
                .categoryId(categoryId)
                .title("Mobiles")
                .categoryDescription("Mobiles available with discounts")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryServiceI.updateCategory(Mockito.any(),Mockito.anyString())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/" +categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(category))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.categoryDescription").exists());


    }

    @Test
    void getSingleCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryServiceI.getCategoryById(categoryId)).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+categoryId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.categoryDescription").exists());

    }

    @Test
    void deleteCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void getAllCategoriesTest() throws Exception {
        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Headphones")
                .categoryDescription("Headphones available with good quality")
                .coverImage("xyz.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("LED TVS")
                .categoryDescription("LED TVS available with good quality")
                .coverImage("def.png")
                .build();
        CategoryDto categoryDto3=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Electronics")
                .categoryDescription("Eletronics products available with good quality")
                .coverImage("hjh.png")
                .build();
        CategoryDto categoryDto4=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("phones")
                .categoryDescription("phones available with good quality")
                .coverImage("dbw.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(categoryDto1,categoryDto2,categoryDto3,categoryDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalpages(100);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setLastpage(false);

        Mockito.when(categoryServiceI.getAllCategories(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void searchCategoriesTest() throws Exception {

        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Headphones")
                .categoryDescription("Headphones available with good quality")
                .coverImage("xyz.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("LED TVS")
                .categoryDescription("LED TVS available with good quality")
                .coverImage("def.png")
                .build();
        CategoryDto categoryDto3=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Electronics")
                .categoryDescription("Eletronics products available with good quality")
                .coverImage("hjh.png")
                .build();
        CategoryDto categoryDto4=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("phones")
                .categoryDescription("phones available with good quality")
                .coverImage("dbw.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(categoryDto1,categoryDto2,categoryDto3,categoryDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalpages(100);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setLastpage(false);

        String subTitle="a";

        Mockito.when(categoryServiceI.searchCategories(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/search/"+subTitle)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void uploadCategoryImage() {
    }

    @Test
    void downloadCategoryImage() {
    }

    @Test
    void createProductWithCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        String productId= UUID.randomUUID().toString();
     Product   product1 = Product.builder()
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
                .category(category)
                .build();

        ProductDto productDto = modelMapper.map(product1, ProductDto.class);

        Mockito.when(productServiceI.createProductWithCategory(Mockito.any(),Mockito.anyString())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/"+categoryId+"/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(product1))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());




    }

    @Test
    void UpdateCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        String productId= UUID.randomUUID().toString();
        Product   product1 = Product.builder()
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
                .category(category)
                .build();
        ProductDto productDto = modelMapper.map(product1, ProductDto.class);

        Mockito.when(productServiceI.updateCategory(categoryId,productId)).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/"+categoryId+ "/products/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    void getAllProductsOfCategoriesTest() throws Exception {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        ProductDto productDto1=ProductDto.builder().productId(UUID.randomUUID().toString())
               .title("Redmi headphones")
               .description("Redmi headphones available ith less price")
               .productImage("abc.png")
               .quantity(100)
               .price(3000.00)
               .discountedPrice(300.00)
               .live(true)
               .stock(true)
               .category(categoryDto)
               .build();

        ProductDto productDto2=ProductDto.builder().productId(UUID.randomUUID().toString())
                .title("Boat headphones")
                .description("Boat headphones available ith less price")
                .productImage("dss.png")
                .quantity(100)
                .price(3000.00)
                .discountedPrice(300.00)
                .live(true)
                .stock(true)
                .category(categoryDto)
                .build();
        ProductDto productDto3=ProductDto.builder().productId(UUID.randomUUID().toString())
                .title("Sony headphones")
                .description("Sony headphones available ith less price")
                .productImage("xyz.png")
                .quantity(100)
                .price(3000.00)
                .discountedPrice(300.00)
                .live(true)
                .stock(true)
                .category(categoryDto)
                .build();


        PageableResponse<ProductDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(productDto1,productDto2,productDto3));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalpages(100);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setLastpage(false);



        Mockito.when(productServiceI.getAllProductsOfCategory(Mockito.anyString(),Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+categoryDto.getCategoryId()+"/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}