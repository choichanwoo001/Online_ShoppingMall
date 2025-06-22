package com.fast_campus_12.not_found.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // 인증코드 저장용 (실제로는 Redis 사용 권장)
    private ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<String, String>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int EXPIRATION_MINUTES = 5;

    /**
     * 이메일 인증코드 발송
     */
    public String sendVerificationEmail(String email) {
        try {
            // 6자리 인증코드 생성
            String verificationCode = generateVerificationCode();

            // 메모리에 인증코드 저장 (5분 후 자동 삭제)
            verificationCodes.put(email, verificationCode);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    verificationCodes.remove(email);
                }
            }, EXPIRATION_MINUTES, TimeUnit.MINUTES);

            // 이메일 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[쇼핑몰] 이메일 인증코드");
            message.setText("안녕하세요.\n\n" +
                    "회원가입을 위한 이메일 인증코드입니다.\n\n" +
                    "인증코드: " + verificationCode + "\n\n" +
                    "인증코드는 " + EXPIRATION_MINUTES + "분 후 만료됩니다.\n\n" +
                    "감사합니다.");

            mailSender.send(message);

            return verificationCode;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 이메일 인증코드 확인
     */
    public boolean verifyEmailCode(String email, String code) {
        try {
            String savedCode = verificationCodes.get(email);

            if (Objects.equals(savedCode, code)) {
                // 인증 성공 시 코드 삭제
                verificationCodes.remove(email);
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 6자리 인증코드 생성
     */
    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}