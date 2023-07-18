package org.example.models.services;

import org.example.data.entities.UserEntity;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.Roles;
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

import static org.example.models.dto.Roles.POJISTENY;
import static org.example.models.dto.Roles.POJISTNIK;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void create(UserDTO user) {
        if (!user.getPassword().equals(user.getConfirmPassword()))
            throw new PasswordsDoNotEqualException();

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Roles> roles = new ArrayList<>();
        roles.add(POJISTNIK);
        userEntity.setRole(roles);
        userEntity.setAdmin(false);

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }
    public String generatePassword () {
        String heslo = "";
        for (int i = 0; i < 7; i++) {
            Random random = new Random();
            int a = random.nextInt(127);
            if (a<33)
                a = a + 33;
            char znak = (char)a;
            heslo = heslo + znak;
        }
       return heslo;
    }

    public UserEntity createPojistenec(InsuredDTO pojistenec, String password) {

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(pojistenec.getEmail());
        userEntity.setPassword(passwordEncoder.encode(password));
        List<Roles> roles = new ArrayList<>();
        roles.add(POJISTENY);
        userEntity.setRole(roles);
        userEntity.setAdmin(false);

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username, " + username + " not found"));
    }
}
