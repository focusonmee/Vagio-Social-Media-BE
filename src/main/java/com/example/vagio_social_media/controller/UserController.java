package com.example.vagio_social_media.controller;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.exception.UserNotFoundException;
import com.example.vagio_social_media.model.User;
import com.example.vagio_social_media.response.RegisterResponse;
import com.example.vagio_social_media.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService; // userService sẽ được tiêm vào từ Spring

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        RegisterResponse.builder()
                                .message(String.join(", ", errorMessages)) // Kết hợp thông điệp lỗi thành một chuỗi
                                .build()
                );
            }
            User user = userService.register(userDTO);
            return ResponseEntity.status(201).body(RegisterResponse.builder()
                    .message("Register successfully!!!!")
                    .user(user)// Thông báo thành công
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RegisterResponse.builder()
                            .message("Register failed: " + e.getMessage())  // Lấy thông báo lỗi từ exception
                            .build()
            );
        }
    }


    @GetMapping("users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Integer userId) throws Exception {
        try {
            UserDTO userDTO = userService.findUserById(userId);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            UserDTO userDTO = userService.findUserByEmail(email);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Email not found");
        }
    }



}
