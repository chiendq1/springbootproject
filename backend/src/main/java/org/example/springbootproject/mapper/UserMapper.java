package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.RoleDto;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.Role;
import org.example.springbootproject.entity.User;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDTO(User user) {
        Set<RoleDto> roles = user.getRoles().stream()
                .map(this::roleToDTO)
                .collect(Collectors.toSet());
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getPhoneNumber(), user.getLocation(), roles);
    }

    public User toEntity(UserDto userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setLocation(userDTO.getLocation());
        user.setUsername(userDTO.getUsername());

        Set<Role> roles = userDTO.getRoles().stream()
                .map(this::dtoToRole)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return user;
    }

    public List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private RoleDto roleToDTO(Role role) {
        return new RoleDto(role.getId(), role.getRoleName());
    }

    private Role dtoToRole(RoleDto roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setRoleName(roleDTO.getRoleName());
        return role;
    }
}

