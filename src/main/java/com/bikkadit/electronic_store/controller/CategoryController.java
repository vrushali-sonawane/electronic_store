package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.payload.ApiResponseMessage;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.CategoryServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static Logger logger= LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryServiceI categoryServiceI;

    /**
     * @author Vrushali Sonawane
     * @apiNote  create category
     * @param categoryDto
     * @return categoryDto
     */
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        logger.info("Initiating request to create category");
        CategoryDto createdCategory = categoryServiceI.createCategory(categoryDto);
        logger.info("completed request to create category");
        return  new ResponseEntity<>(createdCategory, HttpStatus.CREATED);

    }

    /**
     * @author Vrushali sonawane
     * @apiNote update single category
     * @param categoryDto
     * @param categoryId
     * @return
     */

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
       logger.info("Initiating request to update category:{}",categoryId);
        CategoryDto updatedCategory = categoryServiceI.updateCategory(categoryDto, categoryId);
        logger.info("compeleted request to update category:{}",categoryId);
        return  new ResponseEntity<>(updatedCategory,HttpStatus.CREATED);
    }

    /**
     * @author Vrushali Sonawane
     * @apiNote get single category
     * @param categoryId
     * @return categoryDto
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId){
        logger.info("Initiating request to get single category:{}",categoryId);
        CategoryDto categoryDto = categoryServiceI.getCategoryById(categoryId);
        logger.info("completed request to get single category:{}",categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);

    }

    /**
     * @author Vrushali Sonawane
     * @apiNote delete single category
     * @param categoryId
     * @return
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        logger.info("Initiating request to delete category :{}",categoryId);
        categoryServiceI.deleteCategory(categoryId);
        ApiResponseMessage response=ApiResponseMessage.builder().
                message("category successfully deleted ").success(true).status(HttpStatus.OK).build();
        logger.info("completed request to delete category :{}",categoryId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * @author Vrushali Sonawane
     * @apiNote get all categories
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value="sortBy" ,defaultValue = "title",required = false)String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir
    ){
        logger.info("Initiating request to get All categories");
        PageableResponse<CategoryDto> allCategories = categoryServiceI.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        logger.info("completed request to get All categories");

        return new ResponseEntity<>(allCategories,HttpStatus.OK);

    }

    /**
     * @author Vrushali Sonawane
     * @apiNote search categories
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param keyword
     * @return
     */

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<CategoryDto>> searchCategories(
            @RequestParam(value ="pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value="sortBy" ,defaultValue = "title",required = false)String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir,@PathVariable String keyword){

        logger.info("Initiating request to search  categories");
        PageableResponse<CategoryDto> categories = categoryServiceI.searchCategories(pageNumber, pageSize, sortBy, sortDir,keyword);
        logger.info("completed  request to search  categories");
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
}
