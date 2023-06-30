package com.f1tekkz.springrestfulapi.service;

import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.RegisterUserRequest;
import com.f1tekkz.springrestfulapi.model.UserResponse;
import com.f1tekkz.springrestfulapi.repository.UserRepository;
import com.f1tekkz.springrestfulapi.security.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private NativeWebRequest webRequest;

    @Transactional
    public void register(RegisterUserRequest request){
        validationService.validate(request);

        if(userRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt())); // hashing password recommended is bcrypt
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
