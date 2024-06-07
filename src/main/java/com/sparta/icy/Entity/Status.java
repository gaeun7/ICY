package com.sparta.icy.Entity;

public enum Status {
    ENROLLED(Authority.ENROLLED),  // 사용자 권한
    DELETED(Authority.DELETED);  // 관리자 권한

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Authority {
        public static final String ENROLLED = "ENROLLED";
        public static final String DELETED = "DELETED";
    }
}



