package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.payload.ApiResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * @author Moseem Pathan
     * @apiNote create user
     * @param userDto
     * @return ResponseEntity<UserDto>
     */
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        logger.info("Sending request to service for creating new user: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        logger.info("User created successfully in database: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    //update

    /**
     * @author Moseem Pathan
     * @apiNote update user
     * @param userDto
     * @param userId
     * @return ResponseEntity<UserDto>
     */
    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId){
        logger.info("Sending request to service for updating user with ID: {}", userId);
        UserDto updatedUser = userService.updateUser(userDto, userId);
        logger.info("User updated successfully: {}", updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //delete
    /**
     * @author Moseem Pathan
     * @apiNote delete user with given id
     * @param userId
     * @return ResponseEntity<String>
     */
    @DeleteMapping(value = "/delete/{userId}")
    public  ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId){
        logger.info("Sending request to service for deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        logger.info("User deleted successfully with ID: {}", userId);
        return new ResponseEntity<>(new ApiResponse(AppConstants.DELETE_USER, HttpStatus.OK, true), HttpStatus.OK);
    }

    //get all
    /**
     * @author Moseem Pathan
     * @apiNote get all users
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<UserDto>> getAllUser(){
        logger.info("Sending request to service for retrieving all users");
        List<UserDto> allUser = userService.getAllUser();
        logger.info("Successfully retrieved all users");
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    //get single

    /**
     * @author Moseem Pathan
     * @apiNote get user by id
     * @param userId
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/get/{userId}")
    public  ResponseEntity<UserDto> getUser(@PathVariable String userId){
        logger.info("Sending request to service for retrieving user with ID: {}", userId);
        UserDto user = userService.getUserById(userId);
        logger.info("Successfully retrieved user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get user by email

    /**
     * @author Moseem Pathan
     * @apiNote get user by email
     * @param email
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/byEmail/{email}")
    public  ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        logger.info("Sending request to service for retrieving user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        logger.info("Successfully retrieved user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get users by search

    /**
     * @author Moseem Pathan
     * @apiNote get user containing keyword
     * @param keyword
     * @return ResponseEntity<List<UserDto>>
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserContaining(@PathVariable String keyword){
        logger.info("Sending request to service for searching for users containing keyword: {}", keyword);
        List<UserDto> dtoList = userService.searchUser(keyword);
        logger.info("Successfully retrieved users matching the search");
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

}
