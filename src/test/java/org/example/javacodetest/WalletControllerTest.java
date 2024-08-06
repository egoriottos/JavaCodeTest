package org.example.javacodetest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javacodetest.controller.WalletController;
import org.example.javacodetest.domain.entity.Wallet;
import org.example.javacodetest.domain.enums.OperationType;
import org.example.javacodetest.request.WalletOperationRequest;
import org.example.javacodetest.response.WalletResponse;
import org.example.javacodetest.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(WalletController.class)
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalletService walletService;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModelMapper modelMapper;
    private Wallet wallet;

    @Test
    void findOneShouldReturnWallet() throws Exception {
        wallet = new Wallet(UUID.randomUUID(), new BigDecimal(1000));
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setId(wallet.getId());
        walletResponse.setBalance(wallet.getBalance());
        Mockito.when(walletService.getWalletByUUID(wallet.getId())).thenReturn(wallet);
        Mockito.when(modelMapper.map(wallet, WalletResponse.class)).thenReturn(walletResponse);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/wallets/" + wallet.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(wallet.getBalance().intValue()));
    }


    @Test
    void shouldDepositMoney() throws Exception {
        wallet = new Wallet(UUID.randomUUID(), new BigDecimal(1000));

        WalletOperationRequest depositRequest = new WalletOperationRequest();
        depositRequest.setOperationType(OperationType.DEPOSIT);
        depositRequest.setAmount(new BigDecimal(1000));
        depositRequest.setWalletId(wallet.getId());

        WalletResponse depositResponse = new WalletResponse();
        depositResponse.setId(wallet.getId());
        depositResponse.setBalance(wallet.getBalance().add(depositRequest.getAmount()));
        Mockito.when(walletService.deposit(wallet.getId(), depositRequest.getAmount())).thenReturn(wallet);
        Mockito.when(modelMapper.map(wallet, WalletResponse.class)).thenReturn(depositResponse);
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(depositResponse.getId().toString()))
                .andExpect(jsonPath("$.balance").value(depositResponse.getBalance().intValue()));
    }

    @Test
    void shouldWithdrawMoney() throws Exception {
        wallet = new Wallet(UUID.randomUUID(), new BigDecimal(1000));

        WalletOperationRequest withdrawRequest = new WalletOperationRequest();
        withdrawRequest.setOperationType(OperationType.DEPOSIT);
        withdrawRequest.setAmount(new BigDecimal(1000));
        withdrawRequest.setWalletId(wallet.getId());

        WalletResponse withdrawResponse = new WalletResponse();
        withdrawResponse.setId(wallet.getId());
        withdrawResponse.setBalance(wallet.getBalance().subtract(withdrawRequest.getAmount()));
        Mockito.when(walletService.deposit(wallet.getId(), withdrawRequest.getAmount())).thenReturn(wallet);
        Mockito.when(modelMapper.map(wallet, WalletResponse.class)).thenReturn(withdrawResponse);
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(withdrawResponse.getId().toString()))
                .andExpect(jsonPath("$.balance").value(withdrawResponse.getBalance().intValue()));
    }

    @Test
    void shouldThrowBadRequestForInvalidOperation() throws Exception {
        WalletOperationRequest invalidRequest = new WalletOperationRequest();
        invalidRequest.setWalletId(UUID.randomUUID());
        invalidRequest.setAmount(new BigDecimal("100.00"));
        invalidRequest.setOperationType(null);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestForNullUUID() throws Exception {
        wallet = new Wallet(null, new BigDecimal(1000));
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setId(wallet.getId());
        walletResponse.setBalance(wallet.getBalance());
        Mockito.when(walletService.getWalletByUUID(wallet.getId())).thenReturn(wallet);
        Mockito.when(modelMapper.map(wallet, WalletResponse.class)).thenReturn(walletResponse);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/wallets/" + null));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestForInvalidUUID() throws Exception {
        WalletOperationRequest invalidRequest = new WalletOperationRequest();
        invalidRequest.setWalletId(null);
        invalidRequest.setAmount(new BigDecimal("100.00"));
        invalidRequest.setOperationType(null);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}
