package com.fast_campus_12.not_found.shop.auth.repository;

import com.fast_campus_12.not_found.shop.auth.dto.Auth;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthRepository {

    // 예시: 가짜 DB
    private static final Map<String, Auth> fakeDB = new HashMap<>();

    static {
        fakeDB.put("Choi3495", new Auth("Choi3495", "choi3495!"));
        fakeDB.put("admin000", new Auth("admin000", "adminpass000!"));
    }

    public Auth findById(String id) {
        return fakeDB.get(id); // 실제로는 DB 조회
    }

    public void save(Auth auth) {
        fakeDB.put(auth.getId(), auth);
    }

}