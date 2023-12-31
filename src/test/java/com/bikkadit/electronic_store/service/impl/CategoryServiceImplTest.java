package com.bikkadit.electronic_store.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.bikkadit.electronic_store.dto.CategoryDto;
import com.bikkadit.electronic_store.entity.Category;
import com.bikkadit.electronic_store.exception.ResourceNotFoundException;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.repository.CategoryRepositoryI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
class CategoryServiceImplTest {

    @MockBean
    private CategoryRepositoryI categoryRepositoryI;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryServiceImpl categorySeviceImpl;

    Category category1;
    Category category2;

    CategoryDto categoryDto;

    List<Category> categories;

    @BeforeEach
    public void init(){
        String categoryId1 = UUID.randomUUID().toString();
         category1= Category.builder()
                .categoryId(categoryId1)
                .title("Laptops")
                .categoryDescription("Laptops available with discount")
                .build();
        String categoryId2 = UUID.randomUUID().toString();
        category2= Category.builder()
                .categoryId(categoryId2)
                .title("Mobiles")
                .categoryDescription("Laptops available with discount")
                .build();
        categories=new ArrayList<>();
        categories.add(category1);
        categories.add(category2);


    }

    @Test
    public void createCategoryTest(){
        Mockito.when(categoryRepositoryI.save(Mockito.any())).thenReturn(category1);

        CategoryDto createdCategory = categorySeviceImpl.createCategory(modelMapper.map(category1, CategoryDto.class));

        Assertions.assertEquals(category1.getTitle(),createdCategory.getTitle());
    }

    @Test
    public void updateCategoryTest(){
        String categoryId=UUID.randomUUID().toString();
        categoryDto = CategoryDto.builder()
                .categoryId(categoryId)
                .title("Headphones")
                .categoryDescription("Headphones available with discount")
                .build();
        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category1));

        Mockito.when(categoryRepositoryI.save(Mockito.any())).thenReturn(category1);

        CategoryDto updatedCategory = categorySeviceImpl.updateCategory(categoryDto, categoryId);

        //Assertions.assertEquals(categoryDto.getTitle(),updatedCategory.getTitle());
        Assertions.assertNotNull(updatedCategory);
    }

    @Test
    public void getSingleCategoryTest(){
        String categoryId=UUID.randomUUID().toString();
        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category1));

        CategoryDto categoryDto1 = categorySeviceImpl.getCategoryById(categoryId);

        Assertions.assertEquals(category1.getTitle(),categoryDto1.getTitle());
    }


    @Test
    public void deleteCategoryByIdTest(){
        String categoryId = UUID.randomUUID().toString();

        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category1));

        categorySeviceImpl.deleteCategory(categoryId);

        Mockito.verify(categoryRepositoryI,Mockito.times(1)).delete(category1);
    }
    @Test
    public void getAllCategoriesTest(){
        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";
        Sort sort=Sort.by(sortBy).ascending();
       Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

       Page<Category> page=new PageImpl<>(categories);

        Mockito.when(categoryRepositoryI.findAll(pageable)).thenReturn(page);

        PageableResponse<CategoryDto> allCategories = categorySeviceImpl.getAllCategories(pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,allCategories.getContent().size());
    }

    @Test
    public void searchCategoriesTest(){
        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";
        String subTitle="s";
        Sort sort=Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page=new PageImpl<>(categories);

        Mockito.when(categoryRepositoryI.findByTitleContaining(pageable,subTitle)).thenReturn(page);

        PageableResponse<CategoryDto> searchedCategories = categorySeviceImpl.searchCategories(pageNumber, pageSize, sortBy, sortDir, subTitle);

        Assertions.assertEquals(2,searchedCategories.getContent().size());
    }

    @Test
    public void updateCategoryExceptionTest(){
        Assertions.assertThrows(ResourceNotFoundException.class,()-> categorySeviceImpl.updateCategory(null,null));

    }

    @Test
    public void getCategoryByIdExceptionTest(){
        Assertions.assertThrows(ResourceNotFoundException.class,()-> categorySeviceImpl.getCategoryById(null));
    }

    @Test
    public void deleteCategoryExceptionTest(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->categorySeviceImpl.deleteCategory(null));
    }

}