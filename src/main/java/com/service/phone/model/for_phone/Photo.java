package com.service.phone.model.for_phone;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String urlPhoto;
    @ManyToOne(fetch = FetchType.LAZY)
    private PhoneRepairRequest phoneRepairRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    private Sender sender;

}
