package com.trades.auth.services;

import com.trades.auth.dtos.CreateUserAccountRequest;
import com.trades.auth.dtos.SignupRequestDTO;
import com.trades.auth.exceptions.AccountCreationException;
import com.trades.auth.exceptions.UserAlreadyExistsException;
import com.trades.auth.models.AppUser;
import com.trades.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String url;

    @Transactional
    public AppUser signup(SignupRequestDTO signupRequestDTO) {
        if(userRepository.existsByEmail(signupRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with provided email already exists");
        }
        AppUser user = new AppUser();
        user.setUsername(signupRequestDTO.getUsername());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(signupRequestDTO.getPassword());
        user = userRepository.save(user);
        CreateUserAccountRequest request = new CreateUserAccountRequest();
        request.setUserId(user.getId());
        try {
            restTemplate.postForObject(url, request, Void.class);
        } catch (Exception e) {
            throw new AccountCreationException("Failed to create account for the user");
        }
        return user;
    }
}
