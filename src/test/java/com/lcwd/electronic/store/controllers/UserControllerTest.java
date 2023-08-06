package com.lcwd.electronic.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileService fileService;

    User user;

    @BeforeEach
    public void setup() {
        String id = UUID.randomUUID().toString();

        user = User.builder()
                .userId(id)
                .name("Moseem")
                .email("moseem@gmail.com")
                .password("12345")
                .gender("Male")
                .about("I am Developer")
                .imageName("aa.png")
                .build();

    }

    @Test
    void createUserTest() throws Exception {

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);

        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/create/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
                //.andExpect((ResultMatcher) jsonPath("$.name").exists());

    }

    private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    void updateUserTest() throws Exception {

        String userId = UUID.randomUUID().toString();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.updateUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void deleteUserTest() throws Exception {
        String userId = UUID.randomUUID().toString();
        UserDto userDto = modelMapper.map(user, UserDto.class);


        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getAllUsersTest() throws Exception {

        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Moseem")
                .about("I am an java developer")
                .password("123")
                .email("moseem@gmail.com")
                .imageName("ak.png").build();

        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("cj")
                .about("developer")
                .password("345gd")
                .email("cj@gmail.com")
                .imageName("123.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Yasho")
                .about("I am Ironman")
                .password("123asd")
                .email("yashodeep@gmail.com")
                .imageName("abc.png").build();


        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();

        pageableResponse.setContent(Arrays.asList(userDto1, userDto2, userDto3));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(2);
        pageableResponse.setTotalElements(50L);
        pageableResponse.setTotalPages(100);
        pageableResponse.setLastPage(false);

        Mockito.when(userService.getAllUser(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void getUserByIdTest() throws Exception {

        String userId = UUID.randomUUID().toString();

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$.name").exists());
    }

    @Test
    void getUserByEmailTest() throws Exception {

        String email = "sahil@gmail.com";

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userService.getUserByEmail(email)).thenReturn(userDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/email/" + email)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //.andExpect(jsonPath("$.email").exists());

    }

    @Test
    void getUserByKeywordTest() throws Exception {
        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Moseem")
                .about("I am an java developer")
                .password("123")
                .email("moseem@gmail.com")
                .imageName("ak.png").build();

        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("cj")
                .about("developer")
                .password("345gd")
                .email("cj@gmail.com")
                .imageName("123.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Yasho")
                .about("I am Ironman")
                .password("123asd")
                .email("yashodeep@gmail.com")
                .imageName("abc.png").build();

        List list = new ArrayList();
        list.add(userDto1);
        list.add(userDto2);
        list.add(userDto3);

        String keyword = "a";

        Mockito.when(userService.searchUser(Mockito.anyString())).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search/" + keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void uploadUserImageTest() throws Exception {
        String fileName = "abb.png";
        String filePath = "image/users";
        String userId = UUID.randomUUID().toString();

        Mockito.when(fileService.uploadImage(Mockito.any(), Mockito.anyString())).thenReturn(fileName);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(userDto);
        userDto.setImageName(fileName);

        Mockito.when(userService.updateUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/image/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void serveUserImageTest() throws Exception {
        String imagePath = "image/users/";
        String userId = UUID.randomUUID().toString();

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        InputStream resource = new FileInputStream(userDto.getImageName());
        Mockito.when(fileService.getResource(Mockito.anyString(), Mockito.anyString())).thenReturn(resource);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/image/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();


    }
}
