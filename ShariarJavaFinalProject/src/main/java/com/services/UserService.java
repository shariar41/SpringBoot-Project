//package com.services;
//
//import com.models.Role;
//import com.models.User;
//import com.repositories.registration.UserRepository;
//
//import jakarta.validation.Valid;
//
//import java.util.*;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;
//
//@Service
//
//@Validated
//public class UserService {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	// @Autowired // private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//	public User saveUser(@Valid User user) { if
//		(userRepository.findByEmail(user.getEmail()) != null) { throw new
//		IllegalArgumentException("Email already in use"); } user.setPassword(new
//				BCryptPasswordEncoder().encode(user.getPassword())); // Assign role based on
//		userType Set<Role> roles = new HashSet<>(); if
//		("admin".equals(user.getUserType())) { roles.add(Role.ADMIN); } else {
//			roles.add(Role.USER); } user.setRoles(roles); return
//					userRepository.save(user); }
//
//	public List<User> getAllUsers() {
//		return userRepository.findAll();
//	}
//
//	public Optional<User> getUserById(Long id) {
//		return userRepository.findById(id);
//	}
//}


package com.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Dtos.RegisterUserDto;
import com.models.Role;
import com.models.RoleEnum;
import com.models.User;
import com.repositories.RoleRepository;
import com.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private UserRepository userRepository;
    
	@Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    public User createAdministrator(RegisterUserDto input) {
//        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Set<RoleEnum> roles = new HashSet<>();
        
        if (input.getRoles() != null && !input.getRoles().isEmpty()) {
            roles.addAll(input.getRoles());
        } else {
            // Assign default role if none provided
            roles.add(RoleEnum.USER);
        }

        User user = User.builder()
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .fullName(input.getFullName())
                .phone(input.getPhone())
                .address(input.getAddress()) // Assuming address is also part of input or you may need to add it
//                .userType(input.getUserType())
                //.roles(mapRoles(input.getRoles())) // Convert Set<Role> to a suitable format
                .roles(roles)
                .build();
//        user.setRoles(roles);
        return userRepository.save(user);
    }
    
    public User updateUserProfile(Long userId, User updateProfileDto) {
        // Fetch the user from the database
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        User user = optionalUser.get();

        // Update user details with the new values
        if (updateProfileDto.getFullName() != null) {
            user.setFullName(updateProfileDto.getFullName());
        }
        if (updateProfileDto.getPhone() != null) {
            user.setPhone(updateProfileDto.getPhone());
        }
        if (updateProfileDto.getAddress() != null) {
            user.setAddress(updateProfileDto.getAddress());
        }

        // Save the updated user
        return userRepository.save(user);
    }
}