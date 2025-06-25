package com.fast_campus_12.not_found.shop.auth.service;

import com.fast_campus_12.not_found.shop.auth.dto.Auth;
import com.fast_campus_12.not_found.shop.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Auth login(String id, String pw) {
        Auth auth = authRepository.findById(id);

        // 1. ID에 해당하는 row가 없으면 false
        if (Objects.isNull(auth) || auth.isLocked()) {
            return null;    //사용자가 없거나 이미 잠긴 계정
        }

        // 2. 비밀번호 일치 확인
        if (Objects.equals(pw, auth.getPw())) {
            // 로그인 성공 → 실패 횟수 초기화
            auth.setFailCount(0);
            authRepository.save(auth);  // 변경된 정보 저장
            return auth;
        }

        // 3. 로그인 실패 -> 실패 횟수 증가
        int failCount = auth.getFailCount() + 1;
        auth.setFailCount(failCount);

        // 3회 이상 실패 시 계정 잠금
        if (failCount >= 3) {
            auth.setLocked(true);
        }

        authRepository.save(auth);  // 실패 횟수 및 잠금 상태 저장
        return null;

    }

    public void uploadLoginHistory(String id) {
        // @TODO: 진짜 DB 기록 저장 구현 예정
        System.out.println("로그인 히스토리 저장: " + id);
    }
}

// 1. auth table에 주어진 id의 row(데이터)가 있는가
// 2. 있다면 해당 데이터의 pw가 주어진 pw와 일치하는가
// 1, 2를 모두 만족하면 return true
// 1, 2 중 하나라도 아니라면 return false
// 근데 db 조회 결과 null이 return되면 어떻게 하지?
// null.getPw()하면 nullPointException이 발생할텐데...