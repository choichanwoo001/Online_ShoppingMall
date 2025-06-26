package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.auth.dto.Auth;
import org.apache.ibatis.annotations.*;
import java.util.Optional;

@Mapper
public interface AuthMapper {

    /**
     * 로그인 ID로 사용자 조회 (삭제되지 않은 사용자만)
     */
    @Select("""
        SELECT u.USER_ID as userId, u.LOGIN_ID as id, u.PASSWORD as password, 
               u.IS_ACTIVATE as isActivate, u.ROLE as role, u.IS_DELETED as isDeleted,
               u.CREATED_AT as createdAt, u.UPDATED_AT as updatedAt, u.DELETED_AT as deletedAt
        FROM USERS u 
        WHERE u.LOGIN_ID = #{loginId} AND u.IS_DELETED = 0
    """)
    Optional<Auth> findByLoginIdAndNotDeleted(@Param("loginId") String loginId);

    /**
     * 로그인 ID로 사용자 조회 (삭제된 사용자 포함)
     */
    @Select("""
        SELECT u.USER_ID as userId, u.LOGIN_ID as id, u.PASSWORD as password, 
               u.IS_ACTIVATE as isActivate, u.ROLE as role, u.IS_DELETED as isDeleted,
               u.CREATED_AT as createdAt, u.UPDATED_AT as updatedAt, u.DELETED_AT as deletedAt
        FROM USERS u 
        WHERE u.LOGIN_ID = #{loginId}
    """)
    Optional<Auth> findByLoginId(@Param("loginId") String loginId);

    /**
     * 활성화된 사용자만 조회
     */
    @Select("""
        SELECT u.USER_ID as userId, u.LOGIN_ID as id, u.PASSWORD as password, 
               u.IS_ACTIVATE as isActivate, u.ROLE as role, u.IS_DELETED as isDeleted,
               u.CREATED_AT as createdAt, u.UPDATED_AT as updatedAt, u.DELETED_AT as deletedAt
        FROM USERS u 
        WHERE u.LOGIN_ID = #{loginId} AND u.IS_DELETED = 0 AND u.IS_ACTIVATE = 1
    """)
    Optional<Auth> findActiveUserByLoginId(@Param("loginId") String loginId);

    /**
     * 사용자 존재 여부 확인
     */
    @Select("SELECT COUNT(*) > 0 FROM USERS WHERE LOGIN_ID = #{loginId} AND IS_DELETED = 0")
    boolean existsByLoginIdAndNotDeleted(@Param("loginId") String loginId);

    /**
     * 사용자 정보 업데이트
     */
    @Update("""
        UPDATE USERS 
        SET UPDATED_AT = CURRENT_TIMESTAMP 
        WHERE USER_ID = #{userId}
    """)
    int updateUser(@Param("userId") Long userId);

    /**
     * 사용자의 최근 로그인 실패 횟수와 잠김 상태 조회
     */
    @Select("""
        <script>
        SELECT u.USER_ID as userId, u.LOGIN_ID as id, u.PASSWORD as password, 
               u.IS_ACTIVATE as isActivate, u.ROLE as role, u.IS_DELETED as isDeleted,
               u.CREATED_AT as createdAt, u.UPDATED_AT as updatedAt, u.DELETED_AT as deletedAt,
               COALESCE(lh.CONSECUTIVE_FAILED_LOGIN_ATTEMPT, 0) as failCount,
               CASE 
                   WHEN lh.IS_LOCKED = 1 AND lh.CREATED_AT >= DATE_SUB(NOW(), INTERVAL 24 HOUR) 
                   THEN 1 
                   ELSE 0 
               END as locked
        FROM USERS u
        LEFT JOIN (
            SELECT USER_ID, CONSECUTIVE_FAILED_LOGIN_ATTEMPT, IS_LOCKED, CREATED_AT,
                   ROW_NUMBER() OVER (PARTITION BY USER_ID ORDER BY CREATED_AT DESC) as rn
            FROM LOGIN_HISTORY
        ) lh ON u.USER_ID = lh.USER_ID AND lh.rn = 1
        WHERE u.LOGIN_ID = #{loginId}
        </script>
    """)
    Optional<Auth> findUserWithFailCount(@Param("loginId") String loginId);
}
