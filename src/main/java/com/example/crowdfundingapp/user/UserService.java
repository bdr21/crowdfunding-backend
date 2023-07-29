package com.example.crowdfundingapp.user;

import com.example.crowdfundingapp.dto.SignUpRequest;
import com.example.crowdfundingapp.dto.LoginRequest;
import com.example.crowdfundingapp.exceptions.LoginFailException;
import com.example.crowdfundingapp.exceptions.PasswordsNotMatchingException;
import com.example.crowdfundingapp.exceptions.UserAlreadyExistsException;
import com.example.crowdfundingapp.exceptions.UserDoesntExistException;
import com.example.crowdfundingapp.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository entityRepo;

    @Autowired
    public UserService(UserRepository entityRepo) {
        this.entityRepo = entityRepo;
    }

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String confirmPassword = signUpRequest.getConfirmPassword();
        String firstName = signUpRequest.getFirstName();
        String lastName = signUpRequest.getLastName();

        boolean isUserExist = entityRepo.existsByEmail(signUpRequest.getEmail());

        if (isUserExist) {
            throw new UserAlreadyExistsException();
        }

        if (!password.equals(confirmPassword)) {
            throw new PasswordsNotMatchingException();
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User(email, firstName, lastName, encodedPassword, "USER");

        entityRepo.save(newUser);
    }

    public String login(LoginRequest loginRequest, HttpServletResponse res) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Optional<User> userOp = entityRepo.findByEmail(email);

        if (!userOp.isPresent()) {
            throw new UserDoesntExistException();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception e) {
            System.out.println("==>"+e.getMessage());
            System.out.println("==>"+e.getCause());
            e.printStackTrace();
            throw new LoginFailException();
        }

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        String token = jwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(10 * 60 * 60);
        res.addCookie(cookie);

        return token;
    }

    public List<User> getAll() {
        return entityRepo.findAll();
    }

    public Optional<User> getById(Long id) {
        return entityRepo.findById(id);
    }

    public User add(User u) {
        return entityRepo.save(u);
    }

    public User update(User u) {
        return entityRepo.save(u);
    }

    public void deleteById(Long id) {
        entityRepo.deleteById(id);
    }
}
