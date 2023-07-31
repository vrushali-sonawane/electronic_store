package com.bikkadit.electronic_store.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceImplTest {

    @Autowired
    private FileServiceImpl fileServiceImpl;
   String  file= UUID.randomUUID().toString();
   String path="image/users";
   String fullPath=file + path;


    @Test
    void uploadFileTest() {

    }

    @Test
    void getResourceTest() {
    }
}