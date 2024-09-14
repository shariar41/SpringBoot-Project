package com.models;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "users")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "User Type is required")
//    @Column(name="userType")
//    private String userType;

    @NotBlank(message = "Full Name is required")
    @Column(name="fullName")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name="email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;


    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @ElementCollection(targetClass = RoleEnum.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "Role is required")
    @Column(name = "role")
    private Set<RoleEnum> roles;
    
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "user_roles",
//        joinColumns = @JoinColumn(name = "user_id"),
//        inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles;
//    @OneToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
//    private Role role;

    
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName().toString()))
//                    .collect(Collectors.toList());
//    }
    
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roles.name.toString());
//            
//        return List.of(authority);
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    // Getters and setters
}
