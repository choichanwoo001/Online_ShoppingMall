package com.fastcampus.mall;

import com.fastcampus.mall.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jpa-auto-test.xml"})
public class JpaConnectionTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * JPA 자동 테이블 생성 및 CRUD 테스트
     */
    @Test
    public void testJpaAutoTableCreation() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            System.out.println("🔄 JPA 자동 테이블 생성 및 데이터 저장 테스트 시작...");

            // 1. User 엔티티 생성
            User user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setPassword("password123");
            user.setFullName("테스트 사용자");
            user.setIsActive(true);

            // 2. 저장 (테이블이 자동 생성됨)
            em.persist(user);
            em.flush(); // 즉시 DB에 반영

            assertNotNull("자동 생성된 ID가 null", user.getId());
            System.out.println("✅ 사용자 저장 성공! 생성된 ID: " + user.getId());

            // 3. 조회 테스트
            User foundUser = em.find(User.class, user.getId());
            assertNotNull("저장된 사용자 조회 실패", foundUser);
            assertEquals("사용자명이 일치하지 않음", "testuser", foundUser.getUsername());
            assertEquals("이메일이 일치하지 않음", "test@example.com", foundUser.getEmail());

            System.out.println("✅ 사용자 조회 성공! 사용자명: " + foundUser.getUsername());

            // 4. 수정 테스트
            foundUser.setFullName("수정된 사용자");
            em.flush();

            User updatedUser = em.find(User.class, user.getId());
            assertEquals("이름 수정 실패", "수정된 사용자", updatedUser.getFullName());

            System.out.println("✅ 사용자 수정 성공! 새 이름: " + updatedUser.getFullName());

            // 5. @PrePersist, @PreUpdate 동작 확인
            assertNotNull("생성일이 자동 설정되지 않음", foundUser.getCreatedAt());
            assertNotNull("수정일이 자동 설정되지 않음", foundUser.getUpdatedAt());

            System.out.println("✅ 자동 날짜 설정 성공!");
            System.out.println("   생성일: " + foundUser.getCreatedAt());
            System.out.println("   수정일: " + foundUser.getUpdatedAt());

            em.getTransaction().commit();

            System.out.println("🎉 JPA 자동 기능 테스트 완료!");

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("❌ JPA 테스트 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * JPA 자동 쿼리 생성 테스트
     */
    @Test
    public void testJpaQueries() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            System.out.println("🔄 JPA 쿼리 테스트 시작...");

            // 테스트 데이터 생성
            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("user1@example.com");
            user1.setPassword("password");
            user1.setIsActive(true);

            User user2 = new User();
            user2.setUsername("user2");
            user2.setEmail("user2@example.com");
            user2.setPassword("password");
            user2.setIsActive(false);

            em.persist(user1);
            em.persist(user2);
            em.flush();

            // JPQL 쿼리 테스트
            Long activeCount = em.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.isActive = true", Long.class)
                    .getSingleResult();

            assertEquals("활성 사용자 수가 맞지 않음", Long.valueOf(1), activeCount);
            System.out.println("✅ JPQL 쿼리 성공! 활성 사용자 수: " + activeCount);

            // 네이티브 쿼리 테스트
            Object result = em.createNativeQuery("SELECT COUNT(*) FROM users")
                    .getSingleResult();

            assertTrue("전체 사용자 수 확인 실패", ((Number) result).intValue() >= 2);
            System.out.println("✅ 네이티브 쿼리 성공! 전체 사용자 수: " + result);

            em.getTransaction().commit();

            System.out.println("🎉 JPA 쿼리 테스트 완료!");

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("❌ JPA 쿼리 테스트 실패: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }
}