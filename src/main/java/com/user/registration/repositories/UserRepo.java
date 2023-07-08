package com.user.registration.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.user.registration.models.User;

public interface UserRepo extends JpaRepository<User, Integer> {


    User findByEmail(String email);
}
