package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;



    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;


    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        logger.info("Creating new category:{}", categoryDto.getTitle());
        String catId = UUID.randomUUID().toString();
        logger.info("Random id generated for category:{}", catId);
        categoryDto.setCategoryId(catId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        logger.info("Category created successfully:{}",category.getTitle());
        return mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        logger.info("Updating Category with id: {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));
        //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription((categoryDto.getDescription()));
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);
        logger.info("Category updated successfully:{}", updatedCategory.getTitle());

        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        logger.info("Deleting category with id: {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));

        String coverImage = category.getCoverImage();
        String fullPath = imageUploadPath + coverImage;

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
            logger.info("related cover image deleted: {}", path);
        } catch (NoSuchFileException ex) {
            logger.error("User image not found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            logger.error("An error occurred while deleting the cover image: {}", e.getMessage());
            e.printStackTrace();
        }

        categoryRepository.delete(category);
        logger.info("Category deleted successfully:{}", category.getTitle());
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        logger.info("Getting all categories");
        Sort sort = (sortDir.equalsIgnoreCase(AppConstants.SORT_DIR_ASC_CHECK)
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);

        logger.info("Got all categories");
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        logger.info("Fetching category with id: {}", categoryId);
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));

        logger.info("Fetched category:{}", category.getTitle());
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> search(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {

        logger.info("Searching categories with title containing keyword: {} ",keyword);
        Sort sort = (sortDir.equalsIgnoreCase(AppConstants.SORT_DIR_ASC_CHECK) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> page = categoryRepository.findByTitleContaining(keyword, pageable);

        logger.info("Categories retrieved containing keyword:{}", keyword);
        return Helper.getPageableResponse(page, CategoryDto.class);
    }
}
