package com.xplore.interviewer.entity;

public enum SlotStatus {
    AVAILABLE,
    BOOKED,
    RESERVED,
    COMPLETED,
    SCHEDULED;

    public static boolean isValid(String status) {
        try {
            SlotStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}