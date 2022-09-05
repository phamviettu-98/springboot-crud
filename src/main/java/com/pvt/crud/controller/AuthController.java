package com.pvt.crud.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pvt.crud.dto.AuthenticationRequest;
import com.pvt.crud.dto.ResponseObjectTemplate;
import com.pvt.crud.dto.Signup;
import com.pvt.crud.dto.UserDTO;
import com.pvt.crud.entity.User;
import com.pvt.crud.entity.User.UserActive;
import com.pvt.crud.mapper.UserMapper;
import com.pvt.crud.repository.UserRepository;
import com.pvt.crud.security.jwt.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@PostMapping("/signin")
	@Transactional
	public ResponseEntity<ResponseObjectTemplate> signin(@RequestBody AuthenticationRequest data) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));
			Optional<User> userOptional = userRepository.findByUsername(data.getUsername());
			if (!userOptional.isPresent()) {
				System.out.println("Error");
				throw new UsernameNotFoundException("Username " + data + "not found");
			}
			User user = userOptional.get();
			UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(user);
			System.err.println(userDTO);
			String token = jwtTokenProvider.createToken(userDTO);
			Map<Object, Object> model = new HashMap<>();
			model.put("token", token);
			model.put("user", userDTO);
			System.out.println("OK");
			return ok(new ResponseObjectTemplate(200, "success", model));
		} catch (AuthenticationException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseObjectTemplate(HttpStatus.BAD_REQUEST.value(), "Invalid username/password supplied"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObjectTemplate(
					HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid username/password supplied"));
		}
	}

	@PutMapping("/change-password")
	public ResponseEntity<ResponseObjectTemplate> changePassword(@RequestBody Signup data) {
		try {
			String username = data.getUsername();
			Optional<User> userOptional = userRepository.findByUsername(username);
			if (!userOptional.isPresent()) {
				throw new UsernameNotFoundException("Username " + username + "not found");
			}
			User user = userOptional.get();
			user.setPassword(passwordEncoder.encode(data.getPassword()));
			userRepository.save(user);
			return ok(new ResponseObjectTemplate(200, "success", null));
		} catch (AuthenticationException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ResponseObjectTemplate(HttpStatus.UNAUTHORIZED.value(), "Invalid username/password supplied"));
		}
	}
	@PostMapping("/signup")
	public ResponseEntity<ResponseObjectTemplate> signup(@RequestBody Signup data) {
		try {
			Optional<User> userOptional = userRepository.findByUsername(data.getUsername());
			if (userOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ResponseObjectTemplate(HttpStatus.UNAUTHORIZED.value(), "Username was existed"));
			} else {
				User user = UserMapper.INSTANCE.toUserFromSignup(data);
				user.setPassword(passwordEncoder.encode(data.getPassword()));
				System.err.println(user);
				user = userRepository.save(user);
				return ok(new ResponseObjectTemplate(200, "success", UserMapper.INSTANCE.toUserDTO(user)));
			}
		} catch (AuthenticationException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ResponseObjectTemplate(HttpStatus.UNAUTHORIZED.value(), "Invalid username/password supplied"));
		}
	}

}
