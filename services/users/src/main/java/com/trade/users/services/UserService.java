package com.trade.users.services;

import com.trade.users.dtos.UserRequestDTO;
import com.trade.users.dtos.WalletUpdateDTO;
import com.trade.users.exceptions.InsufficientFundsException;
import com.trade.users.exceptions.UserAlreadyExistsException;
import com.trade.users.exceptions.UserNotFoundException;
import com.trade.users.models.AppUser;
import com.trade.users.models.WalletLedger;
import com.trade.users.repositories.LedgerRepository;
import com.trade.users.repositories.UserRepository;
import com.trade.users.util.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LedgerRepository ledgerRepository;

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

    @Transactional
    public void updateWallet(WalletUpdateDTO walletUpdate) {
        WalletLedger walletLedger = new WalletLedger();
        walletLedger.setUserId(walletUpdate.getUserId());
        walletLedger.setAmount(walletUpdate.getCost());
        walletLedger.setType(walletUpdate.getType());
        walletLedger.setReason(walletUpdate.getReason());
        walletLedger.setCreatedAt(LocalDateTime.now());
        Optional<AppUser> userOptional = userRepository.findById(walletUpdate.getUserId());
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        AppUser existingUser = getAppUser(walletUpdate, userOptional);
        ledgerRepository.save(walletLedger);
        userRepository.save(existingUser);
    }

    private static AppUser getAppUser(WalletUpdateDTO walletUpdate, Optional<AppUser> userOptional) {
        AppUser existingUser = userOptional.get();
        if(walletUpdate.getType().equals(Type.DEPOSIT)) {
            existingUser.setWalletBalance(existingUser.getWalletBalance().add(walletUpdate.getCost()));
        } else {
            if (existingUser.getWalletBalance().compareTo(walletUpdate.getCost()) < 0) {
                throw new InsufficientFundsException("Insufficient balance");
            }
            existingUser.setWalletBalance(existingUser.getWalletBalance().subtract(walletUpdate.getCost()));
        }
        return existingUser;
    }
}
