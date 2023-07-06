package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.controllers.UserController;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
//        dto->entity
        User user = mapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
//        dto->entity
        UserDto newDto = mapper.map(user, UserDto.class);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(AppConstants.USER_NOT_FOUND + userId));
        user.setName(userDto.getName());

        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);
        UserDto updatedDto = mapper.map(updatedUser, UserDto.class);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(AppConstants.USER_NOT_FOUND + userId));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoList = users.stream().map((user) -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(AppConstants.USER_NOT_FOUND + userId));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstants.USER_NOT_FOUND + email));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map((user) -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    private User dtoToEntity(UserDto userDto) {

//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();

        return  mapper.map(userDto, User.class);
    }

    private UserDto entityToDto(User user) {

//        UserDto userDto = UserDto.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .about(user.getAbout())
//                .gender(user.getGender())
//                .imageName(user.getImageName())
//                .build();

        return mapper.map(user, UserDto.class);
    }
}
