package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.UserRequest;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String createUser(UserRequest userRequest) {
        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getName(),
                userRequest.getEmail(),
                hashedPassword);


        userRepository.save(user);


        return userRequest.getName();
    }


}
