package com.user.registration.services;

import com.user.registration.models.User;
import com.user.registration.payload.UpdateUserDto;
import com.user.registration.payload.UserDto;
import com.user.registration.payload.UserRes;

public interface UserService {
   UserDto createUser( UserDto user);
   UserDto updateUser(UpdateUserDto user);

   void deleteUser(Integer userId);

   public boolean userExists(String email);
   public UserRes userByEmail(String email);
}
