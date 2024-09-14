package com.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Dtos.LoginUserDto;
import com.Dtos.RegisterUserDto;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.models.Role;
import com.models.RoleEnum;
import com.models.User;
import com.repositories.RoleRepository;
import com.repositories.UserRepository;

@Service
public class AuthenticationService {
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private UserRepository userRepository;
    
	@Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
    private AuthenticationManager authenticationManager;

//    public AuthenticationService(
//        UserRepository userRepository,
//        AuthenticationManager authenticationManager,
//        PasswordEncoder passwordEncoder
//    ) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    public User signup(RegisterUserDto input) {
     // Check if the email is already taken
        try {
        	Optional<User> existingUser = userRepository.findByEmail(input.getEmail());
    	if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        // Create a new user from input

        User user = User.builder()
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .fullName(input.getFullName())
                .phone(input.getPhone())
                .address(input.getAddress()) // Assuming address is also part of input or you may need to add it
//                .userType(input.getUserType())
                //.roles(mapRoles(input.getRoles())) // Convert Set<Role> to a suitable format
//                .role(optionalRole.get())
                .build();
        Set<RoleEnum> roles = new HashSet<>();
        if (input.getRoles() != null) {
            roles.addAll(input.getRoles()); // Add roles from input
        } else {
            // Assign the default role (USER) if no roles are provided
            roles.add(RoleEnum.USER);
        }

        // Set roles to the user
        user.setRoles(roles);
        
        // Save the new user to the database
        return userRepository.save(user);
        }catch (Exception e) {
            // Log the exception or handle it as needed
        	e.printStackTrace();
            throw new RuntimeException("Error occurred while registering the user.", e);
        }
        
    }
    private Set<RoleEnum> mapRoles(Set<RoleEnum> roles) {
        return roles != null ? roles : new HashSet<>();
    }



    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        return optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
    
    }
}