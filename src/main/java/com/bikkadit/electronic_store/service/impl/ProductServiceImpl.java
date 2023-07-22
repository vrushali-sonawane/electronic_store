package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.dto.ProductDto;
import com.bikkadit.electronic_store.entity.Product;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.Helper;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.repository.ProductRepositoryI;
import com.bikkadit.electronic_store.service.ProductServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    private ProductRepositoryI productRepositoryI;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepositoryI.save(product);
         return modelMapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepositoryI.findById(productId).orElseThrow(() -> new ResourceNotFoundException());

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());

        Product updatedProduct = productRepositoryI.save(product);
        return modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void deteteSingleProduct(String productId) {
        Product product = productRepositoryI.findById(productId).orElseThrow(() -> new ResourceNotFoundException());
          productRepositoryI.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {

        Product product = productRepositoryI.findById(productId).orElseThrow(() -> new ResourceNotFoundException());
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
      Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

       Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepositoryI.findAll(pageable);

        PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);

        return response;
    }



    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize, String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepositoryI.findByLiveTrue(pageable);

        PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);

        return response;
    }

    @Override
    public PageableResponse<ProductDto> searchProducts(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page = productRepositoryI.findByTitleContaining(subTitle, pageable);

        PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);

        return response;
    }


}
