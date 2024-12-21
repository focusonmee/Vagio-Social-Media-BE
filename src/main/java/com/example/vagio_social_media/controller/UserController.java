package com.example.vagio_social_media.controller;

import com.example.vagio_social_media.dto.UserDTO;
import com.example.vagio_social_media.model.User;
import com.example.vagio_social_media.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
//@RequiredArgsConstructor

public class UserController {

    @Autowired
    private  IUserService userService; // userService sẽ được tiêm vào từ Spring

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO, BindingResult result) {
        try {
            // Kiểm tra các lỗi từ BindingResult
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                // Trả về lỗi với danh sách lỗi dưới dạng String
                return ResponseEntity.badRequest().body("Validation failed: " + String.join(", ", errorMessages));
            }
            // Tạo  người dùng mới
            User user = userService.register(userDTO);
            // Trả về phản hồi khi tạo người dùng thành công
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (Exception e) {
            // Trả về lỗi trong trường hợp có exception
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") Integer userId) {
        try {
            // Tìm người dùng theo ID và trả về UserDTO
            UserDTO userDTO = userService.findUserById(userId);

            // Trả về UserDTO nếu tìm thấy
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            // Nếu không tìm thấy người dùng, trả về lỗi 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
