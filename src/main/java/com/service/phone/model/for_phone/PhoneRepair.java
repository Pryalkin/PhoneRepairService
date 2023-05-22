package com.service.phone.model.for_phone;

import com.service.phone.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

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
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private LocalDateTime dateOfAcceptanceOfTheRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    private User engineer;
    @EqualsAndHashCode.Include
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    private PhoneRepairRequest phoneRepairRequest;
//    @OneToMany(
//            mappedBy = "phoneRepair",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
    @OneToOne(mappedBy = "phoneRepair", cascade = CascadeType.ALL)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Room room;

//    public void addRoom(Room room){
//        rooms.add(room);
//        room.setPhoneRepair(this);
//    }
}
