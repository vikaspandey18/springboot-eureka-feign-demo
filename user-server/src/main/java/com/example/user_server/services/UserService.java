package com.example.user_server.services;

import com.example.user_server.dto.UserResponse;
import com.example.user_server.entity.User;
import com.example.user_server.mapper.UserMapper;
import com.example.user_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDTO(user);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    public UserResponse createUser(UserResponse userResponse){
        User user = UserMapper.toEntity(userResponse);
        User saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }
}
