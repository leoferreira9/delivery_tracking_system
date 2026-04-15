package com.leonardo.delivery_tracking_system.exception;

public class FailedToUpdateDeliveryStatusException extends RuntimeException {
    public FailedToUpdateDeliveryStatusException(String message) {
        super(message);
    }
}
