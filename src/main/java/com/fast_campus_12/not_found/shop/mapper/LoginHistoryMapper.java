package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.auth.dto.LoginHistory;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface LoginHistoryMapper {

    /**
     * 로그인 기록 추가
     */
    @Insert("""
        INSERT INTO LOGIN_HISTORY (USER_ID, IP, BROWSER, NATION, REGION, 
                                  ATTEMPT_RESULT, CONSECUTIVE_FAILED_LOGIN_ATTEMPT, 
                                  IS_LOCKED, CREATED_AT)
        VALUES (#{userId}, #{ip}, #{browser}, #{nation}, #{region}, 
                #{attemptResult}, #{consecutiveFailedLoginAttempt}, 
                #{isLocked}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginHistory(LoginHistory loginHistory);

    /**
     * 특정 사용자의 최근 로그인 실패 횟수 조회 (24시간 내)
     */
    @Select("""
        SELECT COUNT(*) 
        FROM LOGIN_HISTORY 
        WHERE USER_ID = #{userId} 
          AND ATTEMPT_RESULT = 0 
          AND CREATED_AT >= #{since}
    """)
    int countRecentFailedAttempts(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    /**
     * 특정 사용자의 마지막 로그인 기록 조회
     */
    @Select("""
        SELECT ID as id, USER_ID as userId, IP as ip, BROWSER as browser, 
               NATION as nation, REGION as region, ATTEMPT_RESULT as attemptResult,
               CONSECUTIVE_FAILED_LOGIN_ATTEMPT as consecutiveFailedLoginAttempt,
               IS_LOCKED as isLocked, CREATED_AT as createdAt
        FROM LOGIN_HISTORY 
        WHERE USER_ID = #{userId} 
        ORDER BY CREATED_AT DESC 
        LIMIT 1
    """)
    Optional<LoginHistory> findLatestByUserId(@Param("userId") Long userId);

    /**
     * 특정 사용자의 연속 실패한 로그인 기록들 조회
     */
    @Select("""
        SELECT ID as id, USER_ID as userId, IP as ip, BROWSER as browser, 
               NATION as nation, REGION as region, ATTEMPT_RESULT as attemptResult,
               CONSECUTIVE_FAILED_LOGIN_ATTEMPT as consecutiveFailedLoginAttempt,
               IS_LOCKED as isLocked, CREATED_AT as createdAt
        FROM LOGIN_HISTORY 
        WHERE USER_ID = #{userId} AND ATTEMPT_RESULT = 0
        ORDER BY CREATED_AT DESC
    """)
    List<LoginHistory> findRecentFailedAttempts(@Param("userId") Long userId);

    /**
     * 특정 사용자의 최근 로그인 기록 조회 (지정된 기간)
     */
    @Select("""
        SELECT ID as id, USER_ID as userId, IP as ip, BROWSER as browser, 
               NATION as nation, REGION as region, ATTEMPT_RESULT as attemptResult,
               CONSECUTIVE_FAILED_LOGIN_ATTEMPT as consecutiveFailedLoginAttempt,
               IS_LOCKED as isLocked, CREATED_AT as createdAt
        FROM LOGIN_HISTORY 
        WHERE USER_ID = #{userId} AND CREATED_AT >= #{since}
        ORDER BY CREATED_AT DESC
    """)
    List<LoginHistory> findRecentLoginHistory(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    /**
     * 계정 잠김 상태 확인 (24시간 내 마지막 기록이 잠김 상태인지)
     */
    @Select("""
        SELECT CASE 
                   WHEN lh.IS_LOCKED = 1 AND lh.CREATED_AT >= DATE_SUB(NOW(), INTERVAL 24 HOUR) 
                   THEN 1 
                   ELSE 0 
               END as locked
        FROM LOGIN_HISTORY lh
        WHERE lh.USER_ID = #{userId}
        ORDER BY lh.CREATED_AT DESC
        LIMIT 1
    """)
    @Results({
            @Result(property = "locked", column = "locked")
    })
    Optional<Boolean> isAccountLocked(@Param("userId") Long userId);

    /**
     * 성공한 로그인 이후 실패 횟수 계산
     */
    @Select("""
        <script>
        SELECT COUNT(*) 
        FROM LOGIN_HISTORY lh1
        WHERE lh1.USER_ID = #{userId} 
          AND lh1.ATTEMPT_RESULT = 0
          AND lh1.CREATED_AT > COALESCE(
              (SELECT MAX(lh2.CREATED_AT) 
               FROM LOGIN_HISTORY lh2 
               WHERE lh2.USER_ID = #{userId} AND lh2.ATTEMPT_RESULT = 1), 
              '1970-01-01'
          )
        </script>
    """)
    int getConsecutiveFailedAttempts(@Param("userId") Long userId);
}
