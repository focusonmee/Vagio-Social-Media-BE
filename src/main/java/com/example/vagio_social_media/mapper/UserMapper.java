package com.example.vagio_social_media.mapper;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.model.User;

public class UserMapper {
    public static UserDTO entityToDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public static User dtoToEntity(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()) // Assuming password comes from DTO
                .build();
    }
}
