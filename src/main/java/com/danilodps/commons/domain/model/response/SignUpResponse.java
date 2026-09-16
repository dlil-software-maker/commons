package com.danilodps.commons.domain.model.response;

import java.time.LocalDateTime;

public record SignUpResponse(String id, String username, String email, LocalDateTime signupTimestamp) {}