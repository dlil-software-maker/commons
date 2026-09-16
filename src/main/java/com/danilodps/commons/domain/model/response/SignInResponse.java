package com.danilodps.commons.domain.model.response;

import java.time.LocalDateTime;

public record SignInResponse (String id, String username, String email, LocalDateTime signinTimestamp) {}
