package com.service.phone.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.phone.model.for_phone.PhoneRepair;
import com.service.phone.model.for_phone.PhoneRepairRequest;
import com.service.phone.model.for_phone.Sender;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @EqualsAndHashCode.Include
    private String username;
    @EqualsAndHashCode.Include
    private String email;
    @EqualsAndHashCode.Include
    private String password;
    private String role;
    private String[] authorities;
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PhoneRepairRequest> phoneRepairRequests = new HashSet<>();
    @OneToMany(
            mappedBy = "engineer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PhoneRepair> phoneRepairs = new HashSet<>();
    @OneToMany(
            mappedBy = "sender",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Sender> senders = new HashSet<>();

    public void addPhoneRepairRequest(PhoneRepairRequest phoneRepairRequest){
        phoneRepairRequests.add(phoneRepairRequest);
        phoneRepairRequest.setCustomer(this);
    }

    public void addPhoneRepair(PhoneRepair phoneRepair){
        phoneRepairs.add(phoneRepair);
        phoneRepair.setEngineer(this);
    }

    public void addSender(Sender sender){
        senders.add(sender);
        sender.setSender(this);
    }

}
