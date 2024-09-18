package org.example.springbootproject.service;

import jakarta.validation.Valid;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.Role;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.mapper.UserMapper;
import org.example.springbootproject.payload.request.CreateUserRequest;
import org.example.springbootproject.payload.request.ProfileUpdateRequest;
import org.example.springbootproject.repository.RoleRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.springbootproject.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> listUsersByRole(int pageNo, int pageSize, String sortBy, String searchValue, String filterValue) {
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<User> usersPage = userRepository.getUsersBySearchAndFilter(searchValue, filterValue, paging);

            List<UserDto> users = usersPage.stream()
                    .map(user -> userMapper.toDTO(user))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("currentPage", usersPage.getNumber());
            response.put("totalItems", usersPage.getTotalElements());
            response.put("totalPages", usersPage.getTotalPages());
            response.put("pageSize", usersPage.getSize());

            return response;
        } catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }


    public UserDto getUserDtoByById(int id) {
        User user = userRepository.findUserById(id);

        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDto updateUserDtoById(int id, ProfileUpdateRequest request, String currentUserRole) {
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setLocation(request.getLocation());
            if(currentUserRole.equals(Constants.ADMIN_ROLE)) {
                Set<Role> roles = new HashSet<>();
                Role userRole = roleRepository.findByRoleName(request.getHighestRole());
                roles.add(userRole);
                user.setRoles(roles);
                user.setUsername(request.getUsername());
            }
            userRepository.save(user);
            return userMapper.toDTO(user);
        }

        return null;
    }

    @Transactional
    public UserDto createNewUser(CreateUserRequest request, String password) {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLocation(request.getLocation());
        Role userRole = roleRepository.findByRoleName(request.getHighestRole());
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteUserById(id);
    }

    public UserDto getUserDtoByUsername(String username) {
        User user =  userRepository.findUserByUsername(username);

        return userMapper.toDTO(user);
    }

    public boolean resetPassword(int id, String verifiedPassword) {
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setPassword(passwordEncoder.encode(verifiedPassword));
            userRepository.save(user);

            return true;
        }

        return true;
    }

    public boolean hasId(int id, String userName) {
        User user = userRepository.findUserById(id);

        return user != null && user.getUsername().equalsIgnoreCase(userName);
    }

    public boolean checkFieldExisted(String fieldName, String fieldValue) {
        return switch (fieldName) {
            case "email" -> userRepository.existsEmail(fieldValue);
            case "phoneNumber" -> userRepository.existsPhoneNumber(fieldValue);
            case "username" -> userRepository.existsUserName(fieldValue);
            default -> false;
        };

    }

    public String checkFieldDuplicated(String fieldName, String fieldValue, Integer id) {
        switch (fieldName) {
            case "email" -> {
                if (userRepository.duplicateEmail(fieldValue, id)) return "E-CM-007";
            }
            case "phoneNumber" -> {
                if (userRepository.duplicatePhone(fieldValue, id)) return "E-CM-009";
            }
            case "username" -> {
                if (userRepository.duplicateUsername(fieldValue, id)) return "E-CM-013";
            }
        }

        return "";
    }
}
