package org.example.tuan3.security;

import lombok.Getter;
import org.example.tuan3.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer id;
    private final String email;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            Integer id,
            String email,
            String password,
            boolean active,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
    }

    public static CustomUserDetails from(UserEntity user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Boolean.TRUE.equals(user.getActive()),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return email;
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
        return active;
    }
}