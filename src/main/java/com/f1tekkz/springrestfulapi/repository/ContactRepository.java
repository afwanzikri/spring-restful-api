package com.f1tekkz.springrestfulapi.repository;

import com.f1tekkz.springrestfulapi.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
