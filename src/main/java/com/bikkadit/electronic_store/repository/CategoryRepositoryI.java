package com.bikkadit.electronic_store.repository;

import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.payload.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepositoryI extends JpaRepository<Category, String> {

    Page<Category> findByTitleContaining(Pageable pageable, String title);
}

