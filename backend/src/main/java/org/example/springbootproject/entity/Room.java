package org.example.springbootproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    @Column(nullable = false, unique = true, name = "room_code")
    private String roomCode;

    @Column(nullable = false, unique = true, name = "area")
    private String area;

    @Column(nullable = false, unique = true, name = "capacity")
    private int capacity;

    @Column(nullable = false, unique = true, name = "rent_price")
    private float rentPrice;

    @Column(nullable = false, unique = true, name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "landlord_id", nullable = false)
    private User landlord;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "room_utilities",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "utility_id")
    )
    private Set<Utility> utilities;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tenants",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Room> roomsTenants;
}
