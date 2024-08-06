package org.example.javacodetest.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.javacodetest.domain.enums.OperationType;
import org.example.javacodetest.request.WalletOperationRequest;
import org.example.javacodetest.response.WalletResponse;
import org.example.javacodetest.services.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final ModelMapper modelMapper;

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable UUID WALLET_UUID) {
        if(WALLET_UUID == null) {
            return ResponseEntity.badRequest().build();
        }
        var response = walletService.getWalletByUUID(WALLET_UUID);
        return ResponseEntity.ok(modelMapper.map(response, WalletResponse.class));
    }

    @PostMapping("/wallet")
    public ResponseEntity<WalletResponse> operation(@RequestBody WalletOperationRequest walletOperationRequest) throws BadRequestException {
        if (walletOperationRequest.getOperationType() == OperationType.DEPOSIT) {
            var response = walletService.deposit(walletOperationRequest.getWalletId(), walletOperationRequest.getAmount());
            return ResponseEntity.ok(modelMapper.map(response, WalletResponse.class));
        } else if (walletOperationRequest.getOperationType() == OperationType.WITHDRAW) {
            var response = walletService.withdraw(walletOperationRequest.getWalletId(), walletOperationRequest.getAmount());
            return ResponseEntity.ok(modelMapper.map(response, WalletResponse.class));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
