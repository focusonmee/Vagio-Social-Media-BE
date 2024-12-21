package com.example.vagio_social_media.mapper;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Chuyển User sang UserDTO, ánh xạ đúng các trường

    UserDTO toDTO(User user);

    // Chuyển UserDTO sang User, ánh xạ đúng các trường
    User toEntity(UserDTO userDTO);
}
