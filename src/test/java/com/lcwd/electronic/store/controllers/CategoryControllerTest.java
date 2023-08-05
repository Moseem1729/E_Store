package com.lcwd.electronic.store.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category;

    @BeforeEach
    public void setUp() {
        String categoryId1 = UUID.randomUUID().toString();
        category = Category.builder()
                .categoryId(categoryId1)
                .title("Laptops")
                .description("Laptops available with discount")
                .coverImage("abc.png")
                .build();
    }

    @Test
    void createCategoryTest() throws Exception {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryService.create(Mockito.any())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());

    }

    private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    void updateCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();
        CategoryDto categoryDto=CategoryDto.builder()
                .categoryId(categoryId)
                .title("Mobiles")
                .description("Mobiles available with discounts")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryService.update(Mockito.any(),Mockito.anyString())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/update/" +categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists());

    }


    @Test
    void getSingleCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryService.get(categoryId)).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists());

    }

    @Test
    void deleteCategoryTest() throws Exception {
        String categoryId=UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void getAllCategoriesTest() throws Exception {
        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Headphones")
                .description("Headphones available with good quality")
                .coverImage("xyz.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("LED TVS")
                .description("LED TVS available with good quality")
                .coverImage("def.png")
                .build();
        CategoryDto categoryDto3=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Electronics")
                .description("Electronics products available with good quality")
                .coverImage("hjh.png")
                .build();
        CategoryDto categoryDto4=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("phones")
                .description("phones available with good quality")
                .coverImage("dbw.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(categoryDto1,categoryDto2,categoryDto3,categoryDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setTotalElements(1000L);
        pageableResponse.setLastPage(false);

        Mockito.when(categoryService.getAll(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void searchCategoriesTest() throws Exception {

        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Headphones")
                .description("Headphones available with good quality")
                .coverImage("xyz.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("LED TVS")
                .description("LED TVS available with good quality")
                .coverImage("def.png")
                .build();
        CategoryDto categoryDto3=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Electronics")
                .description("Electronics products available with good quality")
                .coverImage("hjh.png")
                .build();
        CategoryDto categoryDto4=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("phones")
                .description("phones available with good quality")
                .coverImage("dbw.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(categoryDto1,categoryDto2,categoryDto3,categoryDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setTotalElements(1000L);
        pageableResponse.setLastPage(false);

        String subTitle="a";

        Mockito.when(categoryService.search(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/search/"+subTitle)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
