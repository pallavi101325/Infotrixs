package com.user.registration.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserDto {
    private String name;
    private String email;
    private String password;
    private  String new_password;
}

