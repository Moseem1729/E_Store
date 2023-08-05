package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private ModelMapper mapper;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    User user;
    User user1;

    List<User> list = new ArrayList<>();

    @BeforeEach
    public void init(){


        user = User.builder()
                .name("Durgesh")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("abc.png")
                .password("lcwd")
                .build();

        user1 = User.builder()
                .name("Moseem")
                .email("moseem@gmail.com")
                .about("This is testing create method 2")
                .gender("male")
                .imageName("xyz.png")
                .password("asj")
                .build();

        list.add(user);
        list.add(user1);

    }

    //create user
    @Test
    public void createUserTest(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));

        System.out.println(user1.getName());

        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Durgesh", user1.getName());

    }

    //update user test
    @Test
    public void updateUserTest(){

        String userId = "anyUserId";
        UserDto userDto = UserDto.builder()
                .name("Moseem Vajeer Pathan")
                .about("This is updated user about details")
                .gender("Male")
                .imageName("xyz.png")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto, userId);

        System.out.println(updatedUser.getName());
        System.out.println(updatedUser.getImageName());

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(userDto.getName(), updatedUser.getName(), "Name is not updated !!");
        //Multiple assertions are valid
    }

    @Test
    void deleteUserTest() throws IOException {

        String userId = UUID.randomUUID().toString();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
    }

    @Test
    public void getUserByIdTest(){
        String userId = UUID.randomUUID().toString();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto user1 = userService.getUserById(userId);
        Assertions.assertNotNull(user1);
        System.out.println(user1.getUserId());
        Assertions.assertEquals("lcwd", user1.getPassword(), "test failed getting user by Id !!");

    }


    @Test
    public void getUserByEmailTest(){

        String email = "durgesh@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDto user1 = userService.getUserByEmail(email);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("lcwd", user1.getPassword());

    }

    @Test
    public void searchUserTest(){
        String keyword = "Du";
        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(list);
        System.out.println(list.get(0));
        List<UserDto> dtoList = userService.searchUser(keyword);
        Assertions.assertNotNull(dtoList);

    }

    @Test
    public void getAllUserTest(){
        int pageNumber = 0; int pageSize=2; String sortBy="name"; String sortDir="asc";
        Sort sort = sortDir.equalsIgnoreCase(AppConstants.SORT_DIR_ASC_CHECK) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = new PageImpl<>(list, pageable, list.size());

        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);
        PageableResponse<UserDto> pageableResponse = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(pageableResponse);
//        List<UserDto> userDtoList = list.stream().map((user) -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
//        Assertions.assertEquals(userDtoList.get(0),pageableResponse.getContent().get(0));
    }


}
