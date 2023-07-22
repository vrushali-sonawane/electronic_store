package com.bikkadit.electronic_store.repository;

import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.payload.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryI extends JpaRepository<User, String> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Page<User> findUserByNameContaining(String keyword, Pageable pageable);
}
