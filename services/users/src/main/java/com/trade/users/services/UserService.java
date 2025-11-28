package com.trade.users.services;

import com.trade.users.dtos.UserRequestDTO;
import com.trade.users.dtos.WalletUpdateDTO;
import com.trade.users.exceptions.InsufficientFundsException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LedgerRepository ledgerRepository;

    public void createUser(UserRequestDTO userRequestDTO) {
        AppUser user = new AppUser();
        user.setUserId(userRequestDTO.getUserId());
        user.setWalletBalance(BigDecimal.ZERO);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
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
