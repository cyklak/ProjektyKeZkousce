package org.example.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.models.dto.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.models.dto.Role.*;


@Entity
@Getter
@Setter
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private List<Role> roles;

    @Column(nullable = false)
    private boolean admin;

    @OneToOne (mappedBy = "user")
    private InsuredEntity insured;

    // region: UserDetails Methods
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return list of roles of the current user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (admin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));}
        else {
        if (roles.contains(POLICYHOLDER))
            authorities.add(new SimpleGrantedAuthority("ROLE_POLICYHOLDER"));
        if (roles.contains(INSURED))
            authorities.add(new SimpleGrantedAuthority("ROLE_INSURED"));}

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}