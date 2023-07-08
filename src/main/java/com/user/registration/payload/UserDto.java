package com.user.registration.payload;

import com.user.registration.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(min=4,message = "Username must be minimum of 4 characters")
    private String name;

    @Email(message = "Enter a valid Email address")
    private String email;

    @NotNull
    @ValidPassword(message = "Invalid Password")
    @Size(min=8, max = 12, message = "Password length must be between 8-12")
    private String password;

    @NotNull
    @Size(min=10, max = 11, message = "Phone no length must be between 10-11")
    private String phone_number;
}
