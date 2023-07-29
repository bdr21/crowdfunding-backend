package com.example.crowdfundingapp.user;

import com.example.crowdfundingapp.exceptions.UserDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        Optional<User> userOp = userRepository.findByEmail(email);

        if (!userOp.isPresent()) {
            throw new UserDoesntExistException();
        }

        User user = userOp.get();

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                true, true, true, true,
                user.getRoleAsAuthorities()
        );
    }
}