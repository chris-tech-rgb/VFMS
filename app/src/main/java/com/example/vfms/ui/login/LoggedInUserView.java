package com.example.vfms.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    @SuppressWarnings("FieldMayBeFinal")
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}