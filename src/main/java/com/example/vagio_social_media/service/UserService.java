package com.example.vagio_social_media.service;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.exception.EmailNotFoundException;
import com.example.vagio_social_media.exception.UserNotFoundException;
import com.example.vagio_social_media.model.User;
import com.example.vagio_social_media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.vagio_social_media.mapper.UserMapper.entityToDTO;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository; // Using constructor-based injection with Lombok

    @Override
    public User register(UserDTO userDTO) throws Exception {
        // Creating a new user based on the DTO
        User newUser = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        // Saving the user to the database
        return userRepository.save(newUser);
    }

    // find by id
    public UserDTO findUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        return entityToDTO(user);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        // Handling user lookup by email
        return Optional.ofNullable(userRepository.findUserByEmail(email))
                .orElseThrow(() -> new EmailNotFoundException("User with email " + email + " doesn't exist!"));
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
