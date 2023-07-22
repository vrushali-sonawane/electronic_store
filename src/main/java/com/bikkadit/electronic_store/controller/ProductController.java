package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.ProductServiceI;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceI productServiceI;

    private static Logger logger= LoggerFactory.getLogger(ProductController.class);


    //create

    /**
     * @author Vrushali Sonawane
     * @apiNote  create product
     * @param productDto
     * @return productDto
     */
    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
        logger.info("Initiating request to create product");
        ProductDto createdProduct = productServiceI.createProduct(productDto);
        logger.info("completed request to create product");
        return  new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //update
    /**
     * @author Vrushali Sonawane
     * @apiNote  update product
     * @param productDto
     * @return productDto
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId){
        logger.info("Initiating request to update product details:{}",productId);
        ProductDto updatedProduct = productServiceI.updateProduct(productDto,productId);
        logger.info("Completed request to update product details:{}",productId);
        return  new ResponseEntity<>(updatedProduct, HttpStatus.CREATED);
    }
    //delete
    /**
     * @author Vrushali Sonawane
     * @apiNote  delete single product
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId){
        logger.info("Initiating request to delete single product details:{}",productId);
        productServiceI.deteteSingleProduct(productId);
        logger.info("completed request to delete  single product details:{}",productId);
        return new ResponseEntity<>(AppConstants.DELETE_PRODUCT,HttpStatus.OK);
    }

    //getSingleProduct

    /**
     * @author Vrushali Sonawane
     * @apiNote get single product
     * @param productId
     * @return productDto
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId){
        logger.info("Initiating request to get single product details:{}",productId);
        ProductDto product = productServiceI.getSingleProduct(productId);
        logger.info("completed  request to get single product details:{}",productId);
        return new ResponseEntity<>(product,HttpStatus.OK);

    }

    //getAllProducts

    /**
     * @author Vrushali Sonawane
     * @apiNote get All products
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir
    ){
        logger.info("Initiating request to get All product details");
        PageableResponse<ProductDto> allProducts = productServiceI.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        logger.info("completed request to get All product details");
        return  new ResponseEntity<>(allProducts,HttpStatus.OK);
    }
    //getAllLive

    /**
     * @author Vrushali Sonawane
     * @apiNote get All live Products
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir
    ){
        logger.info("Initiating request to get live product details");
        PageableResponse<ProductDto> allProducts = productServiceI.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        logger.info("completed request to get Live product details");
        return  new ResponseEntity<>(allProducts,HttpStatus.OK);
    }
    //searchProduct

    /**
     * @author Vrushali Sonawane
     * @apiNote  search product by title
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param subTitle
     * @return
     */
    @GetMapping("search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir,
            @PathVariable String subTitle
    ){
        logger.info("Initiating request to search product details:{}",subTitle);
        PageableResponse<ProductDto> allProducts = productServiceI.searchProducts(subTitle,pageNumber, pageSize, sortBy, sortDir);
        logger.info("Initiating request to search product details:{}",subTitle);
        return  new ResponseEntity<>(allProducts,HttpStatus.OK);
    }

}
