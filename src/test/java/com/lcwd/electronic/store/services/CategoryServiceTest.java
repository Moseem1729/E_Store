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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private ModelMapper mapper;
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    Category category;
    Category category1;

    CategoryDto categoryDto;

    List<Category> categories;


    @BeforeEach
    public void init(){


        //String id = UUID.randomUUID().toString();
        category = Category.builder()
                //.categoryId(id)
                .title("Laptops")
                .description("Laptops are from all top brands")
                .coverImage("abc.png")
                .build();

        //String id2 = UUID.randomUUID().toString();
        category1 = Category.builder()
                //.categoryId(id2)
                .title("Mobiles")
                .description("Mobiles are from all top brands")
                .coverImage("abc.png")
                .build();

    }

    //create user
    @Test
    public void createTest(){
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto categoryDto1 = categoryService.create(mapper.map(category, CategoryDto.class));

        System.out.println(categoryDto1.getTitle());

        Assertions.assertNotNull(categoryDto1);
        Assertions.assertEquals("Laptops", categoryDto1.getTitle());

    }

    //update user test
    @Test
    public void updateTest(){

        String categoryId = "anyCategoryId";
        categoryDto = CategoryDto.builder()
                .title("Mobiles")
                .description("Mobiles are from all top brands")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);

        System.out.println(updatedCategory.getTitle());
        System.out.println(updatedCategory.getCoverImage());

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertEquals(categoryDto.getTitle(), updatedCategory.getTitle(), "Title is not updated !!");
        //Multiple assertions are valid
    }


}
