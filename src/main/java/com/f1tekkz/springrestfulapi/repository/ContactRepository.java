package com.f1tekkz.springrestfulapi.repository;

import com.f1tekkz.springrestfulapi.entity.Contact;
import com.f1tekkz.springrestfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    Optional<Contact> findFirstByUserAndId(User user, String id);
}
