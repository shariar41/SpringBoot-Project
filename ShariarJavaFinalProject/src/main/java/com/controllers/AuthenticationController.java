package com.controllers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Dtos.LoginUserDto;
import com.Dtos.RegisterUserDto;
import com.models.LoginResponse;
import com.models.User;
import com.services.AuthenticationService;
import com.services.EmailService;
import com.services.JwtService;

@RequestMapping("api/auth")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;

	private final AuthenticationService authenticationService;
	
	@Autowired
	private EmailService emailService ;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> register(@RequestBody RegisterUserDto registerUserDto) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        // Register the user
	        User registeredUser = authenticationService.signup(registerUserDto);
	        response.put("message", "User registered successfully.");
	        
	        // Send success email
	        emailService.sendEmail(registerUserDto.getEmail(), "Registration Successful", "You have been registered successfully.");

	        return ResponseEntity.ok()
	                             .contentType(MediaType.APPLICATION_JSON)
	                             .body(response);

	    }catch (IllegalArgumentException e) {
	        // Catch the specific exception for a taken email
	        response.put("message", "An error occurred during registration: " + e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    } catch (Exception e) {
	        // Catch any other exceptions that might occur
	    	e.printStackTrace();
	        response.put("message", "An unexpected error occurred during registration."+e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
}