package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springbootproject.entity.Role;

import java.util.Comparator;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String location;
    private Set<RoleDto> roles;

    public String getHighestRole() {
        // Define a custom comparator for roles based on their importance
        return roles.stream()
                .max(Comparator.comparingInt(this::getRolePriority))
                .orElse(null).getRoleName(); // return null if no roles exist
    }

    // Helper function to define priority of roles
    private int getRolePriority(RoleDto role) {
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
}
