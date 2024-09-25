package org.example.springbootproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    @Column(nullable = false, unique = true, name = "room_code")
    private String roomCode;

    @Column(nullable = false, name = "area")
    private float area;

    @Column(nullable = false, name = "capacity")
    private int capacity;

    @Column(nullable = false, name = "rent_price")
    private float rentPrice;

    @Column(nullable = false, name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "landlord_id", nullable = false)
    @JsonBackReference
    private User landlord;

    // Many-to-Many relationship with Utility
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "room_utilities", // Join table
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "utility_id")
    )
    @JsonIgnoreProperties("rooms")
    private Set<Utility> utilities;

    // Many-to-Many relationship with User (Tenants)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "tenants",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties("rooms")
    private Set<User> roomsTenants;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Bill> bills;
}
