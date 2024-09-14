package com.Dtos;

import java.util.Set;

import com.models.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {
    private String email;
    
    private String password;
    
    private String fullName;
    
    private Set<RoleEnum> roles;
//    private String userType;
    private String phone;
    private String address;
    
    // getters and setters here...
}