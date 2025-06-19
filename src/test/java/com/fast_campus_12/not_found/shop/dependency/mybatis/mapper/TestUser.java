package com.fast_campus_12.not_found.shop.dependency.mybatis.mapper;


public class TestUser {
    private String id;
    private String pw;

    // 기본 생성자, getter/setter 필요
    public TestUser() {}

    public TestUser(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getIdAndPw() {
        return "id =" + id + ", pw = "+ pw;
    }
}
