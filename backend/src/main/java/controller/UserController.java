package controller;

import dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<UserDto> index() {
        return userService.getAllUsers();
    }

    @GetMapping( "/{id}")
    public ResponseEntity<UserDto> show(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserByById(id));
    }
}
