package com.f1tekkz.springrestfulapi.repository;

import com.f1tekkz.springrestfulapi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
}
