package com.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }

 // Getters and setters...
}