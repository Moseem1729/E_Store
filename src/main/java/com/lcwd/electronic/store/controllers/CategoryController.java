package com.lcwd.electronic.store.controllers;

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

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto createdCategory = categoryService.create(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable("categoryId") String catId) {

        CategoryDto updatedCategory = categoryService.update(categoryDto, catId);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String catId) {

        categoryService.delete(catId);
        return new ResponseEntity<>(
                new ApiResponse("Category deleted successfully !!", HttpStatus.OK, true),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/getAllPageable")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllPageable(
            @RequestParam(defaultValue = "0", required = false) int pageNO,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "title", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNO, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/get/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable String catId) {
        CategoryDto categoryDto = categoryService.get(catId);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<CategoryDto>> getByKeyword(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "0", required = false) int pageNO,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "title", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.search(keyword, pageNO, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //upload categoryCover image
    @PostMapping("/coverImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCatCoverImage(
            @RequestParam("categoryCoverImage") MultipartFile image,
            @PathVariable String categoryId
    ) throws IOException {

        String coverImageName = fileService.uploadImage(image, imageUploadPath);
        logger.info("updated coverImageName this is categoryController: {}", coverImageName);

        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("old image name: {}", categoryDto.getCoverImage());
        categoryDto.setCoverImage(coverImageName);
        CategoryDto categoryDto1 = categoryService.update(categoryDto, categoryId);
        logger.info("new image name: {}", categoryDto1.getCoverImage());

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(coverImageName)
                .message("Image uploaded successfully !!")
                .httpStatus(HttpStatus.CREATED)
                .success(true)
                .build();
        return  new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    //serve coverImage
    @GetMapping(value = "/coverImage/{categoryId}")
    public void serveCoverImage(
            @PathVariable String categoryId,
            HttpServletResponse response
    ) throws IOException {

        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("category image name ; {}" , categoryDto.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

}
