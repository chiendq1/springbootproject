package org.example.springbootproject.service;

import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.mapper.UserMapper;
import org.example.springbootproject.payload.request.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.springbootproject.repository.UserRepository;

import java.util.List;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserDto getUserDtoByById(int id) {
        User user = userRepository.findUserById(id);

        return userMapper.toDTO(user);
    }

    public UserDto updateUserDtoById(int id, ProfileUpdateRequest request) {
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setLocation(request.getLocation());
            userRepository.save(user);

            return userMapper.toDTO(user);
        }

        return null;
    }

    public UserDto getUserDtoByUsername(String username) {
        User user =  userRepository.findUserByUsername(username);

        return userMapper.toDTO(user);
    }

//    public List<UserDto> listUsersByRole(int pageNo, int pageSize, String sortBy) {
//        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//        Page<User> users = userRepository.findAll(paging);
//
//        return userMapper.toDtoList(users);
//    }

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
        if(fieldName.equals("email")) {
            return userRepository.existsEmail(fieldValue);
        }

        return false;
    }

    public boolean checkFieldDuplicated(String fieldName, String fieldValue, Integer id) {
        if(fieldName.equals("email")) {
            return userRepository.duplicateEmail(fieldValue, id);
        } else if(fieldName.equals("phoneNumber")) {
            return userRepository.duplicatePhone(fieldValue, id);
        }

        return false;
    }
}
