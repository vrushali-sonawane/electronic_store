package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.payload.ApiResponseMessage;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.payload.ImageResponse;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.CategoryServiceI;
import com.bikkadit.electronic_store.service.FileServiceI;
import com.bikkadit.electronic_store.service.ProductServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static Logger logger= LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryServiceI categoryServiceI;

    @Autowired
    private ProductServiceI productServiceI;

    @Autowired
    private FileServiceI fileServiceI;
    @Value("${category.image.path}")
    private String imagepath;

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
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId){
        logger.info("Initiating request to delete category :{}",categoryId);
        categoryServiceI.deleteCategory(categoryId);

        logger.info("completed request to delete category :{}",categoryId);
        return new ResponseEntity<>(AppConstants.DELETE_CATEGORY,HttpStatus.OK);
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
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value="sortBy" ,defaultValue = AppConstants.SORT_BY_TITLE,required = false)String sortBy,
            @RequestParam(value="sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir
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
            @RequestParam(value ="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value="sortBy" ,defaultValue = AppConstants.SORT_BY_TITLE,required = false)String sortBy,
            @RequestParam(value="sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir,@PathVariable String keyword){

        logger.info("Initiating request to search  categories");
        PageableResponse<CategoryDto> categories = categoryServiceI.searchCategories(pageNumber, pageSize, sortBy, sortDir,keyword);
        logger.info("completed  request to search  categories");
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    //uploading cover-image

    /**
     * @author Vrushali Sonawane
     * @apiNote upload category image
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("coverImage")MultipartFile image,
                                                             @PathVariable String categoryId ) throws IOException {
       logger.info("Initiating request to upload category image: {}",categoryId);
        String categoryImage = fileServiceI.uploadFile(image, imagepath);
        //get category
        CategoryDto categoryDto = categoryServiceI.getCategoryById(categoryId);
        categoryDto.setCoverImage(categoryImage);
        CategoryDto updatedCategory = categoryServiceI.updateCategory(categoryDto, categoryId);

        ImageResponse response=ImageResponse.builder()
                .imageName(categoryImage).message(AppConstants.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        logger.info("completed request to upload category image: {}",categoryId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    //downloading cover-image

    /**
     * @author Vrushali Sonawane
     * @apiNote download category image
     * @param categoryId
     * @param response
     * @throws IOException
     */
    @GetMapping("image/{categoryId}")
    public void downloadCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        logger.info("Initiating request to download user image: {}",categoryId);
        CategoryDto categoryDto = categoryServiceI.getCategoryById(categoryId);
        logger.info("category Image name: {}", categoryDto.getCoverImage());

        InputStream resource = fileServiceI.getResource(imagepath, categoryDto.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("completed request to download category image: {}",categoryId);
    }

    /**
     * @author Vrushali Sonawane
     * @apiNote create product with category
     * @param categoryId
     * @param productDto
     * @return productDto
     */
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId, @RequestBody ProductDto productDto){
        logger.info("Initiating request to create product with category:{}",categoryId);

        ProductDto productWithCategory = productServiceI.createProductWithCategory(productDto, categoryId);

        logger.info("Completed request to create product with category:{}",categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);

    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategory(@PathVariable String categoryId,
                                                     @PathVariable String productId){
        logger.info("Initiating request  to update category:{}",categoryId);
        ProductDto productDto = productServiceI.updateCategory(categoryId, productId);
        logger.info("completed request  to update category:{}",categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.CREATED);

    }
}
