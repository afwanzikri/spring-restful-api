package com.f1tekkz.springrestfulapi.controller;

import com.f1tekkz.springrestfulapi.entity.Contact;
import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.ContactResponse;
import com.f1tekkz.springrestfulapi.model.CreateContactRequest;
import com.f1tekkz.springrestfulapi.model.WebResponse;
import com.f1tekkz.springrestfulapi.repository.ContactRepository;
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
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        contactRepository.deleteAll();

        User user = new User();
        user.setUsername("f1tekkz");
        user.setPassword(BCrypt.hashpw("iniPswd", BCrypt.gensalt()));
        user.setName("AfwanZ");
        user.setToken("kkshdf9ysuierbwejhrbdaiu9823");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user);
    }

    /*
     * -- Unit test POST Create Contact API --
     */
    @Test
    void createContactBadRequest() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("iniEmail@gmail.com");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactSuccess() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Afwan");
        request.setLastName("Zikri");
        request.setEmail("iniEmail@gmail.com");
        request.setPhone("081266005092");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });

            assertNull(response.getErrors());
            assertEquals("Afwan", response.getData().getFirstName());
            assertEquals("Zikri", response.getData().getLastName());
            assertEquals("iniEmail@gmail.com", response.getData().getEmail());
            assertEquals("081266005092", response.getData().getPhone());

            assertTrue(contactRepository.existsById(response.getData().getId()));

        });
    }

}