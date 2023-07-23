package com.bikkadit.electronic_store.repository;

import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositoryI extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);

}
