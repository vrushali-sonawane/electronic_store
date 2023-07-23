package com.bikkadit.electronic_store.service;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.payload.PageableResponse;

import java.util.List;

public interface ProductServiceI {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete
    void deteteSingleProduct(String productId);

    //getSingleProuct
    ProductDto getSingleProduct(String productId);

    //getAllProduct
    PageableResponse<ProductDto> getAllProducts(int pageNumber,int pageSize, String sortBy,String sortDir);

    //get All Live
    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize, String sortBy,String sortDir);


    //searchProduct
    PageableResponse<ProductDto> searchProducts(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    //create product with category
    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);


}
