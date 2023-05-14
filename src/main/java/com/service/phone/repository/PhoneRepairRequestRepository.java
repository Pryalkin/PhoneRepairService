package com.service.phone.repository;

import com.service.phone.model.for_phone.PhoneRepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PhoneRepairRequestRepository extends JpaRepository<PhoneRepairRequest, Long> {

    Optional<PhoneRepairRequest> findByPhoneNumber(String phoneNumber);
    Set<PhoneRepairRequest> findByCustomerUsername(String username);
    Optional<PhoneRepairRequest> findByIdApp(String idApp);

}
