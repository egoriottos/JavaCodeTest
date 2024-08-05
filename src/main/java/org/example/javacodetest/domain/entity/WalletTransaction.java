package org.example.javacodetest.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.javacodetest.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="WalletTransaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransaction {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID walletId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
