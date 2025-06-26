package com.fast_campus_12.not_found.shop.auth.dto;

public class Auth {
    private String id;
    private String pw;
    private int failCount;   // 로그인 실패 횟수
    private boolean locked;  // 계정 잠김 여부

    public Auth(String id, String pw) {
        this.id = id;
        this.pw = pw;
        this.failCount = 0;
        this.locked = false;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}