package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

public class CategoryServiceTest {

    @Autowired
    private ModelMapper mapper;
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    Category category;

    @BeforeEach
    public void init(){


        category = Category.builder()
                .title("Mobiles")
                .description("Mobiles are from all top brands")
                .coverImage("abc.png")
                .build();

    }

    //create user
    @Test
    public void createUserTest(){
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto categoryDto1 = categoryService.create(mapper.map(category, CategoryDto.class));

        System.out.println(categoryDto1.getTitle());

        Assertions.assertNotNull(categoryDto1);
        Assertions.assertEquals("Mobiles", categoryDto1.getTitle());

    }

    //update user test
    @Test
    public void updateUserTest(){

        String userId = "anyUserId";
        CategoryDto categoryDto = CategoryDto.builder()
                .title("Mobiles")
                .description("Mobiles are from all top brands")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto updatedCategory = categoryService.update(categoryDto, userId);

        System.out.println(updatedCategory.getTitle());
        System.out.println(updatedCategory.getCoverImage());

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertEquals(categoryDto.getTitle(), updatedCategory.getTitle(), "Title is not updated !!");
        //Multiple assertions are valid
    }
}
