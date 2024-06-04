package com.sparta.icy.Entity;

public enum StatusEnum {

    Active(Status.Active),
    Inactive(Status.Inactive);

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String Active = "Active";
        public static final String Inactive = "Inactive";
    }

}
