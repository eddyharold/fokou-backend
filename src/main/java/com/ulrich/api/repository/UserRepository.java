package com.ulrich.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ulrich.api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmailAndPassword(String email, String Password);
}
