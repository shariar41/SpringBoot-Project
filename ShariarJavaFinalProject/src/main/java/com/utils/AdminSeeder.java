package com.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Dtos.RegisterUserDto;
import com.models.Role;
import com.models.RoleEnum;
import com.models.User;
import com.repositories.RoleRepository;
import com.repositories.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
    private UserRepository userRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;


  

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setFullName("Super Admin");
        userDto.setEmail("super.admin@email.com");
        userDto.setPhone("3476291759");
        userDto.setAddress("822 Briarwood Ave,Bridgeport, CT");
        userDto.setPassword("admin");

//        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        Set<RoleEnum> roles = new HashSet<>();

        // Fetch the SUPER_ADMIN role and add it to the roles set
//        roleRepository.findByName(RoleEnum.SUPER_ADMIN).ifPresent(roles::add);
        roles.add(RoleEnum.SUPER_ADMIN);
        if (roles.isEmpty() || optionalUser.isPresent()) {
            return;
        }

//        var user = new User()
//            .setFullName(userDto.getFullName())
//            .setEmail(userDto.getEmail())
//            .setPassword(passwordEncoder.encode(userDto.getPassword()))
//            .setRole(optionalRole.get());
        
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .fullName(userDto.getFullName())
                .phone(userDto.getPhone())
                .address(userDto.getAddress()) // Assuming address is also part of input or you may need to add it
//                .userType(input.getUserType())
                //.roles(mapRoles(input.getRoles())) // Convert Set<Role> to a suitable format
                .roles(roles)
                .build();
        userRepository.save(user);
    }
}
