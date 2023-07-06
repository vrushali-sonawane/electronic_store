package com.bikkadit.electronic_store.repository;

import com.bikkadit.electronic_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryI extends JpaRepository<User, String> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    List<User> findUserByNameContaining(String keyword);
}
