package com.fast_campus_12.not_found.shop.domain.notice.repository;

import com.fast_campus_12.not_found.shop.domain.notice.dto.NoticeDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class NoticeRepository {

    private final Map<Long, NoticeDto> noticeMap = new HashMap<>();

    public NoticeRepository() {
        //DB
        noticeMap.put(1L, new NoticeDto(1L, "📦 배송 지연 안내", "택배 파업으로 인해 일부 배송이 지연됩니다.", "관리자", 105, LocalDateTime.now().minusDays(1), false));
        noticeMap.put(2L, new NoticeDto(2L, "🎉 여름 이벤트 안내", "신규 가입 시 쿠폰 증정!", "관리자", 250, LocalDateTime.now().minusDays(2),true));
        noticeMap.put(3L, new NoticeDto(3L, "💳 무이자 할부 변경", "카드사별 무이자 혜택이 변경됩니다.", "관리자", 180, LocalDateTime.now().minusDays(3), false));
        noticeMap.put(4L, new NoticeDto(4L, "🔧 시스템 점검 안내", "이번 주말 오전 2~4시에 서버 점검이 예정되어 있습니다.", "관리자", 95, LocalDateTime.now().minusDays(4), false));
        noticeMap.put(5L, new NoticeDto(5L, "📢 개인정보 처리방침 개정", "개정된 처리방침을 확인해주세요.", "관리자", 130, LocalDateTime.now().minusDays(5), true));
        noticeMap.put(6L, new NoticeDto(6L, "📬 이메일 수신 설정 변경 안내", "스팸 필터에 따라 이메일 수신이 제한될 수 있습니다.", "관리자", 70, LocalDateTime.now().minusDays(6), false));
        noticeMap.put(7L, new NoticeDto(7L, "🛍️ 인기 상품 재입고", "품절되었던 인기 상품이 다시 입고되었습니다.", "관리자", 300, LocalDateTime.now().minusDays(7), true));
        noticeMap.put(8L, new NoticeDto(8L, "📆 추석 연휴 배송 일정", "추석 연휴 전 배송 마감 일정을 안내드립니다.", "관리자", 210, LocalDateTime.now().minusDays(8), false));
        noticeMap.put(10L, new NoticeDto(9L, "🎁 회원 등급별 혜택 안내", "등급별 혜택을 확인하고 더 많은 포인트를 받아보세요!", "관리자", 180, LocalDateTime.now().minusDays(10), true));
        noticeMap.put(11L, new NoticeDto(10L, "🧾 세금계산서 발급 지연", "세금계산서 발급이 시스템 점검으로 일시 중단됩니다.", "관리자", 42, LocalDateTime.now().minusDays(11), false));
        noticeMap.put(12L, new NoticeDto(11L, "📌 이용약관 변경 안내", "이용약관이 일부 변경되었습니다. 확인 바랍니다.", "관리자", 122, LocalDateTime.now().minusDays(12), true));
        noticeMap.put(13L, new NoticeDto(12L, "⚠️ 로그인 오류 안내", "일부 사용자에게 로그인 장애가 발생했으며 복구 완료되었습니다.", "관리자", 300, LocalDateTime.now().minusDays(13), false));

    }

    public List<NoticeDto> findAll() {
        return new ArrayList<>(noticeMap.values());
    }

    public Optional<NoticeDto> findById(Long id) {
        return Optional.ofNullable(noticeMap.get(id));
    }
}
