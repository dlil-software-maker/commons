package com.danilodps.commons;

import com.danilodps.commons.domain.model.response.DepositResponse;
import com.danilodps.commons.domain.model.response.SignInResponse;
import com.danilodps.commons.domain.model.response.SignUpResponse;
import com.danilodps.commons.domain.model.response.TransactionResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Commons {
    public static void main(String[] args) {

        SignInResponse signInResponse = SignInResponse.builder()
                .id(UUID.randomUUID().toString())
                .email("teste@email.com")
                .username("teste-teste")
                .signinTimestamp(LocalDateTime.now())
                .build();

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .id(UUID.randomUUID().toString())
                .email("teste@email.com")
                .username("teste-teste")
                .signupTimestamp(LocalDateTime.now())
                .build();

        DepositResponse depositResponse = DepositResponse.builder()
                .depositId(UUID.randomUUID().toString())
                .username("teste-teste")
                .userEmail("teste@email.com")
                .amount(new BigDecimal("1000000"))
                .depositTimestamp(LocalDateTime.now())
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .transactionId(UUID.randomUUID().toString())
                .senderEmail("teste_sender@email.com")
                .receiverEmail("teste_receiver@email.com")
                .amount(new BigDecimal("1000000"))
                .transactionTimestamp(LocalDateTime.now())
                .build();

        System.out.println(signInResponse);
        System.out.println(signUpResponse);
        System.out.println(depositResponse);
        System.out.println(transactionResponse);
    }
}