package com.example.arcadefinder.Models;

import com.parse.ParseFile;

public class RequestDetailsModel {
    private boolean deleted;
    private boolean verified;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
