package com.service.phone.model.for_phone;

import com.service.phone.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "phone_repair")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhoneRepair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateOfAcceptanceOfTheRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    private User engineer;
    @ManyToOne(fetch = FetchType.LAZY)
    private PhoneRepairRequest phoneRepairRequest;
    private String status;
//    @OneToMany(
//            mappedBy = "phoneRepair",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Room room;

//    public void addRoom(Room room){
//        rooms.add(room);
//        room.setPhoneRepair(this);
//    }
}
