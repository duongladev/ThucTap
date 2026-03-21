package org.example.tuan3.security;

import lombok.RequiredArgsConstructor;
import org.example.tuan3.entity.UserEntity;
import org.example.tuan3.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return CustomUserDetails.from(user);
    }
}