package com.f1tekkz.springrestfulapi.controller;

import com.f1tekkz.springrestfulapi.entity.Contact;
import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.ContactResponse;
import com.f1tekkz.springrestfulapi.model.CreateContactRequest;
import com.f1tekkz.springrestfulapi.model.UpdateContactRequest;
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

import java.util.List;
import java.util.UUID;

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
        contactRepository.deleteAll(); // hapus child (CONTACT) terlebih dahulu, baru setelah itu bisa hapus parent (USER)
        userRepository.deleteAll();

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
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
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
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("Afwan", response.getData().getFirstName());
            assertEquals("Zikri", response.getData().getLastName());
            assertEquals("iniEmail@gmail.com", response.getData().getEmail());
            assertEquals("081266005092", response.getData().getPhone());
//            assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }

    /*
     * -- Unit test GET Contact by id API --
     */
    @Test
    void getContactNotFound() throws Exception{
        mockMvc.perform(
                get("/api/contacts/12354353")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactSuccess() throws Exception{
        User user = userRepository.findById("f1tekkz").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Afwan");
        contact.setLastName("Zikri");
        contact.setEmail("iniEmail@gmail.com");
        contact.setPhone("081266005092");
        contactRepository.save(contact); // karena method utk UnitTest dan tidak menggunakan annotation @Transactional, maka dibutuhkan contactRepository.save(contact)

        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());

            assertEquals(contact.getId(), response.getData().getId());
            assertEquals(contact.getFirstName(), response.getData().getFirstName());
            assertEquals(contact.getLastName(), response.getData().getLastName());
            assertEquals(contact.getEmail(), response.getData().getEmail());
            assertEquals(contact.getPhone(), response.getData().getPhone());
        });
    }

    /*
     * -- Unit test Update-Contact API --
     */
    @Test
    void updateContactBadRequest() throws Exception{
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("iniEmail@gmail.com");

        mockMvc.perform(
                put("/api/contacts/1242540")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateContactSuccess() throws Exception{
        User user = userRepository.findById("f1tekkz").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Afwan");
        contact.setLastName("Zikri");
        contact.setEmail("iniEmail@gmail.com");
        contact.setPhone("081266005092");
        contactRepository.save(contact); // karena method utk UnitTest dan tidak menggunakan annotation @Transactional, maka dibutuhkan contactRepository.save(contact)

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Alan Anandito");
        request.setLastName("Zikri");
        request.setEmail("alanAZ@gmail.com");
        request.setPhone("085736060036");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(request.getFirstName(), response.getData().getFirstName());
            assertEquals(request.getLastName(), response.getData().getLastName());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertEquals(request.getPhone(), response.getData().getPhone());

            assertTrue(contactRepository.existsById(response.getData().getId()));

        });
    }

    /*
     * -- Unit test DELETE-Contact API --
     */
    @Test
    void deleteContactNotFound() throws Exception{
        mockMvc.perform(
                delete("/api/contacts/12354353")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteContactSuccess() throws Exception{
        User user = userRepository.findById("f1tekkz").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Afwan");
        contact.setLastName("Zikri");
        contact.setEmail("iniEmail@gmail.com");
        contact.setPhone("081266005092");
        contactRepository.save(contact); // karena method utk UnitTest dan tidak menggunakan annotation @Transactional, maka dibutuhkan contactRepository.save(contact)

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("OK", response.getData());
            assertFalse(contactRepository.existsById(contact.getId()));
        });
    }

    /*
     * -- Unit test GET Search-contact API --
     */
    @Test
    void searchContactNotFound() throws Exception{
        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());
        });
    }

    @Test
    void searchContactSuccess() throws Exception{
        User user = userRepository.findById("f1tekkz").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setUser(user);
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstName("Afwan" + i);
            contact.setLastName("Zikri");
            contact.setEmail("iniEmail@gmail.com");
            contact.setPhone("081266005092");
            contactRepository.save(contact); // karena method utk UnitTest dan tidak menggunakan annotation @Transactional, maka dibutuhkan contactRepository.save(contact)
        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Afwan")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Zikri")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "gmail.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "081266005092")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "081266005092")
                        .queryParam("page", "1000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(1000, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());
        });
    }
}