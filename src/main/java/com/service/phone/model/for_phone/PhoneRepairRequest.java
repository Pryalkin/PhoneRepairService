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
@Table(name = "phone_repair_request")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhoneRepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    private String idApp;
    @EqualsAndHashCode.Include
    private String phoneNumber;
    @EqualsAndHashCode.Include
    private String causeOfFailure;
    private String status;
    @OneToMany(
            mappedBy = "phoneRepairRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Photo> photos = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User customer;
    @EqualsAndHashCode.Include
    private LocalDateTime dateOfRequestForPhoneRepair;
    @OneToMany(
            mappedBy = "phoneRepairRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PhoneRepair> phoneRepairs = new HashSet<>();

    public void addPhoto(Photo photo){
        photos.add(photo);
        photo.setPhoneRepairRequest(this);
    }

    public void addPhoneRepair(PhoneRepair phoneRepair){
        phoneRepairs.add(phoneRepair);
        phoneRepair.setPhoneRepairRequest(this);
    }

}
