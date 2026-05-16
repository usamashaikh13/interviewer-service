package com.xplore.interviewer.entity;

public enum SlotStatus {
    AVAILABLE,
    BOOKED,
    RESERVED,
    COMPLETED,
    SCHEDULED,
    CANCELLED,
    NO_SHOW;

    public static boolean isValid(String status) {
        try {
            SlotStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
