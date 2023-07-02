package com.f1tekkz.springrestfulapi.controller;

import com.f1tekkz.springrestfulapi.entity.Address;
import com.f1tekkz.springrestfulapi.entity.Contact;
import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.AddressResponse;
import com.f1tekkz.springrestfulapi.model.CreateAddressRequest;
import com.f1tekkz.springrestfulapi.model.UpdateAddressRequest;
import com.f1tekkz.springrestfulapi.model.WebResponse;
import com.f1tekkz.springrestfulapi.repository.AddressRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("f1tekkz");
        user.setPassword(BCrypt.hashpw("iniPswd", BCrypt.gensalt()));
        user.setName("AfwanZ");
        user.setToken("kkshdf9ysuierbwejhrbdaiu9823");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user); // karena method utk UnitTest dan tidak menggunakan annotation @Transactional, maka dibutuhkan contactRepository.save(contact)

        Contact contact = new Contact();
        contact.setUser(user);
//        contact.setId(UUID.randomUUID().toString());
        contact.setId("12425345");
        contact.setFirstName("Afwan");
        contact.setLastName("Zikri");
        contact.setEmail("iniEmail@gmail.com");
        contact.setPhone("081266005092");
        contactRepository.save(contact); // karena method utk UnitTest dan tidak menggunakan annotation @Transactional, maka dibutuhkan contactRepository.save(contact)
    }

    /*
     * -- Unit test Create-Address API --
     */
    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Jl. H. Rean");
        request.setCity("Tangerang Selatan");
        request.setProvince("Banten");
        request.setCountry("");
        request.setPostalCode("15415");

        mockMvc.perform(
                post("/api/contacts/12425345/addresses")
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
    void createAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Jl. H. Rean");
        request.setCity("Tangerang Selatan");
        request.setProvince("Banten");
        request.setCountry("Indonesia");
        request.setPostalCode("15415");

        mockMvc.perform(
                post("/api/contacts/12425345/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());

            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    /*
     * -- Unit test GET Address API --
     */

    @Test
    void getAddressButContactNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/99999/addresses/987654")
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
    void getAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/12425345/addresses/987654")
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
    void getAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("12425345").orElseThrow();

        Address address = new Address();
        address.setId("987654");
        address.setStreet("Jl. H. Rean, Namara Residence");
        address.setCity("Tangerang Selatan");
        address.setProvince("Banten");
        address.setCountry("Indonesia");
        address.setPostalCode("15415");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/12425345/addresses/987654")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(address.getId(), response.getData().getId());
            assertEquals(address.getStreet(), response.getData().getStreet());
            assertEquals(address.getCity(), response.getData().getCity());
            assertEquals(address.getProvince(), response.getData().getProvince());
            assertEquals(address.getCountry(), response.getData().getCountry());
            assertEquals(address.getPostalCode(), response.getData().getPostalCode());
        });
    }

    /*
     * -- Unit test Update-Address API --
     */
    @Test
    void updateAddressBadRequest() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jl. H. Rean");
        request.setCity("Tangerang Selatan");
        request.setProvince("Banten");
        request.setCountry(""); // should NotBlank
        request.setPostalCode("15415");

        mockMvc.perform(
                put("/api/contacts/888888/addresses/999999")
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
    void updateAddressButContactNotFound() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jl. H. Rean");
        request.setCity("Tangerang Selatan");
        request.setProvince("Banten");
        request.setCountry("Indonesia"); // should NotBlank
        request.setPostalCode("15415");

        mockMvc.perform(
                put("/api/contacts/888888/addresses/999999")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
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
    void updateAddressNotFound() throws Exception {
        Contact contact = contactRepository.findById("12425345").orElseThrow();

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jl. H. Rean");
        request.setCity("Tangerang Selatan");
        request.setProvince("Banten");
        request.setCountry("Indonesia"); // should NotBlank
        request.setPostalCode("15415");

        mockMvc.perform(
                put("/api/contacts/12425345/addresses/999999")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
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
    void updateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("12425345").orElseThrow();

        Address address = new Address();
        address.setId("999999");
        address.setStreet("Jl. Rajawali Selatan 2 No. 1B");
        address.setCity("Jakarta Pusat");
        address.setProvince("DKI Jakarta");
        address.setCountry("Indonesia");
        address.setPostalCode("10720");
        address.setContact(contact);
        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jl. H. Rean");
        request.setCity("Tangerang Selatan");
        request.setProvince("Banten");
        request.setCountry("Indonesia"); // should NotBlank
        request.setPostalCode("15415");

        mockMvc.perform(
                put("/api/contacts/12425345/addresses/999999")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
//            assertEquals(request.getAddressId(), response.getData().getId());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    /*
     * -- Unit test DELETE Address API --
     */
    @Test
    void deleteAddressButContactNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/99999/addresses/987654")
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
    void deleteAddressNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/12425345/addresses/987654")
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
    void deleteAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("12425345").orElseThrow();

        Address address = new Address();
        address.setId("987654");
        address.setStreet("Jl. H. Rean, Namara Residence");
        address.setCity("Tangerang Selatan");
        address.setProvince("Banten");
        address.setCountry("Indonesia");
        address.setPostalCode("15415");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/12425345/addresses/987654")
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
            assertFalse(addressRepository.existsById(address.getId()));
        });
    }

    /*
     * -- Unit test GET LIST Address API --
     */
    @Test
    void getListAddressContactNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/99999/addresses")
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
    void getNoListAddressContactFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/12425345/addresses")
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
    void getListAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("12425345").orElseThrow();

        Address address = new Address();
        address.setId("987654");
        address.setStreet("Jl. H. Rean, Namara Residence");
        address.setCity("Tangerang Selatan");
        address.setProvince("Banten");
        address.setCountry("Indonesia");
        address.setPostalCode("15415");
        address.setContact(contact);
        addressRepository.save(address);

        Address address1 = new Address();
        address1.setId("999999");
        address1.setStreet("Jl. Rajawali Selatan 2 No. 1B");
        address1.setCity("Jakarta Pusat");
        address1.setProvince("DKI Jakarta");
        address1.setCountry("Indonesia");
        address1.setPostalCode("10720");
        address1.setContact(contact);
        addressRepository.save(address1);

        mockMvc.perform(
                get("/api/contacts/12425345/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertTrue(addressRepository.existsById(address.getId()));
            assertTrue(addressRepository.existsById(address1.getId()));
            assertEquals(address.getId(), response.getData().get(0).getId());
            assertEquals(address1.getId(), response.getData().get(1).getId());
        });
    }

    @Test
    void getListAddressSuccess2() throws Exception {
        Contact contact = contactRepository.findById("12425345").orElseThrow();

        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setId("id-" +i);
            address.setStreet("Jl. H. Rean, Namara Residence-" +i);
            address.setCity("Tangerang Selatan-" +i);
            address.setProvince("Banten" +i);
            address.setCountry("Indonesia" +i);
            address.setPostalCode("15415" +i);
            address.setContact(contact);
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/api/contacts/12425345/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "kkshdf9ysuierbwejhrbdaiu9823")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(5, response.getData().size());
        });
    }
}