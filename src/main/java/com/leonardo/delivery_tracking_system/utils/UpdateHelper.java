package com.leonardo.delivery_tracking_system.utils;

public class UpdateHelper {
    public static <T> T getIfNotNull(T newValue, T currentValue){
        return newValue != null ? newValue : currentValue;
    }
}
