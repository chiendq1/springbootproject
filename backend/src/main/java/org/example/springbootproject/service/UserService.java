package org.example.springbootproject.service;

import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.springbootproject.repository.UserRepository;

import java.util.List;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDto getUserDtoByById(int id) {
        User user = userRepository.findUserById(id);

        return userMapper.toDTO(user);
    }

    public UserDto updateUserDtoById(int id, UserDto userDto) {
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setFullName(userDto.getFullName());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setLocation(userDto.getLocation());
            userRepository.save(user);

            return userMapper.toDTO(user);
        }

        return null;
    }

    public UserDto getUserDtoByUsername(String username) {
        User user =  userRepository.findUserByUsername(username);

        return userMapper.toDTO(user);
    }

    public List<UserDto> getAllDtoUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toDtoList(users);
    }

    public boolean resetPassword(int id, String verifiedPassword) {
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setPassword(verifiedPassword);
            userRepository.save(user);

            return true;
        }

        return true;
    }
}

