package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.entity.Product;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.PageableResponse;
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
import org.springframework.data.domain.*;

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
    private CategoryServiceImpl categoryServiceImpl;

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
        String productId=UUID.randomUUID().toString();

        Mockito.when(productRepositoryI.findById(Mockito.anyString())).thenReturn(Optional.of(product1));

        productServiceImpl.deteteSingleProduct(productId);

        Mockito.verify(productRepositoryI,Mockito.times(1)).delete(product1);
    }

    @Test
    void getSingleProductTest() {

        String productId=UUID.randomUUID().toString();

        Mockito.when(productRepositoryI.findById(productId)).thenReturn(Optional.of(product1));

        ProductDto productDto1 = productServiceImpl.getSingleProduct(productId);

        Assertions.assertNotNull(productDto1);
    }

    @Test
    void getAllProducts() {
        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";


        Sort sort=Sort.by("title").ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product>  page=new PageImpl<>(products);

        Mockito.when(productRepositoryI.findAll(pageable)).thenReturn(page);

        PageableResponse<ProductDto> allProducts = productServiceImpl.getAllProducts(pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,allProducts.getContent().size());
    }

    @Test
    void getAllLive() {
        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";

        Sort sort=Sort.by("title").ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product>  page=new PageImpl<>(products);

        Mockito.when(productRepositoryI.findByLiveTrue(pageable)).thenReturn(page);

        PageableResponse<ProductDto> allLiveProducts = productServiceImpl.getAllLive(pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,allLiveProducts.getContent().size());
    }

    @Test
    void searchProducts() {

        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";
        String subTitle="a";

        Sort sort=Sort.by("title").ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product>  page=new PageImpl<>(products);

        Mockito.when(productRepositoryI.findByTitleContaining(subTitle,pageable)).thenReturn(page);

        PageableResponse<ProductDto> allSearchedProducts = productServiceImpl.searchProducts(subTitle,pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,allSearchedProducts.getContent().size());
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

        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";
        String categoryId=UUID.randomUUID().toString();
        Category category = Category.builder()
                .categoryId(categoryId)
                .title("Laptops")
                .categoryDescription("Laptops available with discount")
                .build();

        Sort sort=Sort.by("title").ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product>  page=new PageImpl<>(products);
        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category));

        Mockito.when(productRepositoryI.findByCategory(category,pageable)).thenReturn(page);

        PageableResponse<ProductDto> allProductsOfCategory = productServiceImpl.getAllProductsOfCategory(categoryId,pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,allProductsOfCategory.getContent().size());
    }

    @Test
    public void updateProductExceptionTest(){
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productServiceImpl.updateProduct(null,null));

    }

    @Test
    public void getSingleProductExceptionTest(){

        Assertions.assertThrows(ResourceNotFoundException.class,()-> productServiceImpl.getSingleProduct(null));
    }
    @Test
    public void deleteSingleProductTest(){
        Assertions.assertThrows(ResourceNotFoundException.class,()-> productServiceImpl.deteteSingleProduct(null));
    }

    @Test
    public void getCategoryByIdExceptionTest(){
        Assertions.assertThrows(ResourceNotFoundException.class,()-> categoryServiceImpl.getCategoryById(null));
    }
}