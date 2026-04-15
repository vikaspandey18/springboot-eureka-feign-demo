package com.example.user_server.mapper;


import com.example.user_server.dto.UserResponse;
import com.example.user_server.entity.User;

public class UserMapper {

    public static UserResponse toDTO(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    public static User toEntity(UserResponse userResponse){
        User user = new User();
        user.setId(userResponse.getId());
        user.setName(userResponse.getName());
        user.setEmail(userResponse.getEmail());
        return user;
    }
}
