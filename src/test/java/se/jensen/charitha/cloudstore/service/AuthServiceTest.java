package se.jensen.charitha.cloudstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import se.jensen.charitha.cloudstore.dto.AuthResponseDto;
import se.jensen.charitha.cloudstore.dto.LoginRequestDto;
import se.jensen.charitha.cloudstore.dto.RegisterRequestDto;
import se.jensen.charitha.cloudstore.model.User;
import se.jensen.charitha.cloudstore.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder);
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUserWhenRequestIsValid() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("secret");

        AuthResponseDto response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registration successful.");

        User savedUser = userRepository.findByUsername("testuser").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(passwordEncoder.matches("secret", savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldReturnErrorWhenLoginCredentialsAreInvalid() {
        String rawPassword = "secret";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userRepository.save(new User("testuser", "test@example.com", encodedPassword, "ROLE_USER"));

        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("testuser");
        request.setPassword("wrongpass");

        AuthResponseDto response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Invalid username or password.");
    }
}
