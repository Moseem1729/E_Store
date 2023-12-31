package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Override
    public UserDto createUser( UserDto userDto) {

        logger.info("Sending request to repository for creating user: {}", userDto.getName());
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user = mapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);

        UserDto newDto = mapper.map(user, UserDto.class);
        logger.info("Sending response to controller of user created successfully: {}", savedUser.getName());
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        logger.info("Sending request to repository for updating user: {} with ID: {}", userDto.getName(), userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        user.setName(userDto.getName());

        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);
        System.err.println(updatedUser.getName());

        UserDto updatedDto = mapper.map(updatedUser, UserDto.class);
        logger.info("Sending response to controller of user updated successfully: {} with ID: {}", updatedUser.getName(), userId);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId){
        logger.info("Sending request to repository for deleting user with Id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));

        //delete user profile image
//        images/users/abc.png
        String imageName = user.getImageName();
        String fullPath = imagePath + imageName;


        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("User image not found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //delete user
        userRepository.delete(user);
        logger.info("Sending response to controller of user deleted successfully with Id: {}", userId);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        logger.info("Sending request to repository for retrieving all users");
        Sort sort = sortDir.equalsIgnoreCase(AppConstants.SORT_DIR_ASC_CHECK) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> userList = page.getContent();

        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
//        List<UserDto> dtoList = userList.stream().map((user) -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
//        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
//        pageableResponse.setContent(dtoList);
//        pageableResponse.setPageNumber(page.getNumber());
//        pageableResponse.setPageSize(page.getSize());
//        pageableResponse.setTotalElements(page.getTotalElements());
//        pageableResponse.setTotalPages(page.getTotalPages());
//        pageableResponse.setLastPage(page.isLast());

        logger.info("Sending response to controller of successfully retrieved all users");

        return pageableResponse;
    }

    @Override
    public UserDto getUserById(String userId) {
        logger.info("Sending request to repository for retrieving user by Id: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));

        UserDto userDto = mapper.map(user, UserDto.class);
        logger.info("Sending response to controller of successfully retrieved user: {}", user);

        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        logger.info("Sending request to repository for retrieving user by email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + email));
        logger.info("Sending response to controller of successfully retrieved user: {}", user.getName());

        return mapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        logger.info("Sending request to repository for searching for users containing keyword: {}", keyword);

        List<User> users = userRepository.findByNameContaining(keyword);
        logger.info("Sending response to controller of successfully retrieved users matching the search");

        List<UserDto> dtoList = users.stream().map((user) -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
        logger.info("Sending response to controller of successfully retrieved users matching the search");

        return dtoList;
    }

//    private User dtoToEntity(UserDto userDto) {
//
//          User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
//
//        return  mapper.map(userDto, User.class);
//    }

//    private UserDto entityToDto(User user) {
//
//        UserDto userDto = UserDto.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .about(user.getAbout())
//                .gender(user.getGender())
//                .imageName(user.getImageName())
//                .build();
//
//        return mapper.map(user, UserDto.class);
//    }
}
