package com.trade.users.repositories;

import com.trade.users.models.WalletLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LedgerRepository extends JpaRepository<WalletLedger, UUID> {
}
