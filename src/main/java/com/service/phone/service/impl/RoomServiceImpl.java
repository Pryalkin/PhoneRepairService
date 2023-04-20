package com.service.phone.service.impl;

import com.service.phone.model.for_phone.PhoneRepair;
import com.service.phone.model.for_phone.Room;
import com.service.phone.repository.RoomRepository;
import com.service.phone.service.RoomService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(PhoneRepair phoneRepair) {
        Room room = new Room();
        room.setNumber(generateNumberRoom());
        room.setPhoneRepair(phoneRepair);
        roomRepository.save(room);
    }

    private String generateNumberRoom() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
