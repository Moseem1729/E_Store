package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.ApiResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    //create

    /**
     * @Author Moseem Pathan
     * @param userDto
     * @return ResponseEntity<UserDto>
     */
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        logger.info("Creating new user: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        logger.info("User created successfully: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    //update

    /**
     * @Author Moseem Pathan
     * @param userDto
     * @param userId
     * @return ResponseEntity<UserDto>
     */
    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId){
        logger.info("Updating user with ID: {}", userId);
        UserDto updatedUser = userService.updateUser(userDto, userId);
        logger.info("User updated successfully: {}", updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //delete
    /**
     * @Author Moseem Pathan
     * @param userId
     * @return ResponseEntity<String>
     */
    @DeleteMapping(value = "/delete/{userId}")
    public  ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId){
        logger.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        logger.info("User deleted successfully with ID: {}", userId);
        return new ResponseEntity<>(new ApiResponse(AppConstants.DELETE_USER, true), HttpStatus.OK);
    }

    //get all
    /**
     * @Author Moseem Pathan
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<UserDto>> getAllUser(){
        logger.info("Retrieving all users");
        List<UserDto> allUser = userService.getAllUser();
        logger.info("Successfully retrieved all users");
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    //get single

    /**
     * @Author Moseem Pathan
     * @param userId
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/get/{userId}")
    public  ResponseEntity<UserDto> getUser(@PathVariable String userId){
        logger.info("Retrieving user with ID: {}", userId);
        UserDto user = userService.getUserById(userId);
        logger.info("Successfully retrieved user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get user by email

    /**
     * @Author Moseem Pathan
     * @param email
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/byEmail/{email}")
    public  ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        logger.info("Retrieving user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        logger.info("Successfully retrieved user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get users by search

    /**
     * @Author Moseem Pathan
     * @param keyword
     * @return ResponseEntity<List<UserDto>>
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserContaining(@PathVariable String keyword){
        logger.info("Searching for users containing keyword: {}", keyword);
        List<UserDto> dtoList = userService.searchUser(keyword);
        logger.info("Successfully retrieved users matching the search");
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

}
