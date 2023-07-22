package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.payload.ApiResponse;
import com.lcwd.electronic.store.payload.ImageResponse;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    /**
     *
     * @param categoryDto
     * @return ResponseEntity<CategoryDto>
     * @author Moseem Pathan
     * @apiNote create category
     */
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logger.info("Sending request to service for creating new Category: {}", categoryDto.getTitle());

        CategoryDto createdCategory = categoryService.create(categoryDto);
        logger.info("Category created successfully in database: {}", createdCategory.getTitle());

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     *
     * @param categoryDto
     * @param catId
     * @return ResponseEntity<CategoryDto>
     * @author Moseem Pathan
     * @apiNote update category
     */
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable("categoryId") String catId) {
        logger.info("Sending request to service for updating Category with Id: {}", catId);

        CategoryDto updatedCategory = categoryService.update(categoryDto, catId);
        logger.info("Category updated successfully in database: {}", updatedCategory.getTitle());

        return ResponseEntity.ok(updatedCategory);
    }

    /**
     *
     * @param catId
     * @return ResponseEntity<ApiResponse>
     * @author Moseem Pathan
     * @apiNote delete category
     */
    @DeleteMapping("/delete/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String catId) {
        logger.info("Sending request to service for deleting Category with Id: {}", catId);

        categoryService.delete(catId);
        logger.info("Category deleted successfully from database with id:{}", catId);

        return new ResponseEntity<>(
                new ApiResponse(AppConstants.DELETE_CATEGORY, HttpStatus.OK, true),
                HttpStatus.ACCEPTED);
    }

    /**
     *
     * @param pageNO
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return ResponseEntity<PageableResponse<CategoryDto>>
     * @author Moseem Pathan
     * @apiNote get all categories pageable
     */
    @GetMapping("/getAllPageable")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllPageable(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNO,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY_TITLE, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        logger.info("Sending request to service for getting all Categories");

        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNO, pageSize, sortBy, sortDir);
        logger.info("Categories retrieved successfully from database");

        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     *
     * @param catId
     * @return ResponseEntity<CategoryDto>
     * @author Moseem Pathan
     * @apiNote get category by id
     */
    @GetMapping("/get/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable String catId) {
        logger.info("Sending request to service for getting Category with Id: {}", catId);

        CategoryDto categoryDto = categoryService.get(catId);
        logger.info("Category fetched successfully: {}", categoryDto.getTitle());

        return ResponseEntity.ok(categoryDto);
    }

    /**
     *
     * @param keyword
     * @param pageNO
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return ResponseEntity<PageableResponse<CategoryDto>>
     * @author Moseem Pathan
     * @apiNote search category title by keyword
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<CategoryDto>> getByKeyword(
            @PathVariable String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNO,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY_TITLE, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        logger.info("Sending request to service for retrieve Categories by keyword: {}", keyword);

        PageableResponse<CategoryDto> pageableResponse = categoryService.search(keyword, pageNO, pageSize, sortBy, sortDir);
        logger.info("Categories retrieved successfully from database");

        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //upload categoryCover image

    /**
     *
     * @param image
     * @param categoryId
     * @return ResponseEntity<ImageResponse>
     * @throws IOException
     * @author Moseem Pathan
     * @apiNote upload cover image for category
     */
    @PostMapping("/coverImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCatCoverImage(
            @RequestParam("categoryCoverImage") MultipartFile image,
            @PathVariable String categoryId
    ) throws IOException {
        logger.info("Sending request to FileService for uploading new cover image");
        String coverImageName = fileService.uploadImage(image, imageUploadPath);
        logger.info("updated coverImageName this is categoryController: {}", coverImageName);

        logger.info("Sending request to CategoryService for retrieving Category with id: {}", categoryId);
        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("old image name: {}", categoryDto.getCoverImage());
        categoryDto.setCoverImage(coverImageName);

        logger.info("Sending request to CategoryService for updating Category with id: {}", categoryId);
        CategoryDto categoryDto1 = categoryService.update(categoryDto, categoryId);
        logger.info("new image name: {}", categoryDto1.getCoverImage());

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(coverImageName)
                .message(AppConstants.IMAGE_UPLOAD)
                .httpStatus(AppConstants.HTTP_STATUS)
                .success(AppConstants.IMAGE_UPLOAD_SUCCESS)
                .build();
        logger.info("Cover image updated successfully");

        return  new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    //serve coverImage

    /**
     *
     * @param categoryId
     * @param response
     * @throws IOException
     * @author Moseem Pathan
     * @apiNote serve cover image
     */
    @GetMapping(value = "/coverImage/{categoryId}")
    public void serveCoverImage(
            @PathVariable String categoryId,
            HttpServletResponse response
    ) throws IOException {

        logger.info("Entering serveCoverImage method");
        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("category image name ; {}" , categoryDto.getCoverImage());

        logger.info("Sending request to FileService for serving image with categoryId: {}", categoryId);
        InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        logger.info("Exiting serveCoverImage method");

    }

}
