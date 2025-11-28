package com.trades.auth.services;

import com.trades.auth.dtos.CreateUserAccountRequest;
import com.trades.auth.dtos.LoginRequestDTO;
import com.trades.auth.dtos.LoginResponse;
import com.trades.auth.dtos.SignupRequestDTO;
import com.trades.auth.exceptions.AccountCreationException;
import com.trades.auth.exceptions.InvalidCredentialsException;
import com.trades.auth.exceptions.UserAlreadyExistsException;
import com.trades.auth.models.AuthUser;
import com.trades.auth.repositories.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    @Value("${user.service.url}")
    private String url;

    @Transactional
    public AuthUser signup(SignupRequestDTO signupRequestDTO) {
        if(authRepository.existsByEmail(signupRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with provided email already exists");
        }
        AuthUser user = new AuthUser();
        user.setUsername(signupRequestDTO.getUsername());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user = authRepository.save(user);
        CreateUserAccountRequest request = new CreateUserAccountRequest();
        request.setUserId(user.getId());
        try {
            restTemplate.postForObject(url, request, Void.class);
        } catch (Exception e) {
            throw new AccountCreationException("Failed to create account for the user");
        }
        return user;
    }

    public LoginResponse login(LoginRequestDTO loginRequestDTO) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Incorrect Password");
        }
        String token = jwtService.generateToken(userDetails.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }
}
