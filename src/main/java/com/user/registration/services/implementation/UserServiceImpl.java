package com.user.registration.services.implementation;

import com.user.registration.exceptions.ResourceNotFoundException;
import com.user.registration.exceptions.UserAlreadyExistsException;
import com.user.registration.models.User;
import com.user.registration.payload.UpdateUserDto;
import com.user.registration.payload.UserDto;
import com.user.registration.payload.UserRes;
import com.user.registration.repositories.UserRepo;
import com.user.registration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto createUser(UserDto userDto){
        User newUser = DtotoUser(userDto);
        if(this.userRepo.findByEmail(newUser.getEmail()) != null){
            throw new UserAlreadyExistsException("User already exists with the provided email.");
        }
        User savedUser;
        try {
         savedUser = this.userRepo.save(newUser);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Error saving user: " + ex.getMessage(), ex);
        }
        if (savedUser == null) {
            throw new RuntimeException("Error saving user: null returned from repository");
        }
        return UsertoDto(savedUser);
     }

     @Override
    public UserDto updateUser(UpdateUserDto userDto){
        User user;
        try {
             user = this.userRepo.findByEmail(userDto.getEmail());
        }
        catch(ResourceNotFoundException ex){
            throw new RuntimeException("User not found with the given email ");
        }
        if(bCryptPasswordEncoder.matches(userDto.getPassword(), user.getPassword()) == false){
             throw new RuntimeException("Invalid Password");
        }
        //user.setName(userDto.getName() );
        //user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getNew_password()));
        User updatedUser = this.userRepo.save(user);

        return this.UsertoDto(updatedUser);
     }
     @Override
     public void deleteUser(Integer userId){
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user" ,  "id",  userId));
        this.userRepo.deleteById(userId);
        return;
     }
     private User DtotoUser(UserDto userDto){
        User user = new User();
       //user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setPhone_number(userDto.getPhone_number());
        return user;
     }
     public UserDto UsertoDto (User user){
        UserDto userDto = new UserDto();
        //userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhone_number(user.getPhone_number());
        return userDto;
     }

    public boolean userExists(String email){
        User user = userRepo.findByEmail(email);
        return user != null;
    }
    public UserRes userByEmail(String email){
        User user = userRepo.findByEmail(email);
        UserRes userRes = new UserRes();
        userRes.setEmail(user.getEmail());
        userRes.setName(user.getName());
        userRes.setPhone_number(user.getPhone_number());
        return userRes;
    }
}
