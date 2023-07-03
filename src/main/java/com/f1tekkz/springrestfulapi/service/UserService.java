package com.f1tekkz.springrestfulapi.service;

import com.f1tekkz.springrestfulapi.entity.User;
import com.f1tekkz.springrestfulapi.model.RegisterUserRequest;
import com.f1tekkz.springrestfulapi.model.UpdateUserRequest;
import com.f1tekkz.springrestfulapi.model.UserResponse;
import com.f1tekkz.springrestfulapi.repository.UserRepository;
import com.f1tekkz.springrestfulapi.resolver.CurrentDateTime;
import com.f1tekkz.springrestfulapi.security.BCrypt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private NativeWebRequest webRequest;

    @Autowired
    private CurrentDateTime currentDateTime;

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
        user.setCreatedBy(user.getUsername());
        user.setCreatedDate(currentDateTime.getDateTime());
        userRepository.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        user.setModifiedBy(user.getUsername());
        user.setModifiedDate(currentDateTime.getDateTime());
        userRepository.save(user);

        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
