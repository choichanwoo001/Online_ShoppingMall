package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.dto.SignupRequest;
import com.fast_campus_12.not_found.shop.entity.User;
import com.fast_campus_12.not_found.shop.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDAO userDAO;
    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    private static final int BCRYPT_ROUNDS = 12;

    // 정규식 패턴
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{4,16}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{4,16}$");
    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z]{2,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    /**
     * 아이디 중복 확인
     */
    public boolean isUserIdAvailable(String userId) {
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
            log.debug("1");
            return false;
        }

        return userDAO.countByUserId(userId) == 0;
    }

    /**
     * 회원가입 요청 유효성 검사
     */
    public Map<String, String> validateSignupRequest(SignupRequest request) {
        Map<String, String> errors = new HashMap<String, String>();

        // 아이디 검증
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            errors.put("userId", "아이디를 입력해주세요.");
        } else if (!USER_ID_PATTERN.matcher(request.getUserId()).matches()) {
            errors.put("userId", "영문+숫자 혼용 4~16자로 입력해주세요.");
        } else if (userDAO.countByUserId(request.getUserId()) > 0) {
            errors.put("userId", "이미 사용중인 아이디입니다.");
        }

        // 비밀번호 검증
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            errors.put("password", "비밀번호를 입력해주세요.");
        } else if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            errors.put("password", "영문 대소문자/숫자 조합 4~16자로 입력해주세요.");
        }

        // 이름 검증
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            errors.put("userName", "이름을 입력해주세요.");
        } else if (!USER_NAME_PATTERN.matcher(request.getUserName()).matches()) {
            errors.put("userName", "한글 또는 영문 2~20자로 입력해주세요.");
        }

        // 이메일 검증
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.put("email", "이메일을 입력해주세요.");
        } else if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            errors.put("email", "올바른 이메일 형식이 아닙니다.");
        } else if (userDAO.countByEmail(request.getEmail()) > 0) {
            errors.put("email", "이미 사용중인 이메일입니다.");
        }

        return errors;
    }

    /**
     * 사용자 생성
     */
    public Long createUser(SignupRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        String hashedPassword = hashPassword(request.getPassword());
        user.setPassword(hashedPassword);

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setDetailAddress(request.getDetailAddress());
        user.setMobilePhone(request.getMobilePhone());
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setRole("USER");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        return userDAO.insertUser(user);
    }

    // ============================================================================
    // 🔐 jBCrypt 관련 메서드들 (추가)
    // ============================================================================

    /**
     * 🔐 비밀번호 해시화
     */
    private String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * 🔐 비밀번호 검증
     */
//    public boolean verifyPassword(String plainPassword, String hashedPassword) {
//        if (plainPassword == null || hashedPassword == null) {
//            return false;
//        }
//
//        try {
//            return BCrypt.checkpw(plainPassword, hashedPassword);
//        } catch (Exception e) {
//            return false;
//        }
//    }

    /**
     * 🔐 로그인 인증
     */
//    public boolean authenticateUser(String userId, String plainPassword) {
//        try {
//            User user = userDAO.findByUserId(userId);
//
//            if (user == null || !user.getIsActive() || user.getIsDeleted()) {
//                return false;
//            }
//
//            return verifyPassword(plainPassword, user.getPassword());
//
//        } catch (Exception e) {
//            throw new RuntimeException("로그인 처리 중 오류 발생", e);
//        }
//    }

    /**
     * 🔐 비밀번호 변경
     */
//    public boolean changePassword(String userId, String currentPassword, String newPassword) {
//        try {
//            // 현재 비밀번호 확인
//            if (!authenticateUser(userId, currentPassword)) {
//                return false;
//            }
//
//            // 새 비밀번호 유효성 검사
//            if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
//                throw new IllegalArgumentException("새 비밀번호 형식이 올바르지 않습니다.");
//            }
//
//            // 새 비밀번호 암호화
//            String hashedNewPassword = hashPassword(newPassword);
//
//            // DB 업데이트
//            int result = userDAO.updatePassword(userId, hashedNewPassword);
//            return result > 0;
//
//        } catch (Exception e) {
//            throw new RuntimeException("비밀번호 변경 중 오류 발생", e);
//        }
//    }
}