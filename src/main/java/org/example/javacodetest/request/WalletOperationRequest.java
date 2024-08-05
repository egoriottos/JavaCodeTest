package org.example.javacodetest.request;

import lombok.Data;
import org.example.javacodetest.domain.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletOperationRequest {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
