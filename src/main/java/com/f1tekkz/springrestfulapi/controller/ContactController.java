package com.f1tekkz.springrestfulapi.controller;

import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.ContactResponse;
import com.f1tekkz.springrestfulapi.model.CreateContactRequest;
import com.f1tekkz.springrestfulapi.model.WebResponse;
import com.f1tekkz.springrestfulapi.service.ContactService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }
}
