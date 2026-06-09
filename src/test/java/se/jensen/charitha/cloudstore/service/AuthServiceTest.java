package se.jensen.charitha.cloudstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.charitha.cloudstore.dto.AuthResponseDto;
import se.jensen.charitha.cloudstore.dto.LoginRequestDto;
import se.jensen.charitha.cloudstore.dto.RegisterRequestDto;
import se.jensen.charitha.cloudstore.model.User;
import se.jensen.charitha.cloudstore.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, authenticationManager);
    }

    @Test
    void shouldRegisterNewUserWhenRequestIsValid() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("secret");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");

        AuthResponseDto response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registration successful.");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-secret");
        assertThat(savedUser.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void shouldReturnErrorWhenLoginCredentialsAreInvalid() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("testuser");
        request.setPassword("wrongpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("invalid credentials"));

        AuthResponseDto response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Invalid username or password.");
    }
}
