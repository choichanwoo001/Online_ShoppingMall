package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.dto.SignupRequest;
import com.fast_campus_12.not_found.shop.entity.User;
import com.fast_campus_12.not_found.shop.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 정규식 패턴
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{4,16}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{4,16}$");
    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z]{2,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    /**
     * 아이디 중복 확인
     */
    public boolean isUserIdAvailable(String userId) {
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
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
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
}