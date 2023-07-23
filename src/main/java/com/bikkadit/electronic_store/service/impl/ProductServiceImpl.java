package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.entity.Product;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.payload.Helper;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.repository.CategoryRepositoryI;
import com.bikkadit.electronic_store.repository.ProductRepositoryI;
import com.bikkadit.electronic_store.service.ProductServiceI;
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
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    private ProductRepositoryI productRepositoryI;

    @Autowired
    private CategoryRepositoryI categoryRepositoryI;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.image.path}")
    private String imagePath;

    private static Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        logger.info("Initiating dao call to create product details");
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepositoryI.save(product);
        logger.info("completed dao call to create product details");
         return modelMapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        logger.info("Initiating dao call to update product details:{}",productId);
        Product product = productRepositoryI.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());

        Product updatedProduct = productRepositoryI.save(product);
        logger.info("completed dao call to update product details:{}",productId);
        return modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void deteteSingleProduct(String productId) {
        logger.info("Initiating dao call to update product details:{}",productId);
        Product product = productRepositoryI.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND ));
        String fullPath = imagePath + product.getProductImage();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("product image not found in this folder");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        logger.info("Completed dao call to update product details:{}",productId);
          productRepositoryI.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
           logger.info("Initiating dao call to get single product:{}",productId);

        Product product = productRepositoryI.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND ));
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        logger.info("completed dao call to get single product details:{}",productId);
        return productDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        logger.info("Initiating dao call to get all product details");
      Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

       Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepositoryI.findAll(pageable);

        PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);
        logger.info("completed dao call to get all product details");
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize, String sortBy,String sortDir) {
        logger.info("Initiating dao call to get all live product details");
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepositoryI.findByLiveTrue(pageable);

        PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);
        logger.info("completed dao call to get all live product details");
        return response;
    }

    @Override
    public PageableResponse<ProductDto> searchProducts(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        logger.info("Initiating dao call to search product details:{}",subTitle);
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepositoryI.findByTitleContaining(subTitle, pageable);

        PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);
        logger.info("completed dao call to search product details:{}",subTitle);
        return response;
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
       logger.info("Initiating dao call to create product with category:{}",categoryId);
        Category category = categoryRepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND));

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());

        Product product = modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepositoryI.save(product);

        logger.info("Completed dao call to create product with category:{}",categoryId);
        return modelMapper.map(savedProduct,ProductDto.class);


    }


}
