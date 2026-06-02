package se.jensen.charitha.cloudstore.service;

import se.jensen.charitha.cloudstore.dto.AuthResponseDto;
import se.jensen.charitha.cloudstore.dto.LoginRequestDto;
import se.jensen.charitha.cloudstore.dto.RegisterRequestDto;
import se.jensen.charitha.cloudstore.model.User;
import se.jensen.charitha.cloudstore.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDto register(RegisterRequestDto request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            return new AuthResponseDto(false, "Username is required.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return new AuthResponseDto(false, "Password is required.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponseDto(false, "Username already exists.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), request.getEmail(), encodedPassword, "ROLE_USER");
        userRepository.save(user);
        return new AuthResponseDto(true, "Registration successful.");
    }

    public AuthResponseDto login(LoginRequestDto request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            return new AuthResponseDto(false, "Username is required.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return new AuthResponseDto(false, "Password is required.");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return new AuthResponseDto(true, "Login successful.");
        } catch (AuthenticationException e) {
            return new AuthResponseDto(false, "Invalid username or password.");
        }
    }
}
