package org.example.springbootproject.controller;

import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<UserDto> index() {
        return userService.getAllDtoUsers();
    }

    @GetMapping( "/{id}")
    public ResponseEntity<UserDto> show(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserDtoByById(id));
    }
}
