package com.danilodps.commons;

import com.danilodps.commons.domain.model.response.DepositResponse;
import com.danilodps.commons.domain.model.response.SignInResponse;
import com.danilodps.commons.domain.model.response.SignUpResponse;
import com.danilodps.commons.domain.model.response.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Commons {
    private static final Logger log = LoggerFactory.getLogger(Commons.class);
    static void main(String[] args) {
        log.info("[Commons Running...]");

        SignInResponse signInResponse = new SignInResponse(
                UUID.randomUUID().toString(),
                "teste-teste",
                "teste@email.com",
                LocalDateTime.now());

        SignUpResponse signUpResponse = new SignUpResponse(
                UUID.randomUUID().toString(),
                "teste-teste",
                "teste@email.com",
                LocalDateTime.now());

        DepositResponse depositResponse = new DepositResponse(
                UUID.randomUUID().toString(),
                "teste-teste",
                "teste@email.com",
               new BigDecimal("1000000"),
               LocalDateTime.now());

        TransactionResponse transactionResponse = new TransactionResponse(
                UUID.randomUUID().toString(),
                new BigDecimal("1000000"),
                LocalDateTime.now(),
                "teste_sender@email.com",
                "teste_receiver@email.com");


        log.info("[signInResponse] {}", signInResponse);
        log.info("[signUpResponse] {}", signUpResponse);
        log.info("[depositResponse] {}", depositResponse);
        log.info("[transactionResponse] {}", transactionResponse);
    }

}