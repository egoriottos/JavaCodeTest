package org.example.javacodetest.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.javacodetest.domain.entity.Wallet;
import org.example.javacodetest.domain.entity.WalletTransaction;
import org.example.javacodetest.domain.enums.OperationType;
import org.example.javacodetest.repository.WalletRepository;
import org.example.javacodetest.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    public Wallet getWalletByUUID(UUID uuid) {
        return walletRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Wallet with uuid " + uuid + " not found"));
    }
    @Transactional
    public Wallet deposit( UUID uuid,BigDecimal amount) {
            var wallet = walletRepository.findById(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Wallet with uuid " + uuid + " not found"));
            wallet.setBalance(wallet.getBalance().add(amount));
            walletRepository.save(wallet);
            WalletTransaction transaction = new WalletTransaction();
            transaction.setId(UUID.randomUUID());
            transaction.setWalletId(uuid);
            transaction.setOperationType(OperationType.DEPOSIT);
            transaction.setAmount(amount);
            transaction.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
            return wallet;

    }

    @Transactional
    public Wallet withdraw( UUID uuid, BigDecimal amount) {
        var wallet = walletRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Wallet with uuid " + uuid + " not found"));
        wallet.setBalance(wallet.getBalance().subtract(amount));
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("You can't withdraw more than you have");
        }
        walletRepository.save(wallet);
        WalletTransaction transaction = new WalletTransaction();
        transaction.setId(UUID.randomUUID());
        transaction.setWalletId(uuid);
        transaction.setOperationType(OperationType.WITHDRAW);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        return wallet;
    }
}
