package org.manmet.crmapplication.service;

import org.manmet.crmapplication.config.CustomUserDetails;
import org.manmet.crmapplication.model.Permissions;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.model.User;
import org.manmet.crmapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("Attempting to find user by username: " + usernameOrEmail);
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElse(null);
        if (user == null) {
            System.out.println("Username not found, trying email: " + usernameOrEmail);
            user = userRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            System.out.println("User found by username: " + user.getUsername());
        }

        // Fetch user roles and permissions
        Set<Roles> roles = user.getRoles();
        Set<Permissions> permissions = new HashSet<>();

        for (Roles role : roles) {
            permissions.addAll(role.getPermissions()); // Accumulate permissions from all roles
        }

        // Return a CustomUserDetails object with user, roles, and permissions
        return new CustomUserDetails(user, roles, permissions);
    }
}
