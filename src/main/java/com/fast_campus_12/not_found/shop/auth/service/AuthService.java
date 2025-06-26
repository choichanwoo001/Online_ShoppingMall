package com.fast_campus_12.not_found.shop.auth.service;

import com.fast_campus_12.not_found.shop.auth.dto.Auth;
import com.fast_campus_12.not_found.shop.auth.dto.LoginHistory;
import com.fast_campus_12.not_found.shop.mapper.AuthMapper;
import com.fast_campus_12.not_found.shop.mapper.LoginHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.fast_campus_12.not_found.shop.service.UserService.PASSWORD_PATTERN;
import static com.fast_campus_12.not_found.shop.service.UserService.USER_ID_PATTERN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthMapper authMapper;
    private final LoginHistoryMapper loginHistoryMapper;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(1);

    /** 로그인 처리 */
    @Transactional
    public Auth login(String loginId, String password) {
        // ── 1) 입력값 유효성 검사 ─────────────────────────────────────────
        if (Objects.isNull(loginId) || loginId.isBlank()
                || !USER_ID_PATTERN.matcher(loginId).matches()) {
            // 유효성 실패 시 null 반환
            return null;
        }
        if (Objects.isNull(password) || password.isBlank()
                || !PASSWORD_PATTERN.matcher(password).matches()) {
            // 유효성 실패 시 null 반환
            return null;
        }

        // ── 2) DB에서 사용자 조회 (실패 횟수 포함) ─────────────────────────
        Optional<Auth> userOpt = authMapper.findUserWithFailCount(loginId);
        if (userOpt.isEmpty()) {
            return null;
        }
        Auth user = userOpt.get();

        // ── 3) 활성화·삭제 여부 확인 ────────────────────────────────────
        if (!user.getIsActivate() || user.getIsDeleted()) {
            recordLoginAttempt(user.getUserId(), false);
            return null;
        }

        // ── 4) 잠김 상태 확인 (마지막 실패 시점으로부터 30분 이내) ───────
        if (isAccountLocked(user.getUserId())) {
            user.setLocked(true);
            recordLoginAttempt(user.getUserId(), false);
            return null;
        }

        // ── 5) 비밀번호 검증 ───────────────────────────────────────────
        if (verifyPassword(password, user.getPassword())) {
            recordLoginAttempt(user.getUserId(), true);
            user.setFailCount(0);
            return user;
        } else {
            recordLoginAttempt(user.getUserId(), false);
            int failCount = getFailedAttemptCount(user.getUserId());
            user.setFailCount(failCount);
            if (failCount >= MAX_FAILED_ATTEMPTS) {
                user.setLocked(true);
            }
            return null;
        }
    }

    /** 비밀번호 검증 (로그인 시 사용) */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (Objects.isNull(plainPassword) || Objects.isNull(hashedPassword)) {
            return false;
        }
        if (!(hashedPassword.startsWith("$2a$") ||
                hashedPassword.startsWith("$2b$") ||
                hashedPassword.startsWith("$2y$"))) {
            throw new IllegalStateException("유효하지 않은 BCrypt 해시 형식입니다.");
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호 검증 중 오류가 발생했습니다.", e);
        }
    }

    /** 로그인 시도 기록 저장 */
    private void recordLoginAttempt(Long userId, boolean success) {
        int prevFailures = getFailedAttemptCount(userId);
        int consecutiveFailures = success ? 0 : prevFailures + 1;
        boolean locked = consecutiveFailures >= MAX_FAILED_ATTEMPTS;

        LoginHistory lh = LoginHistory.builder()
                .userId(userId)
                .attemptResult(success)
                .consecutiveFailedLoginAttempt(consecutiveFailures)
                .isLocked(locked)
                .createdAt(LocalDateTime.now())
                .build();
        loginHistoryMapper.insertLoginHistory(lh);
    }

    /** 계정 잠김 여부 확인 (마지막 실패 시점 기준 30분 이내면 잠김) */
    public boolean isAccountLocked(Long userId) {
        Optional<LoginHistory> last = loginHistoryMapper.findLatestByUserId(userId);
        if (last.isEmpty() || !last.get().getIsLocked()) {
            return false;
        }
        LocalDateTime lockedAt = last.get().getCreatedAt();
        return Duration.between(lockedAt, LocalDateTime.now())
                .compareTo(LOCK_DURATION) < 0;
    }

    /** 연속 실패 횟수 조회 */
    public int getFailedAttemptCount(Long userId) {
        return loginHistoryMapper.getConsecutiveFailedAttempts(userId);
    }

    /** 추가 로그인 히스토리(성공) 처리 */
    public void uploadLoginHistory(String loginId) {
        // 기본 로그인 기록은 recordLoginAttempt에서 이미 저장됨.
        log.info("사용자 [{}] 로그인 성공 처리 완료", loginId);
    }

    /** 사용자 조회 (컨트롤러에서 failCount, locked 조회용) */
    public Auth findById(String loginId) {
        return authMapper.findUserWithFailCount(loginId)
                .orElse(null);
    }
}
