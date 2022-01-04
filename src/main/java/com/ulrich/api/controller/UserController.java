package com.ulrich.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ulrich.api.model.User;
import com.ulrich.api.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	UserRepository userRepository;

	// build user login
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user) {
		Optional<User> userOptional = Optional.empty();

		try {
			userOptional = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
			if (!userOptional.isPresent()) {
				logger.info(String.format("User not found in the database."));
				return new ResponseEntity<>(String.format("User not found in the database."), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
	}

	// build create user rest api
	@PostMapping
	public ResponseEntity<?> save(@RequestBody User user) {
		User _user = null;
		try {
			_user = userRepository.save(user);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ResponseEntity<>(_user, HttpStatus.CREATED);
	}

	// build get all users REST api
	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	// build get user by id
	@GetMapping("{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") long userId) {
		Optional<User> userOptional = Optional.empty();

		try {
			userOptional = userRepository.findById(userId);
			if (!userOptional.isPresent()) {
				logger.info(String.format("User: " + userId + " not found in the database."));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
	}

	// build update user rest api
	@PutMapping("{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		User _user = null;
		try {
			Optional<User> userData = userRepository.findById(id);

			if (userData.isPresent()) {
				_user = userData.get();
				_user.setName(user.getName());
				_user.setFirstname(user.getFirstname());
				_user.setEmail(user.getEmail());
				_user.setPhoneNumber(user.getPhoneNumber());
				_user.setPassword(user.getPassword());
				_user.setPersonality(user.getPersonality());
				_user = userRepository.save(_user);
			} else {
				logger.info(String.format("User: " + id + " not found in the database."));
				return new ResponseEntity<>(String.format("User: " + id + " not found."), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ResponseEntity<>(_user, HttpStatus.OK);
	}

	// build delete user rest api
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(String.format("User: " + id + " has been removed."), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(String.format("User: " + id + " not found."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
