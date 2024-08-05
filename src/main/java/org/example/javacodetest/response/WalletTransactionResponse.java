package org.example.javacodetest.response;

import lombok.Data;
import org.example.javacodetest.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WalletTransactionResponse {
    private UUID id;
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
