package com.example.vagio_social_media.service;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.model.User;

import java.util.List;

public interface IUserService {
    User register(UserDTO userDTO) throws Exception;

    UserDTO findUserById(Integer userId) throws Exception;

    User findUserByEmail(String email) throws Exception;

    User followUser(Integer userId1, Integer userId2);

    User updateUser(User user,Integer userId);

    List<User> searchUser(String query);

}
