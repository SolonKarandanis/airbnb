package com.solon.airbnb.email.domain;

public enum EmailStatus {
    PENDING("PENDING"),
    FAILED("FAILED"),
    SENT("SENT");

    private final String value;

    private EmailStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
