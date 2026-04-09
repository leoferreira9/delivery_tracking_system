package com.leonardo.delivery_tracking_system.exception;

public class EntityAlreadyRegisteredException extends RuntimeException {
    public EntityAlreadyRegisteredException(String message) {
        super(message);
    }
}
