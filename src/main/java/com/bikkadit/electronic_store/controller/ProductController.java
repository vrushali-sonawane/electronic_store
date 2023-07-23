package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.payload.ImageResponse;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.FileServiceI;
import com.bikkadit.electronic_store.service.ProductServiceI;
import lombok.Getter;
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

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceI productServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Value("${product.image.path}")
    private String imagePath;

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
        logger.info("completed request to search product details:{}",subTitle);
        return  new ResponseEntity<>(allProducts,HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("productImage")MultipartFile image,@PathVariable String productId) throws IOException {
        logger.info("Initiating request to upload product image:{}",productId);
        String imageName = fileServiceI.uploadFile(image, imagePath);
        ProductDto productDto = productServiceI.getSingleProduct(productId);
        productDto.setProductImage(imageName);
        ProductDto productDto1 = productServiceI.updateProduct(productDto, productId);

        ImageResponse response = ImageResponse.builder()
                .imageName(imageName).message(AppConstants.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        logger.info("completed request to upload product image:{}",productId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void downloadProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
       logger.info("Initiating request to download product image:{}",productId);
        ProductDto productDto = productServiceI.getSingleProduct(productId);
        //String productImage = productDto.getProductImage();

        InputStream resource = fileServiceI.getResource(imagePath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Completed request to download product image:{}",productId);

    }
}
