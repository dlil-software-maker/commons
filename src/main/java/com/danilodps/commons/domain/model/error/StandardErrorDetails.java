package com.danilodps.commons.domain.model.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StandardErrorDetails {
    private String message;
    private LocalDateTime timestamp;
}
