package org.example.models.services;

import org.example.data.entities.UserEntity;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void create(UserDTO user);

    UserEntity createPojistenec(PojistenecDTO pojistenec, String password);

    String generatePassword ();

}
