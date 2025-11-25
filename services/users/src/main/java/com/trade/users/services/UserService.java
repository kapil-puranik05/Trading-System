package com.trade.users.services;

import com.trade.users.dtos.UserRequestDTO;
import com.trade.users.exceptions.UserAlreadyExistsException;
import com.trade.users.exceptions.UserNotFoundException;
import com.trade.users.models.AppUser;
import com.trade.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public AppUser registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        AppUser user = new AppUser();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setWalletBalance(userRequestDTO.getWalletBalance());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public AppUser getUser(UUID userId) {
        Optional<AppUser> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return user.get();
    }
}
