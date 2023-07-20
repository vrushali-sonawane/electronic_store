package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.payload.Helper;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.repository.CategoryRepositoryI;
import com.bikkadit.electronic_store.service.CategoryServiceI;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryServiceI {

    private static Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepositoryI categoryRepositoryI;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.image.path}")
    private String imagepath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        logger.info("Initiating dao call to save category");
        //generate random categoryId
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepositoryI.save(category);
        logger.info("Completed dao call to save category");
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        logger.info("Initiating dao call to update category:{}" ,categoryId);
        Category category = categoryRepositoryI.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));

        category.setTitle(categoryDto.getTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepositoryI.save(category);

        logger.info("completed  dao call to update category:{}" ,categoryId);
        return modelMapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        logger.info("Initiating dao call to delete category:{}" ,categoryId);
        Category category = categoryRepositoryI.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));
        String fullPath= imagepath+ category.getCoverImage();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException e){
            logger.info("category image is not found in folder");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        logger.info("completed dao call to delete category:{}" ,categoryId);
        categoryRepositoryI.delete(category);

    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        logger.info("Initiating dao call to get single category:{}" ,categoryId);
        Category category = categoryRepositoryI.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));

        logger.info("completed dao call to get single category:{}" ,categoryId);
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
       logger.info("Initiating dao call to get All users");
        Sort sort=(sortDir.equalsIgnoreCase("desc"))? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> page = categoryRepositoryI.findAll(pageable);

        PageableResponse<CategoryDto> response = Helper.getpageableResponse(page, CategoryDto.class);

        logger.info("completed dao call to get All users");
        return response;
    }

    @Override
    public PageableResponse<CategoryDto> searchCategories(int pageNumber, int pageSize, String sortBy, String sortDir,String title) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepositoryI.findByTitleContaining(pageable, title);

        PageableResponse<CategoryDto> response = Helper.getpageableResponse(page, CategoryDto.class);
        return response;
    }
}
