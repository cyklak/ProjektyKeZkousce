package org.example.models.services;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.Role;
import org.example.models.dto.UserDTO;
import org.example.models.exceptions.DuplicateEmailException;
import org.example.models.exceptions.PasswordsDoNotEqualException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.models.dto.Role.POJISTENY;
import static org.example.models.dto.Role.POJISTNIK;

@Service
public class UserServiceImpl implements UserService {
    private Random random;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(UserDTO user) {
        if (!user.getPassword().equals(user.getConfirmPassword()))
            throw new PasswordsDoNotEqualException();

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> role = new ArrayList<>();
        role.add(POJISTNIK);
        userEntity.setRole(role);
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
            random = new Random();
            int a = random.nextInt(127);
            if (a<33)
                a = a + 33;
            char znak = (char)a;
            heslo = heslo + znak;
        }
       return heslo;
    }
    @Override
    public UserEntity createPojistenec(PojistenecDTO pojistenec, String password) {

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(pojistenec.getEmail());
        userEntity.setPassword(passwordEncoder.encode(password));
        List<Role> role = new ArrayList<>();
        role.add(POJISTENY);
        userEntity.setRole(role);
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
