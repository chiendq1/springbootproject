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

    public List<UserDto> getAllDtoUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toDtoList(users);
    }
}

