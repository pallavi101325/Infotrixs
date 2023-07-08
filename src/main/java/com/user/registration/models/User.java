package com.user.registration.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Setter

public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(name = "user_name" , nullable = false, length = 100)
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone_number;
}
