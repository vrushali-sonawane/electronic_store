package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.entity.Product;
import com.bikkadit.electronic_store.repository.CategoryRepositoryI;
import com.bikkadit.electronic_store.repository.ProductRepositoryI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductServiceImplTest {

    @MockBean
    private ProductRepositoryI productRepositoryI;

    @MockBean
    private CategoryRepositoryI categoryRepositoryI;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    Product  product1;

    Product product2;

    ProductDto productDto;

    List<Product> products;

    @BeforeEach
    public void init(){
        String productId= UUID.randomUUID().toString();
         product1 = Product.builder()
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

        String productId2= UUID.randomUUID().toString();
        product2 = Product.builder()
                .productId(productId2)
                .title("HP laptop")
                .discountedPrice(3000.00)
                .price(60000.00)
                .live(true)
                .quantity(100)
                .addedDate(new Date())
                .stock(true)
                .description("This laptop  has many fetures")
                .productImage("xyz.png")
                .build();

        products=new ArrayList<>();
        products.add(product1);
        products.add(product2);

    }

    @Test
    void createProductTest() {

        Mockito.when(productRepositoryI.save(Mockito.any())).thenReturn(product1);

        ProductDto productDto1 = productServiceImpl.createProduct(modelMapper.map(product1, ProductDto.class));

        Assertions.assertEquals(product1.getTitle(),productDto1.getTitle());
    }

    @Test
    void updateProductTest() {

        String productId=UUID.randomUUID().toString();
        ProductDto productDto1 = ProductDto.builder()
                .productId(productId)
                .title("Dell laptop")
                .discountedPrice(4000.00)
                .price(65000.00)
                .live(true)
                .quantity(100)
                .addedDate(new Date())
                .stock(true)
                .description("This laptop  has many features")
                .productImage("def.png")
                .build();

        Mockito.when(productRepositoryI.findById(Mockito.anyString())).thenReturn(Optional.of(product1));

        Mockito.when(productRepositoryI.save(Mockito.any())).thenReturn(product1);

        ProductDto productDto2 = productServiceImpl.updateProduct(productDto1, productId);

        Assertions.assertNotNull(productDto2);


    }

    @Test
    void deteteSingleProductTest() {

        Mockito.when(productRepositoryI.findById(Mockito.anyString())).thenReturn(Optional.of(product1));

        productRepositoryI.delete(product1);
    }

    @Test
    void getSingleProductTest() {

        String pruductId=UUID.randomUUID().toString();

        Mockito.when(productRepositoryI.findById(pruductId)).thenReturn(Optional.of(product1));

        ProductDto productDto1 = productServiceImpl.getSingleProduct(pruductId);

        Assertions.assertNotNull(productDto1);
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
    void createProductWithCategory() {
        String categoryId=UUID.randomUUID().toString();
        Category category = Category.builder()
                .categoryId(categoryId)
                .title("Laptops")
                .categoryDescription("Laptops available with discount")
                .build();

        String productId= UUID.randomUUID().toString();
        product1 = Product.builder()
                .productId(productId)
                .title("Samsung A34")
                .discountedPrice(3000.00)
                .price(20000.00)
                .quantity(100)
                .live(true)
                .category(category)
                .addedDate(new Date())
                .stock(true)
                .description("This mobile  has many fetures")
                .productImage("abc.png")
                .build();

        Mockito.when(categoryRepositoryI.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        Mockito.when(productRepositoryI.save(Mockito.any())).thenReturn(product1);

        ProductDto productWithCategory = productServiceImpl.createProductWithCategory(modelMapper.map(product1, ProductDto.class), categoryId);

        Assertions.assertNotNull(productWithCategory);


    }

    @Test
    void updateCategoryTest() {

        String categoryId=UUID.randomUUID().toString();
        Category category = Category.builder()
                .categoryId(categoryId)
                .title("Laptops")
                .categoryDescription("Laptops available with discount")
                .build();

        String productId= UUID.randomUUID().toString();
        product1 = Product.builder()
                .productId(productId)
                .title("Samsung A34")
                .discountedPrice(3000.00)
                .price(20000.00)
                .quantity(100)
                .live(true)
                .category(category)
                .addedDate(new Date())
                .stock(true)
                .description("This mobile  has many fetures")
                .productImage("abc.png")
                .build();

        Mockito.when(categoryRepositoryI.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        Mockito.when(productRepositoryI.findById(Mockito.anyString())).thenReturn(Optional.of(product1));

        Mockito.when(productRepositoryI.save(Mockito.any())).thenReturn(product1);

        ProductDto productDto1 = productServiceImpl.updateCategory(categoryId, productId);

        Assertions.assertNotNull(productDto1);
    }

    @Test
    void getAllProductsOfCategory() {
    }
}