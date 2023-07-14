package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.payload.ApiResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.payload.ImageResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create

    /**
     * @param userDto
     * @return ResponseEntity<UserDto>
     * @author Moseem Pathan
     * @apiNote create user
     */
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        logger.info("Sending request to service for creating new user: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        logger.info("User created successfully in database: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    //update

    /**
     * @param userDto
     * @param userId
     * @return ResponseEntity<UserDto>
     * @author Moseem Pathan
     * @apiNote update user
     */
    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        logger.info("Sending request to service for updating user with ID: {}", userId);
        UserDto updatedUser = userService.updateUser(userDto, userId);
        logger.info("User updated successfully: {}", updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //delete

    /**
     * @param userId
     * @return ResponseEntity<String>
     * @author Moseem Pathan
     * @apiNote delete user with given id
     */
    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) throws IOException {
        logger.info("Sending request to service for deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        logger.info("User deleted successfully with ID: {}", userId);
        return new ResponseEntity<>(new ApiResponse(AppConstants.DELETE_USER, HttpStatus.OK, true), HttpStatus.OK);
    }

    //get all

    /**
     * @return ResponseEntity<UserDto>
     * @author Moseem Pathan
     * @apiNote get all users
     */
    @GetMapping("/getAll")
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        logger.info("Sending request to service for retrieving all users");
        PageableResponse<UserDto> allUser = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Successfully retrieved all users");
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    //get single

    /**
     * @param userId
     * @return ResponseEntity<UserDto>
     * @author Moseem Pathan
     * @apiNote get user by id
     */
    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        logger.info("Sending request to service for retrieving user with ID: {}", userId);
        UserDto user = userService.getUserById(userId);
        logger.info("Successfully retrieved user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get user by email

    /**
     * @param email
     * @return ResponseEntity<UserDto>
     * @author Moseem Pathan
     * @apiNote get user by email
     */
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        logger.info("Sending request to service for retrieving user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        logger.info("Successfully retrieved user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get users by search

    /**
     * @param keyword
     * @return ResponseEntity<List < UserDto>>
     * @author Moseem Pathan
     * @apiNote get user containing keyword
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserContaining(@PathVariable String keyword) {
        logger.info("Sending request to service for searching for users containing keyword: {}", keyword);
        List<UserDto> dtoList = userService.searchUser(keyword);
        logger.info("Successfully retrieved users matching the search");
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable String userId
    ) throws IOException {

        String imageName = fileService.uploadImage(image, imageUploadPath);

        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);

        ImageResponse response = ImageResponse.builder()
                .imageName(imageName)
                .message("Image uploaded successfully !!")
                .httpStatus(HttpStatus.CREATED)
                .success(true)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    //serve image
    @GetMapping(value = "/image/{userId}")
    public void serveUserImage(
            @PathVariable String userId,
            HttpServletResponse response
    ) throws IOException {

        UserDto userDto = userService.getUserById(userId);
        logger.info("user image name ; {}" , userDto.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, userDto.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

}
