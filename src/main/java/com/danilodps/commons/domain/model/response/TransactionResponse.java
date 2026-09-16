package com.danilodps.commons.domain.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(String transactionId, BigDecimal amount, LocalDateTime transactionTimestamp, String senderEmail, String receiverEmail) { }
