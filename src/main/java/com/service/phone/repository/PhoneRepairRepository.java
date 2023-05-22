package com.service.phone.repository;

import com.service.phone.model.for_phone.PhoneRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PhoneRepairRepository extends JpaRepository<PhoneRepair, Long> {

    Optional<Set<PhoneRepair>> findByEngineerUsername(String username);
    Optional<Set<PhoneRepair>> findByPhoneRepairRequestCustomerUsername(String username);

}
