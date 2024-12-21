package com.example.vagio_social_media.service;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.exception.EmailNotFoundException;
import com.example.vagio_social_media.exception.UserNotFoundException;
import com.example.vagio_social_media.mapper.UserMapper;
import com.example.vagio_social_media.model.User;
import com.example.vagio_social_media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository; // Using constructor-based injection with Lombok
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(UserDTO userDTO) throws Exception {
        // Creating a new user based on the DTO
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        User newUser = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
        String password = userDTO.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        newUser.setPassword(encodePassword);
        // Saving the user to the database
        return userRepository.save(newUser);
    }

    // find by id
    public UserDTO findUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        return userMapper.toDTO(user);
    }


    @Override
    public UserDTO findUserByEmail(String email) throws Exception {
        // Tìm kiếm người dùng theo email, nếu không tìm thấy sẽ ném ra ngoại lệ
        User user = userRepository.findUserByEmail(email); // Giả sử findUserByEmail trả về null khi không tìm thấy người dùng

        if (user == null) {
            // Nếu không tìm thấy người dùng, ném ngoại lệ với thông báo lỗi
            throw new EmailNotFoundException("User with email " + email + " doesn't exist!");
        }

        // Nếu tìm thấy người dùng, chuyển đổi sang UserDTO và trả về
        return userMapper.toDTO(user); // Giả sử bạn có một UserMapper để chuyển đổi từ User sang UserDTO
    }


    @Override
    @Transactional
    public User followUser(Integer userId1, Integer userId2) {
        try {
            // Finding both users by ID
            User user1 = userRepository.findById(userId1)
                    .orElseThrow(() -> new Exception("User doesn't exist!"));
            User user2 = userRepository.findById(userId2)
                    .orElseThrow(() -> new Exception("User doesn't exist!"));

            // Logic to follow user2 by user1
            user2.getFollowers().add(user1.getId());
            user1.getFollowers().add(user2.getId()); // Assuming `getFollowers` returns a collection

             userRepository.save(user1);
             userRepository.save(user2);
            return user1;
        } catch (Exception e) {
            throw new RuntimeException("Follow operation failed: " + e.getMessage());
        }
    }

    @Override
    public User updateUser(User user, Integer userId) {
        try {
            // Find the existing user by ID
            User existingUser = userRepository.findById(userId).orElseThrow();

            // Update only the fields provided in the new `user` object
            if (user.getFirstName() != null) {
                existingUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                existingUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }

            // Save and return the updated user
            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }


    @Override
    public List<User> searchUser(String query) {
        // Assuming a method `searchUsersByQuery` exists in UserRepository
        return null;
    }
}
