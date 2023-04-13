package com.service.phone.model.for_phone;

import com.service.phone.model.for_phone.Photo;
import com.service.phone.model.for_phone.Room;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;
    private String message;
    @OneToMany(
            mappedBy = "sender",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Photo> photos = new HashSet<>();
    private LocalDateTime localDateTime;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "sender_room",
            joinColumns = @JoinColumn(name = "sender_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms = new HashSet<>();

    public void addRoom(Room room){
        rooms.add(room);
        room.getSenders().add(this);
    }

    public void addPhoto(Photo photo){
        photos.add(photo);
        photo.setSender(this);
    }
}
