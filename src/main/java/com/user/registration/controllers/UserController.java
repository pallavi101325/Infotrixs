package com.user.registration.controllers;

import com.user.registration.Security.Jwt.JwtUtils;
import com.user.registration.exceptions.InvalidPasswordException;
import com.user.registration.exceptions.UserAlreadyExistsException;
import com.user.registration.payload.*;
import com.user.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:3000/")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    // POST Request - create user
    @PostMapping ("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto){
        try {
        UserDto createUserDto = this.userService.createUser(userDto);
        //return new ResponseEntity<>( createUserDto , HttpStatus.CREATED);
            return ResponseEntity.ok("User created successfully");
        }
       catch (UserAlreadyExistsException ex){
          //return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(ex.getMessage() , false) , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping ("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest){
        try {
            if (!userService.userExists(loginRequest.getEmail())) {
                return new ResponseEntity<>("Account doesn't exist!", HttpStatus.BAD_REQUEST);
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        }
        String token = jwtUtils.generateToken(loginRequest.getEmail());
        LoginResponse res = new LoginResponse();
        res.setToken(token);
        res.setUser(userService.userByEmail(loginRequest.getEmail()));
        return new ResponseEntity<LoginResponse>(res , HttpStatus.OK);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDto userDto ){
        try {
            UserDto updatedUser = this.userService.updateUser(userDto);
            return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
        }
        catch (InvalidPasswordException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(ex.getMessage() , false) , HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userId){
        this.userService.deleteUser(userId);
        //return ResponseEntity.ok( Map.of("Message" , "User deleted successfully") );
        return new ResponseEntity<ApiResponse>(new ApiResponse("user deleted successfully!!" , true) , HttpStatus.OK);
    }
}
