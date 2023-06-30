package com.f1tekkz.springrestfulapi.service;

import com.f1tekkz.springrestfulapi.entity.Contact;
import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.ContactResponse;
import com.f1tekkz.springrestfulapi.model.CreateContactRequest;
import com.f1tekkz.springrestfulapi.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    public ContactRepository contactRepository;

    @Autowired
    public ValidationService validationService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request){
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
