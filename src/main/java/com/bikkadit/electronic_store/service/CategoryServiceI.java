package com.bikkadit.electronic_store.service;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.payload.PageableResponse;


import java.util.List;

public interface CategoryServiceI {

    //create category
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);


    //delete
    void deleteCategory(String categoryId);

    //get single category
    CategoryDto getCategoryById(String categoryId);

    //get All categories
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    //search categories
    PageableResponse<CategoryDto> searchCategories(int pageNumber, int pageSize, String sortBy, String sortDir,String title);


}
