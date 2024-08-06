package org.example.javacodetest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.javacodetest.domain.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletOperationRequest {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
