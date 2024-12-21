package com.example.vagio_social_media.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Sử dụng Lombok để tự động sinh ra constructor không tham số
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstName", length = 100)
    private String firstName;

    @Column(name = "lastName", length = 100)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "followers", length = 200)
    private List<Integer> followers;

    @Column(name = "followings", length = 200)
    private List<Integer> followings;
}
