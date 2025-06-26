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
     * LOGIN_ID 중복 확인 (사용자가 입력하는 아이디)
     */
    public boolean isUserIdAvailable(String loginId) {
        if (!USER_ID_PATTERN.matcher(loginId).matches()) {
            log.debug("LOGIN_ID 형식이 올바르지 않습니다: {}", loginId);
            return false;
        }
        boolean exists = userDAO.existsByLoginId(loginId);
        log.debug("LOGIN_ID 중복 확인: {} -> 사용가능: {}", loginId, !exists);
        return !exists; // 존재하지 않으면 사용 가능
    }

    public Map<String, String> validateUserRequest(Object request, boolean isSignup) {
        Map<String, String> errors = new HashMap<>();

        String loginId = null;
        String name = null;
        String email = null;
        String password = null;
        Long userId = null;

        if (isSignup && request instanceof SignupRequest signupReq) {
            loginId = signupReq.getUserId();
            name = signupReq.getUserName();
            email = signupReq.getEmail();
            password = signupReq.getPassword();

            // 아이디 검증
            if (loginId == null || loginId.trim().isEmpty()) {
                errors.put("userId", "아이디를 입력해주세요.");
            } else if (!USER_ID_PATTERN.matcher(loginId).matches()) {
                errors.put("userId", "영문+숫자 혼용 4~16자로 입력해주세요.");
            } else if (userDAO.existsByLoginId(loginId)) {
                errors.put("userId", "이미 사용중인 아이디입니다.");
            }

        } else if (!isSignup && request instanceof UserUpdateRequest updateReq) {
            loginId = updateReq.getUserId();
            name = updateReq.getUserName();
            email = updateReq.getEmail();
            password = updateReq.getPassword();

            User user = userDAO.findByLoginId(loginId);
            if (user == null) {
                errors.put("userId", "존재하지 않는 사용자입니다.");
                return errors;
            }
            userId = user.getUserId();
        }

        // 공통 검증
        validateName(name, errors);
        validateEmail(email, userId, errors);
        validatePassword(password, isSignup, errors);

        return errors;
    }
    private void validateName(String name, Map<String, String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.put("userName", "이름을 입력해주세요.");
        } else if (!USER_NAME_PATTERN.matcher(name).matches()) {
            errors.put("userName", "한글 또는 영문 2~20자로 입력해주세요.");
        }
    }

    private void validateEmail(String email, Long currentUserId, Map<String, String> errors) {
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "이메일을 입력해주세요.");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.put("email", "올바른 이메일 형식이 아닙니다.");
        } else {
            boolean exists = (currentUserId == null)
                    ? userDetailMapper.existsByEmail(email)  // 회원가입
                    : userDetailMapper.existsByEmailExcludingUserId(email, currentUserId);  // 수정
            if (exists) {
                errors.put("email", "이미 사용중인 이메일입니다.");
            }
        }
    }

    private void validatePassword(String password, boolean isRequired, Map<String, String> errors) {
        if (password == null || password.trim().isEmpty()) {
            if (isRequired) errors.put("password", "비밀번호를 입력해주세요.");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.put("password", "영문 대소문자/숫자 조합 4~16자로 입력해주세요.");
        }
    }

    /**
     * 사용자 생성
     */
    @Transactional
    public Long signup(SignupRequest request) {
        try {
            // 1. USERS 테이블 저장
            User user = User.builder()
                    .loginId(request.getUserId())  // LOGIN_ID 필드에 저장
                    .password(hashPassword(request.getPassword()))
                    .isActivate(true)
                    .isDeleted(false)
                    .role("USER")
                    .build();

            Long userId = userDAO.insertUser(user);  // AUTO_INCREMENT로 생성된 PK 반환
            log.info("USERS 테이블 저장 완료. 사용자ID: {}, LOGIN_ID: {}", userId, user.getLoginId());

            // 2. USER_DETAIL 테이블 저장
            UserDetail userDetail = UserDetail.builder()
                    .userId(userId)  // FK로 설정
                    .email(request.getEmail())
                    .name(request.getUserName())
                    .phoneNumber(request.getMobilePhone())
                    .jobCode(request.getJobCode())  // 기본값 1 적용됨
                    .build();

            userDetailMapper.insertUserDetail(userDetail);
            log.info("USER_DETAIL 테이블 저장 완료");

            // 3. DEFAULT_USER_ADDRESS 테이블 저장
            if (request.getRoadAddress1() != null || request.getAddress() != null) {
                UserAddress userAddress = UserAddress.builder()
                        .userId(userId)  // FK로 설정
                        .roadAddress1(request.getRoadAddress1() != null ? request.getRoadAddress1() : request.getAddress())
                        .roadAddress2(request.getRoadAddress2())
                        .jibunAddress(request.getJibunAddress())
                        .detailAddress(request.getDetailAddress())
                        .englishAddress(request.getEnglishAddress())
                        .zipCode(parseZipCode(request.getZipCode()))
                        .addressName(request.getAddressName())
                        .build();

                userAddressMapper.insertUserAddress(userAddress);
                log.info("DEFAULT_USER_ADDRESS 테이블 저장 완료");
            }

            log.info("회원가입 완료: 사용자ID={}, LOGIN_ID={}", userId, request.getUserId());
            return userId;

        } catch (Exception e) {
            log.error("회원가입 실패: LOGIN_ID={}, 에러={}", request.getUserId(), e.getMessage(), e);
            throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자 정보 조회 (LOGIN_ID 기반)
     */
    public UserInfoResponse getUserInfo(String loginId) {
        try {
            User user = userDAO.findByLoginId(loginId);
            log.debug("DB에서 조회된 유저: {}", user);

            if (user == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            // 상세 정보 조회
            UserDetail userDetail = userDetailMapper.findByUserId(user.getUserId());
            UserAddress userAddress = userAddressMapper.findByUserId(user.getUserId());

            // 응답 객체 생성
            UserInfoResponse.UserInfoResponseBuilder builder = UserInfoResponse.builder()
                    .userId(user.getLoginId())  // 화면에서는 LOGIN_ID를 userId로 표시
                    .isActivate(user.getIsActivate())
                    .isDeleted(user.getIsDeleted())
                    .role(user.getRole())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt());

            // 상세 정보 추가
            if (userDetail != null) {
                builder.userName(userDetail.getName())
                        .email(userDetail.getEmail())
                        .mobilePhone(userDetail.getPhoneNumber())
                        .gender(userDetail.getGender())
                        .birthDate(userDetail.getBirthDate())
                        .jobCode(userDetail.getJobCode());
            }

            // 주소 정보 추가
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
                    displayAddress += " " + userAddress.getRoadAddress2();
                }
                builder.address(displayAddress);
            }

            return builder.build();

        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: loginId={}, error={}", loginId, e.getMessage(), e);
            throw new RuntimeException("사용자 정보 조회에 실패했습니다.", e);
        }
    }

    /**
     * 회원정보 수정
     */
    @Transactional
    public boolean updateUserInfo(UserUpdateRequest request) {
        try {
            log.info("회원정보 수정 시작: LOGIN_ID={}", request.getUserId());

            // LOGIN_ID로 PK 조회
            User existingUser = userDAO.findByLoginId(request.getUserId());
            if (existingUser == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }
            Long userPkId = existingUser.getUserId();

            // 1. USERS 테이블 업데이트 (비밀번호가 제공된 경우만)
            if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
                User user = new User();
                user.setUserId(userPkId);
                user.setPassword(hashPassword(request.getPassword()));
                user.setUpdatedAt(LocalDateTime.now());

                userMapper.updateUserPassword(user);
                log.info("USERS 테이블 비밀번호 업데이트 완료");
            }

            // 2. USER_DETAIL 테이블 업데이트
            UserDetail userDetail = new UserDetail();
            userDetail.setUserId(userPkId);
            userDetail.setEmail(request.getEmail());
            userDetail.setName(request.getUserName());
            userDetail.setPhoneNumber(request.getMobilePhone());
            userDetail.setGender(request.getGender());
            userDetail.setJobCode(request.getJobCode());

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

            // 3. DEFAULT_USER_ADDRESS 테이블 업데이트
            if (request.getRoadAddress1() != null || request.getDetailAddress() != null) {
                UserAddress userAddress = new UserAddress();
                userAddress.setUserId(userPkId);
                userAddress.setRoadAddress1(request.getRoadAddress1());
                userAddress.setRoadAddress2(request.getRoadAddress2());
                userAddress.setJibunAddress(request.getJibunAddress());
                userAddress.setDetailAddress(request.getDetailAddress());
                userAddress.setEnglishAddress(request.getEnglishAddress());
                userAddress.setAddressName(request.getAddressName());
                userAddress.setZipCode(parseZipCode(request.getZipCode()));

                userAddressMapper.updateUserAddress(userAddress);
                log.info("DEFAULT_USER_ADDRESS 테이블 업데이트 완료");
            }

            log.info("회원정보 수정 완료: LOGIN_ID={}", request.getUserId());
            return true;

        } catch (Exception e) {
            log.error("회원정보 수정 실패: LOGIN_ID={}, 에러={}", request.getUserId(), e.getMessage(), e);
            throw new RuntimeException("회원정보 수정 처리 중 오류가 발생했습니다.", e);
        }
    }

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
     * 우편번호 파싱 유틸리티
     */
    private Integer parseZipCode(String zipCode) {
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            try {
                return Integer.parseInt(zipCode);
            } catch (NumberFormatException e) {
                log.warn("우편번호 파싱 실패: {}", zipCode);
            }
        }
        return null;
    }

    /*
    * 회원정보 수정 관련
    */

    //사용자 삭제 (소프트 삭제)
    @Transactional
    public boolean deleteUser(String loginId) {
        try {
            User user = userDAO.findByLoginId(loginId);
            if (user == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            User updateUser = User.builder()
                    .userId(user.getUserId())
                    .isDeleted(true)
                    .deletedAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            int result = userMapper.updateUser(updateUser);
            log.info("사용자 삭제 완료: LOGIN_ID={}", loginId);
            return result > 0;

        } catch (Exception e) {
            log.error("사용자 삭제 실패: LOGIN_ID={}, 에러={}", loginId, e.getMessage(), e);
            throw new RuntimeException("사용자 삭제에 실패했습니다.", e);
        }
    }

    /**
     * 비밀번호 재설정 (임시 비밀번호로)
     */
    @Transactional
    public boolean resetPassword(String loginId, String newPassword) {
        try {
            // 사용자 존재 여부 확인
            User user = userDAO.findByLoginId(loginId);
            if (user == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            // 새 비밀번호 암호화 및 업데이트
            String hashedNewPassword = hashPassword(newPassword);
            int result = userDAO.updatePassword(loginId, hashedNewPassword);

            log.info("비밀번호 재설정 완료: LOGIN_ID={}", loginId);
            return result > 0;

        } catch (Exception e) {
            log.error("비밀번호 재설정 실패: LOGIN_ID={}, 에러={}", loginId, e.getMessage(), e);
            throw new RuntimeException("비밀번호 재설정 중 오류가 발생했습니다.", e);
        }
    }

    // 이메일로 로그인 아이디 찾기
    public String findLoginIdByEmail(String email) {
        try {

            // 이메일로 사용자 조회 (USER_DETAIL 테이블 기준)
            User user = userDAO.selectByEmail(email);

            if (user == null) {
                log.debug("해당 이메일로 가입된 사용자가 없습니다: {}", email);
                return null;
            }
            // 탈퇴하거나 비활성화된 사용자 체크
            if (user.getIsDeleted() || !user.getIsActivate()) {
                log.debug("탈퇴하거나 비활성화된 사용자입니다: email={}, isDeleted={}, isActivate={}",
                        email, user.getIsDeleted(), user.getIsActivate());
                return null;
           }
            log.debug("이메일로 LOGIN_ID 찾기 성공: email={}, loginId={}", email, user.getLoginId());
            return user.getLoginId();

        } catch (Exception e) {
            log.error("이메일로 LOGIN_ID 찾기 실패: email={}, 에러={}", email, e.getMessage(), e);
            return null;
        }
    }
}