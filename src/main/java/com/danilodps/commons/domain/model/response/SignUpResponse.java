package com.danilodps.commons.domain.model.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SignUpResponse(String id, String username, String email, LocalDateTime signupTimestamp){}