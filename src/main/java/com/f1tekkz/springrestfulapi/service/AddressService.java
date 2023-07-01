package com.f1tekkz.springrestfulapi.service;

import com.f1tekkz.springrestfulapi.entity.Address;
import com.f1tekkz.springrestfulapi.entity.Contact;
import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.AddressResponse;
import com.f1tekkz.springrestfulapi.model.CreateAddressRequest;
import com.f1tekkz.springrestfulapi.model.WebResponse;
import com.f1tekkz.springrestfulapi.repository.AddressRepository;
import com.f1tekkz.springrestfulapi.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setContact(contact);
        address.setId(UUID.randomUUID().toString());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

//        addressRepository.save(address);

        return toAddressResponse(address);
    }

    private AddressResponse toAddressResponse(Address address){
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }
}
