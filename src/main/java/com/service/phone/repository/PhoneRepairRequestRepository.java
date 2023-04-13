package com.service.phone.repository;

import com.service.phone.model.for_phone.PhoneRepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepairRequestRepository extends JpaRepository<PhoneRepairRequest, Long> {
}
