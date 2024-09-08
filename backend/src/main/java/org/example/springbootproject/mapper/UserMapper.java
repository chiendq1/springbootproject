package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());  // Remember to encode the password
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        return user;
    }

    public List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

