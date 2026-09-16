package com.danilodps.commons.domain.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DepositResponse(String depositId, String username, String userEmail, BigDecimal amount, LocalDateTime depositTimestamp) {
}
