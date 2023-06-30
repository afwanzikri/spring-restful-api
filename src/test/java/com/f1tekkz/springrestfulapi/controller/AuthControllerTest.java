package com.f1tekkz.springrestfulapi.controller;

import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.LoginUserRequest;
import com.f1tekkz.springrestfulapi.model.TokenResponse;
import com.f1tekkz.springrestfulapi.model.WebResponse;
import com.f1tekkz.springrestfulapi.repository.UserRepository;
import com.f1tekkz.springrestfulapi.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("pertama");
        request.setPassword("test");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((request)))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginFailedUserWrongPassword() throws Exception {
        User user = new User();
        user.setName("Test");
        user.setUsername("pertama");
        user.setPassword(BCrypt.hashpw("true", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("pertama");
        request.setPassword("false");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((request)))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setName("Test");
        user.setUsername("pertama");
        user.setPassword(BCrypt.hashpw("true", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("pertama");
        request.setPassword("true");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((request)))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());

            User userDB =  userRepository.findById("pertama").orElse(null);
            assertNotNull((userDB));
            assertEquals(userDB.getToken(), response.getData().getToken());
            assertEquals(userDB.getTokenExpiredAt(), response.getData().getExpiredAt());
        });
    }

}