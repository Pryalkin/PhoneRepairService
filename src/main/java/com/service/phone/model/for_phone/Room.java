package com.service.phone.model.for_phone;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne(fetch = FetchType.LAZY)
    private PhoneRepair phoneRepair;
    @ManyToMany(mappedBy = "rooms")
    private Set<Sender> senders;

}
