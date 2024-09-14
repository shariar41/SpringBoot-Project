package com.config;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

//import com.services.CustomUserDetailsService;
//import com.utils.JwtAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
        ) {
            this.authenticationProvider = authenticationProvider;
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

//    @Autowired
//    private UserDetailsService userDetailsService;

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers("/").permitAll()  // Publicly accessible endpoints
//                .requestMatchers("/api/register").permitAll() 
//                .requestMatchers("/api/login").permitAll() 
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Admin-specific endpoints
//                .anyRequest().authenticated())
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(Customizer.withDefaults());

        http
                .csrf(csrf -> csrf
                                .disable()  // Disable CSRF for APIs; not recommended for all use cases.
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/public/**").permitAll()  // Public endpoints
                                .requestMatchers("/api/auth/signup").permitAll()
                                .requestMatchers("/api/auth/login").permitAll()  // Allow access to the login page
                                .requestMatchers("/api/auth/logout").authenticated()  // Allow access to the logout endpoint
                                .requestMatchers("/api/users").hasRole("ADMIN")  // Only ADMIN can access
                                .requestMatchers("/api/**").authenticated()  // Protect all other endpoints
                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/api/auth/login")  // Custom login page URL
//                                .loginProcessingUrl("/api/auth/login")  // URL for form submission
//                                .defaultSuccessUrl("/api/home", true)  // Redirect to home on successful login
//                                .failureUrl("/api/auth/login?error")  // Redirect to login with error query parameter on failure
//                                .permitAll()
//                )
                .logout(logout ->
                        logout
                                .logoutUrl("/api/auth/logout")  // URL for logout request
                                .logoutSuccessUrl("/api/auth/login?logout")  // Redirect to login with logout query parameter on success
                                .invalidateHttpSession(true)  // Invalidate session on logout
                                .deleteCookies("JSESSIONID")  // Remove session cookie on logout
                                .permitAll()
                )
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management
                )
                .authenticationProvider(authenticationProvider) // Custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS configuration

		/*
		 * http .csrf(csrf -> csrf.disable()) .authorizeHttpRequests(authorizeRequests
		 * -> authorizeRequests .requestMatchers("/api/**").permitAll() // Adjust
		 * according to your endpoints .anyRequest().authenticated() )
		 * .formLogin(formLogin -> formLogin .loginPage("/api/login") // Endpoint for
		 * the login page .loginProcessingUrl("/api/login") // Endpoint for processing
		 * login .defaultSuccessUrl("/api/home", true) // Redirect to home on successful
		 * login .failureUrl("/api/login?error") // Redirect to login with error on
		 * failure .permitAll() ) .logout(logout -> logout .logoutUrl("/api/logout") //
		 * Endpoint for logout .logoutSuccessUrl("/api/login?logout") // Redirect to
		 * login on successful logout .invalidateHttpSession(true)
		 * .deleteCookies("JSESSIONID") .permitAll() );
		 */

        return http.build();
    }

//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    UserDetailsService userDetailsService() {
//	        // Define your UserDetailsService implementation here
//	        return new CustomUserDetailsService();  // Replace with your actual implementation
//	    }

//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8005"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}