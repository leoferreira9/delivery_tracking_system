package com.leonardo.delivery_tracking_system.dto.error;

import java.time.Instant;

public record ErrorResponse(int status, String error, String message, Instant timestamp, String path) {}
