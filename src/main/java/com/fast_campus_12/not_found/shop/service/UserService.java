package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.dto.SignupRequest;
import com.fast_campus_12.not_found.shop.dto.UserInfoResponse;
import com.fast_campus_12.not_found.shop.dto.UserUpdateRequest;
import com.fast_campus_12.not_found.shop.entity.User;
import com.fast_campus_12.not_found.shop.dao.UserDAO;
import com.fast_campus_12.not_found.shop.entity.UserAddress;
import com.fast_campus_12.not_found.shop.entity.UserDetail;
import com.fast_campus_12.not_found.shop.mapper.UserAddressMapper;
import com.fast_campus_12.not_found.shop.mapper.UserDetailMapper;
import com.fast_campus_12.not_found.shop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final UserAddressMapper userAddressMapper;
    private final UserDAO userDAO;

    private static final int BCRYPT_ROUNDS = 12;

    // 정규식 패턴
    public static final Pattern USER_ID_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{4,16}$");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{4,16}$");
    public static final Pattern USER_NAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z]{2,20}$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    /**
     * 아이디 중복 확인
     */
    public boolean isUserIdAvailable(String userId) {
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
            log.debug("아이디 형식이 올바르지 않습니다: {}", userId);
            return false;
        }
        boolean exists = userDAO.existsByUserId(userId);
        log.debug("아이디 중복 확인: {} -> 사용가능: {}", userId, !exists);
        return !exists; // 존재하지 않으면 사용 가능
    }

    /**
     * 회원가입 요청 유효성 검사
     */
    public Map<String, String> validateSignupRequest(SignupRequest request) {
        log.debug("회원가입 요청 유효성 검사 시작: {}", request.getUserId());
        Map<String, String> errors = new HashMap<String, String>();

        // 아이디 검증
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            errors.put("userId", "아이디를 입력해주세요.");
        } else if (!USER_ID_PATTERN.matcher(request.getUserId()).matches()) {
            errors.put("userId", "영문+숫자 혼용 4~16자로 입력해주세요.");
        } else if (userDAO.existsByUserId(request.getUserId())) {
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
        } else if (userDetailMapper.existsByEmail(request.getEmail())) {
            errors.put("email", "이미 사용중인 이메일입니다.");
        }

        log.debug("유효성 검사 완료. 오류 개수: {}", errors.size());
        return errors;
    }

    /**
     * 사용자 생성
     */
    @Transactional
    public String signup(SignupRequest request) {
        try {
            // 1. USERS 테이블 저장
            User user = User.builder()
                    .userId(request.getUserId())
                    .password(hashPassword(request.getPassword()))
                    .isActivate(true)
                    .isDeleted(false)
                    .role("USER")
                    .build();

            userMapper.insertUser(user);
            String userId = user.getUserId();
            log.info("USERS 테이블 저장 완료. 사용자ID: {}", userId);

            // 2. USER_DETAIL 테이블 저장
            UserDetail userDetail = new UserDetail();
            userDetail.setUserId(userId);
            userDetail.setEmail(request.getEmail());
            userDetail.setName(request.getUserName());
            userDetail.setPhoneNumber(request.getMobilePhone());

            userDetailMapper.insertUserDetail(userDetail);
            log.info("USER_DETAIL 테이블 저장 완료");

            // 3. default_user_address 테이블 저장 (ERD에 맞춘 모든 필드)
            UserAddress userAddress = UserAddress.builder()
                    .userId(userId)
                    .roadAddress1(request.getRoadAddress1() != null ? request.getRoadAddress1() : request.getAddress()) // 하위 호환성
                    .roadAddress2(request.getRoadAddress2())
                    .jibunAddress(request.getJibunAddress())
                    .detailAddress(request.getDetailAddress())
                    .englishAddress(request.getEnglishAddress())
                    .zipCode(request.getZipCode())
                    .addressName(request.getAddressName())
                    .build();

            userAddressMapper.insertUserAddress(userAddress);
            log.info("default_user_address 테이블 저장 완료");

            log.info("회원가입 완료: 사용자ID={}", userId);
            return userId;

        } catch (Exception e) {
            log.error("회원가입 실패: 사용자ID={}, 에러={}", request.getUserId(), e.getMessage(), e);
            throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다.", e);
        }
    }

    // ============================================================================
    // 🔐 jBCrypt 관련 메서드들 (추가)
    // ============================================================================

    /**
     * 🔐 비밀번호 해시화
     */
    private String hashPassword(String plainPassword) {
        log.debug("비밀번호 해시화 시작");

        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
        String hashedPassword = BCrypt.hashpw(plainPassword, salt);

        log.debug("비밀번호 해시화 완료");
        return hashedPassword;
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
//    @Transactional
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
    /**
     * 사용자 정보 조회
     */
    public UserInfoResponse getUserInfo(String userId) {
        try {
            // USERS 테이블에서 기본 정보 조회
            User user = userMapper.findByUserId(userId);
            log.debug("DB에서 조회된 유저: {}", user); // ★ 로그 추가

            if (user == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            // USER_DETAIL 테이블에서 상세 정보 조회
            UserDetail userDetail = userDetailMapper.findByUserId(userId);

            // default_user_address 테이블에서 주소 정보 조회
            UserAddress userAddress = userAddressMapper.findByUserId(userId);

            // 응답 객체 생성
            UserInfoResponse.UserInfoResponseBuilder builder = UserInfoResponse.builder()
                    .userId(user.getUserId())
                    .isActivate(user.getIsActivate())
                    .isDeleted(user.getIsDeleted())
                    .role(user.getRole())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt());

            // 상세 정보가 있으면 추가
            if (userDetail != null) {
                builder.userName(userDetail.getName())
                        .email(userDetail.getEmail())
                        .mobilePhone(userDetail.getPhoneNumber())
                        .gender(userDetail.getGender())
                        .birthDate(userDetail.getBirthDate());
            }

            // 주소 정보가 있으면 추가
            if (userAddress != null) {
                builder.roadAddress1(userAddress.getRoadAddress1())
                        .roadAddress2(userAddress.getRoadAddress2())
                        .jibunAddress(userAddress.getJibunAddress())
                        .detailAddress(userAddress.getDetailAddress())
                        .englishAddress(userAddress.getEnglishAddress())
                        .zipCode(userAddress.getZipCode())
                        .addressName(userAddress.getAddressName());

                // 화면 표시용 주소 조합
                if (userAddress.getZipCode() != null) {
                    builder.postcode(String.valueOf(userAddress.getZipCode()));
                }

                String displayAddress = userAddress.getRoadAddress1();
                if (userAddress.getRoadAddress2() != null && !userAddress.getRoadAddress2().isEmpty()) {
                    displayAddress += userAddress.getRoadAddress2();
                }
                builder.address(displayAddress);
            }

            return builder.build();

        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException("사용자 정보 조회에 실패했습니다.", e);
        }
    }

    /**
     * 회원정보 수정 요청 유효성 검사
     */
    public Map<String, String> validateUserUpdateRequest(UserUpdateRequest request) {
        Map<String, String> errors = new HashMap<>();

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
        } else {
            // 다른 사용자가 사용 중인 이메일인지 확인
            if (userDetailMapper.existsByEmailExcludingUserId(request.getEmail(), request.getUserId())) {
                errors.put("email", "이미 사용중인 이메일입니다.");
            }
        }

        // 비밀번호 검증 (입력된 경우만)
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
                errors.put("password", "영문 대소문자/숫자 조합 4~16자로 입력해주세요.");
            }
        }

        return errors;
    }

    /**
     * 회원정보 수정
     */
    @Transactional
    public boolean updateUserInfo(UserUpdateRequest request) {
        try {
            log.info("회원정보 수정 시작: 사용자ID={}", request.getUserId());

            // 1. USERS 테이블 업데이트 (비밀번호가 제공된 경우만)
            if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
                User user = new User();
                user.setUserId(request.getUserId());
                user.setPassword(hashPassword(request.getPassword()));
                user.setUpdatedAt(LocalDateTime.now());

                userMapper.updateUserPassword(user);
                log.info("USERS 테이블 비밀번호 업데이트 완료");
            }

            // 2. USER_DETAIL 테이블 업데이트
            UserDetail userDetail = new UserDetail();
            userDetail.setUserId(request.getUserId());
            userDetail.setEmail(request.getEmail());
            userDetail.setName(request.getUserName());
            userDetail.setPhoneNumber(request.getMobilePhone());
            userDetail.setGender(request.getGender());

            // 생년월일 파싱
            if (request.getBirthDate() != null && !request.getBirthDate().trim().isEmpty()) {
                try {
                    userDetail.setBirthDate(LocalDate.parse(request.getBirthDate()));
                } catch (Exception e) {
                    log.warn("생년월일 파싱 실패: {}", request.getBirthDate());
                }
            }

            userDetailMapper.updateUserDetail(userDetail);
            log.info("USER_DETAIL 테이블 업데이트 완료");

            // 3. default_user_address 테이블 업데이트
            if (request.getRoadAddress1() != null || request.getDetailAddress() != null) {
                UserAddress userAddress = new UserAddress();
                userAddress.setUserId(request.getUserId());
                userAddress.setRoadAddress1(request.getRoadAddress1());
                userAddress.setRoadAddress2(request.getRoadAddress2());
                userAddress.setJibunAddress(request.getJibunAddress());
                userAddress.setDetailAddress(request.getDetailAddress());
                userAddress.setEnglishAddress(request.getEnglishAddress());
                userAddress.setAddressName(request.getAddressName());

                // zipCode 파싱
                if (request.getZipCode() != null && !request.getZipCode().trim().isEmpty()) {
                    try {
                        userAddress.setZipCode(Integer.parseInt(request.getZipCode()));
                    } catch (NumberFormatException e) {
                        log.warn("우편번호 파싱 실패: {}", request.getZipCode());
                    }
                }

                userAddressMapper.updateUserAddress(userAddress);
                log.info("default_user_address 테이블 업데이트 완료");
            }

            log.info("회원정보 수정 완료: 사용자ID={}", request.getUserId());
            return true;

        } catch (Exception e) {
            log.error("회원정보 수정 실패: 사용자ID={}, 에러={}", request.getUserId(), e.getMessage(), e);
            throw new RuntimeException("회원정보 수정 처리 중 오류가 발생했습니다.", e);
        }
    }
}