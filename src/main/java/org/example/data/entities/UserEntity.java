package org.example.data.entities;

import jakarta.persistence.*;
import org.example.models.dto.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.models.dto.Roles.*;


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
    private List<Roles> roles;

    @Column(nullable = false)
    private boolean admin;

    @OneToOne (mappedBy = "user")
    private InsuredEntity pojistenec;

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
        if (roles.contains(POJISTNIK))
            authorities.add(new SimpleGrantedAuthority("ROLE_POJISTNIK"));
        if (roles.contains(POJISTENY))
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

    public InsuredEntity getPojistenec() {
        return pojistenec;
    }

    public void setPojistenec(InsuredEntity pojistenec) {
        this.pojistenec = pojistenec;
    }

    public List<Roles> getRole() {
        return roles;
    }

    public void setRole(List<Roles> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
// endregion


}