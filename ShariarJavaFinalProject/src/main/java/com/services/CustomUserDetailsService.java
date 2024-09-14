/*
 * package com.services; import java.util.Optional; import
 * java.util.stream.Collectors;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
 * org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.stereotype.Service;
 * 
 * import com.models.User; import com.repositories.registration.UserRepository;
 * 
 * @Service public class CustomUserDetailsService implements UserDetailsService
 * {
 * 
 * @Autowired private UserRepository userRepository;
 * 
 * @Override public UserDetails loadUserByUsername(String username) throws
 * UsernameNotFoundException { Optional<User> user =
 * userRepository.findByEmail(username); if (user == null) { throw new
 * UsernameNotFoundException("User not found"); } return new
 * org.springframework.security.core.userdetails.User( user.getEmail(),
 * user.getPassword(), user.getRoles().stream() .map(role -> new
 * SimpleGrantedAuthority(role.name())) .collect(Collectors.toList()) ); } }
 */