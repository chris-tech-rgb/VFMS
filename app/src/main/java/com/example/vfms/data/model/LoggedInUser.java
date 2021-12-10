package com.example.vfms.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    @SuppressWarnings("FieldMayBeFinal")
    private String userId;
    private final String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}