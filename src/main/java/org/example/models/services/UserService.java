package org.example.models.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.Role;
import org.example.models.dto.UserDTO;
import org.example.models.exceptions.DuplicateEmailException;
import org.example.models.exceptions.PasswordsDoNotEqualException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.models.dto.Role.*;
/**
 * lombok generated constructor throws IllegalArgumentException that prevents null values in constructor arguments
 */
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final PasswordEncoder passwordEncoder;


    /** creates a new user and saves them into UserRepository, this method is called during registration of new policyholders
     * @param user
     */
    public void create(UserDTO user) {
        if (!user.getPassword().equals(user.getConfirmPassword()))
            throw new PasswordsDoNotEqualException();

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = new ArrayList<>();
        roles.add(POLICYHOLDER);
        userEntity.setRoles(roles);
        userEntity.setAdmin(false);

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    /**
     * @return password for a new user
     */
    public String generatePassword () {
        String password = "";
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int a = random.nextInt(127);
            if (a<33)
                a = a + 33;
            char character = (char)a;
            password = password + character;
        }
       return password;
    }

    /** creates a new user and saves them into UserRepository, this method is called when a policyholder creates a new insured person who is different from the policyholder
     * @param insured
     * @param password
     * @return a new userEntity
     */
    public UserEntity createInsured(InsuredDTO insured, String password) {

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(insured.getEmail());
        userEntity.setPassword(passwordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        roles.add(INSURED);
        userEntity.setRoles(roles);
        userEntity.setAdmin(false);

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
        return userEntity;
    }

    /**
     * @param id
     * @return email of a selected policyholder
     */
    public String getPolicyholderEmail(Long id) {
       return userRepository.findById(id).orElseThrow().getEmail();
    }

    /**
     * @param username
     * @return details of a user selected by their username
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username, " + username + " not found"));
    }
}
