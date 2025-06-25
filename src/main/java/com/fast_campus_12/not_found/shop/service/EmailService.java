package com.fast_campus_12.not_found.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

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

    /**
     * 임시 비밀번호 이메일 발송
     */
    public boolean sendTempPasswordEmail(String email, String tempPassword) {
        try {
            log.info("임시 비밀번호 이메일 발송 시작: email={}", email);
            String subject = "[쇼핑몰] 임시 비밀번호 발급 안내";
            String htmlContent = buildTempPasswordEmailContent(tempPassword);
            // 이메일 발송 (실제 구현 시 SMTP 설정 필요)
            // mailSender.send(email, subject, htmlContent);
            log.info("임시 비밀번호 이메일 발송 완료: email={}", email);
            return true;

        } catch (Exception e) {
            log.error("임시 비밀번호 이메일 발송 실패: email={}, error={}", email, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 임시 비밀번호 이메일 내용 생성
     */
    private String buildTempPasswordEmailContent(String tempPassword) {
        return String.format("""
        <html>
        <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5;">
            <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                <h2 style="color: #333; text-align: center; margin-bottom: 30px;">임시 비밀번호 발급 안내</h2>
                
                <p style="color: #666; line-height: 1.6; margin-bottom: 20px;">
                    안녕하세요.<br>
                    요청하신 임시 비밀번호가 발급되었습니다.
                </p>
                
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 6px; margin: 20px 0; text-align: center;">
                    <p style="margin: 0; color: #333; font-size: 14px;">임시 비밀번호</p>
                    <p style="margin: 10px 0 0 0; font-size: 24px; font-weight: bold; color: #007bff; letter-spacing: 2px;">%s</p>
                </div>
                
                <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 6px; padding: 15px; margin: 20px 0;">
                    <h4 style="color: #856404; margin: 0 0 10px 0; font-size: 16px;">⚠️ 보안 안내</h4>
                    <ul style="color: #856404; margin: 0; padding-left: 20px;">
                        <li>임시 비밀번호는 24시간 후 만료됩니다.</li>
                        <li>로그인 후 즉시 새로운 비밀번호로 변경해주세요.</li>
                        <li>임시 비밀번호는 타인에게 노출되지 않도록 주의하세요.</li>
                    </ul>
                </div>
                
                <div style="text-align: center; margin-top: 30px;">
                    <a href="#" style="background-color: #007bff; color: white; text-decoration: none; padding: 12px 30px; border-radius: 6px; display: inline-block;">
                        로그인 하기
                    </a>
                </div>
                
                <p style="color: #999; font-size: 12px; text-align: center; margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;">
                    본 메일은 발신전용입니다. 문의사항이 있으시면 고객센터로 연락해주세요.
                </p>
            </div>
        </body>
        </html>
        """, tempPassword);
    }
}