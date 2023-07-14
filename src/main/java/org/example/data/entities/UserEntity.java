package org.example.data.entities;

import jakarta.persistence.*;
import org.example.models.dto.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.example.models.dto.Role.*;


@Entity
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private List<Role> role;

    @Column(nullable = false)
    private boolean admin;

    @OneToOne (mappedBy = "user")
    private PojistenecEntity pojistenec;

    // region: UserDetails Methods
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (admin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));}
        else {
        if (role.contains(POJISTNIK))
            authorities.add(new SimpleGrantedAuthority("ROLE_POJISTNIK"));
        if (role.contains(POJISTENY))
            authorities.add(new SimpleGrantedAuthority("ROLE_POJISTENY"));}

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

    // region: Getters and Setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // public String getPassword() {
    //  return password;
    //}

    public void setPassword(String password) {
        this.password = password;
    }

    public PojistenecEntity getPojistenec() {
        return pojistenec;
    }

    public void setPojistenec(PojistenecEntity pojistenec) {
        this.pojistenec = pojistenec;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
// endregion


}