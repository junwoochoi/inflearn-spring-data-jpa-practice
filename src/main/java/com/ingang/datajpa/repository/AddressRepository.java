package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
