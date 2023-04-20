package com.service.phone.dto;

import com.service.phone.model.for_phone.Sender;
import lombok.Data;

import java.util.Set;

@Data
public class RoomAnswerDTO {

    private String number;
    private Set<Sender> senders;
}
