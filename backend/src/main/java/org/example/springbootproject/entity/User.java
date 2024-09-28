package org.example.springbootproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.util.Comparator;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "location", unique = true)
    private String location;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;

    // One User can own multiple Rooms
    @OneToMany(mappedBy = "landlord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Room> rooms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tenants",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    @JsonIgnoreProperties("roomsTenants")
    private Set<Room> roomsTenants;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties("users")
    private Set<Role> roles;

    public boolean isExist(String username) {
        return this.username.equals(username);
    }

    public String getHighestRole() {
        // Define a custom comparator for roles based on their importance
        return roles.stream()
                .max(Comparator.comparingInt(this::getRolePriority))
                .orElse(null).getRoleName(); // return null if no roles exist
    }

    // Helper function to define priority of roles
    private int getRolePriority(Role role) {
        switch (role.getRoleName().toUpperCase()) {
            case "ADMIN":
                return 3;
            case "LANDLORD":
                return 2;
            case "TENANT":
                return 1;
            default:
                return 0; // Unknown roles get the lowest priority
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Phone: %s, Email: %s", fullName, phoneNumber, email);
    }
}
