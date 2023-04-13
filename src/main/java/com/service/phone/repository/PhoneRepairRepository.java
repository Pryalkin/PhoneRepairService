package com.service.phone.repository;

import com.service.phone.model.for_phone.PhoneRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepairRepository extends JpaRepository<PhoneRepair, Long> {
}
