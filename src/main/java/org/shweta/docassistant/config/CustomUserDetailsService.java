package org.shweta.docassistant.config;

import lombok.RequiredArgsConstructor;
import org.shweta.docassistant.exceptions.ResourceNotFoundException;
import org.shweta.docassistant.models.User;
import org.shweta.docassistant.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);

        return user.map(CustomUserDetails::new).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

    }
}
