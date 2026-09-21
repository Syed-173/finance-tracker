package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.UserRequest;
import com.syed.finance_tracker.Service.UserService;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        System.out.println(email);

        return userService.getUsers();
    }
    @PostMapping("/users")
    public String createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }




}
