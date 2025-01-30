package org.manmet.crmapplication.service;

import org.manmet.crmapplication.customexceptionhandler.EmailAlreadyExistsException;
import org.manmet.crmapplication.customexceptionhandler.UsernameAlreadyExistsException;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.model.User;
import org.manmet.crmapplication.repository.RolesRepository;
import org.manmet.crmapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return an optional containing the user if found, empty otherwise.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Saves a user to the repository.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * Signs up a new user with the given information and assigns a role.
     *
     * @param user the user to sign up.
     * @return the signed-up user.
     * @throws UsernameAlreadyExistsException if the username already exists.
     * @throws EmailAlreadyExistsException    if the email is already in use.
     * @throws RoleNotFoundException          if the specified role is not found.
     */
    public User signUpUser(User user, String roleName) throws RoleNotFoundException {
        checkIfUsernameOrEmailExists(user);

        encodePassword(user);
        assignRole(user, roleName);

        User savedUser = userRepository.save(user);
        logger.info("User saved: " + savedUser); // Use logging instead of System.out.println
        return savedUser;
    }

    private void checkIfUsernameOrEmailExists(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use!");
        }
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void assignRole(User user, String roleName) throws RoleNotFoundException {
        Roles role = rolesRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));

        Set<Roles> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setEnabled(true);
    }
}