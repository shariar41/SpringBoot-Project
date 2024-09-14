/*
 * package com.controllers; import com.models.AuthenticationResponse; import
 * com.models.Role; import com.models.User; import
 * com.repositories.registration.UserRepository; import
 * com.services.EmailService; import com.services.UserService;
 * 
 * import jakarta.validation.Valid;
 * 
 * import java.util.HashMap; import java.util.List; import java.util.Map; import
 * java.util.Optional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.MediaType; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.security.core.AuthenticationException; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.web.bind.annotation.CrossOrigin; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * 
 * @RestController
 * 
 * @RequestMapping("/api")
 * 
 * @CrossOrigin(origins = "http://localhost:4200") public class UserController {
 * 
 * @Autowired private UserService userService;
 * 
 * @Autowired private UserRepository userRepository;
 * 
 * @Autowired private EmailService emailService ;
 * 
 * @Autowired private AuthenticationManager authenticationManager;
 * 
 * @Autowired private PasswordEncoder passwordEncoder;
 * 
 * @GetMapping("/home") public String home() { // Retrieve the currently
 * authenticated user Authentication authentication =
 * SecurityContextHolder.getContext().getAuthentication(); String username =
 * authentication != null ? authentication.getName() : "Guest";
 * 
 * // Return a simple message or user-specific data return "Welcome, " +
 * username + "!"; }
 * 
 * // GET method to retrieve all users
 * 
 * @GetMapping("/users") public ResponseEntity<List<User>> getAllUsers() {
 * List<User> users = userService.getAllUsers(); return new
 * ResponseEntity<>(users, HttpStatus.OK); }
 * 
 * // GET method to retrieve a user by ID
 * 
 * @GetMapping("/users/{id}") public ResponseEntity<User>
 * getUserById(@PathVariable("id") Long id) { Optional<User> user =
 * userService.getUserById(id); return user.map(u -> new ResponseEntity<>(u,
 * HttpStatus.OK)) .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); }
 * 
 * @PostMapping("/register") public ResponseEntity<Map<String, String>>
 * registerUser(@Valid @RequestBody User request) { Map<String, String> response
 * = new HashMap<>(); if (userRepository.findByEmail(request.getEmail()) !=
 * null) { response.put("message", "Email is already taken.");
 * emailService.sendEmail(request.getEmail(), "Registration Error",
 * "The email address is already taken."); return
 * ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
 * response); } User user = User.builder() .fullName(request.getFullName())
 * .email(request.getEmail()) .phone(request.getPhone())
 * .address(request.getAddress()) .userType(request.getUserType())
 * .password(passwordEncoder.encode(request.getPassword())) .build();
 * userRepository.save(user); response.put("message",
 * "User registered successfully."); emailService.sendEmail(request.getEmail(),
 * "Registration Successful", "You have been registered successfully."); return
 * ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response); }
 * 
 * 
 * 
 * @PostMapping("/login") public ResponseEntity<String> loginUser(@RequestParam
 * String email, @RequestParam String password) { try { Authentication
 * authentication = authenticationManager.authenticate( new
 * UsernamePasswordAuthenticationToken(email, password) ); return
 * ResponseEntity.ok("Login successful"); } catch (AuthenticationException e) {
 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " +
 * e.getMessage()); } }
 * 
 * @PostMapping("/login") public ResponseEntity<AuthenticationResponse>
 * loginUser(@RequestParam String email, @RequestParam String password) { try {
 * Authentication authentication = authenticationManager.authenticate( new
 * UsernamePasswordAuthenticationToken(email, password) );
 * 
 * // Set the authentication in the security context
 * SecurityContextHolder.getContext().setAuthentication(authentication);
 * 
 * // Generate JWT token String jwtToken =
 * jwtTokenService.generateToken(authentication);
 * 
 * // Retrieve user's role String role =
 * authentication.getAuthorities().iterator().next().getAuthority();
 * 
 * // Prepare response with token, email, and role AuthenticationResponse
 * response = AuthenticationResponse.builder() .message("Login successful")
 * .token(jwtToken) .email(email) .role(Role.valueOf(role)) .build();
 * 
 * return ResponseEntity.ok(response); } catch (AuthenticationException e) {
 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
 * AuthenticationResponse.builder() .message("Login failed: " + e.getMessage())
 * .build() ); } }
 * 
 * }
 */

package com.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.User;
import com.services.UserService;

import java.util.List;

@RequestMapping("api/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/myprofile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }
 // Update profile method
    @PutMapping("/myprofile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateProfile(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Delegate the update logic to the UserService
        User updatedUser = userService.updateUserProfile(currentUser.getId(), user);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}