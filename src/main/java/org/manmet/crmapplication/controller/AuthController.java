package org.manmet.crmapplication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.manmet.crmapplication.config.CustomUserDetails;
import org.manmet.crmapplication.customexceptionhandler.EmailAlreadyExistsException;
import org.manmet.crmapplication.customexceptionhandler.UsernameAlreadyExistsException;
import org.manmet.crmapplication.dto.LoginRequestdto;
import org.manmet.crmapplication.dto.SignupRequestdto;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.model.User;
import org.manmet.crmapplication.repository.RolesRepository;
import org.manmet.crmapplication.service.UserService;
import org.manmet.crmapplication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RolesRepository rolesRepository; // Inject RoleRepository


    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, RolesRepository rolesRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.rolesRepository = rolesRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestdto signupRequestDto, HttpServletResponse response) {

        try {
            // Convert SignupRequestdto to User
            User user = new User();
            user.setFirstName(signupRequestDto.getFirstName());
            user.setLastName(signupRequestDto.getLastName());
            user.setEmail(signupRequestDto.getEmail());
            user.setUsername(signupRequestDto.getUsername());
            user.setPassword(signupRequestDto.getPassword()); // Password will be encoded in UserService

            // Fetch the role from the database based on the role string in the DTO
            String roleName = signupRequestDto.getRole().toUpperCase();
            Roles role = rolesRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));

            // Assign role to user
            user.getRoles().add(role);

            userService.signUpUser(user, roleName);

            // Return success message without token
            return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid LoginRequestdto loginRequestDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );

            // Extract the CustomUserDetails object
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Convert Set<Roles> to Set<String>
            Set<String> roleNames = userDetails.getRoles().stream()
                     .map(Roles::getRoleName) // Extract the role names
                     .collect(Collectors.toSet());

            // Generate the token with username and permissions
            String token = jwtUtil.generateToken(userDetails.getUsername(), roleNames, userDetails.getPermissions());
            log.info("Generated JWT token: {}", token);
            log.info("User authenticated successfully: {}", loginRequestDto.getUsername());

            // Retrieve the user's roles
            User user = userService.findByUsername(loginRequestDto.getUsername());
            List<String> roles = user.getRoles().stream()
                    .map(Roles::getRoleName)
                    .toList();

            // Store the token in a cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            log.info("JWT Cookie added: {}", cookie.getValue());
            return ResponseEntity.ok().body(Map.of(
                    "message", "User authenticated successfully",
                    "roles", roles
            ));


        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", loginRequestDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authentication failed"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        // Server-side cookie deletion for JWT
        Cookie cookie = new Cookie("token", null);  // Replace "token" with your JWT cookie name
        cookie.setPath("/");  // Ensure the path matches where the cookie was set
        cookie.setHttpOnly(true);  // Optional: HttpOnly flag based on your cookie settings
        cookie.setMaxAge(0);  // Set maxAge to 0 to delete the cookie
        response.addCookie(cookie);  // Add the expired cookie to the response

        // Optionally, delete any other session-related cookies like JSESSIONID
        Cookie jsessionidCookie = new Cookie("JSESSIONID", null);
        jsessionidCookie.setPath("/");
        jsessionidCookie.setMaxAge(0);
        response.addCookie(jsessionidCookie);

        return ResponseEntity.ok("User logged out successfully");
    }
}
