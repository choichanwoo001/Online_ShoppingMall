package com.fast_campus_12.not_found.shop.qna.service;

import com.fast_campus_12.not_found.shop.qna.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class DevQnaServiceImpl implements QnaService {

    @Override
    public List<QuestionDto> getAllQuestions() {
        return List.of(
                QuestionDto.builder().id(1L).nickname("고사장").content("사이즈 정사이즈인가요?")
                        .createdAt(LocalDateTime.now().minusHours(5)).isSecret(false).answer("네, 정사이즈입니다 :)").build(),
                QuestionDto.builder().id(2L).nickname("익명").content("화이트는 비침 있나요?")
                        .createdAt(LocalDateTime.now().minusDays(1)).isSecret(true).answer(null).build(),
                QuestionDto.builder().id(3L).nickname("김포포").content("재입고 언제예정인가요?")
                        .createdAt(LocalDateTime.now().minusDays(2)).isSecret(false).answer("다음주 초 예정입니다").build(),
                QuestionDto.builder().id(4L).nickname("포근이").content("배송 얼마나 걸리나요?")
                        .createdAt(LocalDateTime.now().minusHours(30)).isSecret(false).answer("1~2일 소요돼요").build(),
                QuestionDto.builder().id(5L).nickname("쇼핑요정").content("하의랑 세트인가요?")
                        .createdAt(LocalDateTime.now().minusHours(10)).isSecret(false).answer("세트로 구매 가능합니다.").build(),
                QuestionDto.builder().id(6L).nickname("민수아빠").content("2XL도 나오나요?")
                        .createdAt(LocalDateTime.now().minusMinutes(20)).isSecret(true).answer("XL까지 제작돼요").build(),
                QuestionDto.builder().id(7L).nickname("윤서").content("수량이 부족하다고 나와요")
                        .createdAt(LocalDateTime.now().minusHours(3)).isSecret(false).answer("품절입니다. 다음주 입고 예정").build(),
                QuestionDto.builder().id(8L).nickname("이쁜누나").content("포인트 사용 가능한가요?")
                        .createdAt(LocalDateTime.now().minusDays(3)).isSecret(false).answer("결제 시 사용 가능해요").build(),
                QuestionDto.builder().id(9L).nickname("newUser01").content("후기 적으면 적립금 주나요?")
                        .createdAt(LocalDateTime.now().minusDays(4)).isSecret(false).answer("텍스트 후기 500원 적립돼요").build(),
                QuestionDto.builder().id(10L).nickname("닉네임22").content("오늘 주문하면 내일 올까요?")
                        .createdAt(LocalDateTime.now().minusHours(7)).isSecret(false).answer("서울/경기권이면 가능해요").build(),
                QuestionDto.builder().id(11L).nickname("고사장").content("샘플 촬영한 모델 신장 알 수 있을까요?")
                        .createdAt(LocalDateTime.now().minusHours(14)).isSecret(false).answer("181cm / 68kg 입니다").build(),
                QuestionDto.builder().id(12L).nickname("익명고객").content("문의드립니다")
                        .createdAt(LocalDateTime.now().minusHours(6)).isSecret(true).answer("답변드렸습니다!").build(),
                QuestionDto.builder().id(13L).nickname("뽀로로").content("같은 디자인 긴팔 있나요?")
                        .createdAt(LocalDateTime.now().minusDays(2)).isSecret(false).answer(null).build(),
                QuestionDto.builder().id(14L).nickname("와우").content("기모 있는 버전인가요?")
                        .createdAt(LocalDateTime.now().minusMinutes(90)).isSecret(false).answer("아니요, 기본 원단입니다").build(),
                QuestionDto.builder().id(15L).nickname("만두").content("세탁기 돌려도 되나요?")
                        .createdAt(LocalDateTime.now().minusMinutes(55)).isSecret(false).answer("찬물 세탁 권장드립니다").build(),
                QuestionDto.builder().id(16L).nickname("고객1").content("남편 선물로 괜찮을까요?")
                        .createdAt(LocalDateTime.now().minusHours(12)).isSecret(false).answer("네! 만족도 높은 제품입니다").build(),
                QuestionDto.builder().id(17L).nickname("윤이").content("목 부분 늘어나진 않나요?")
                        .createdAt(LocalDateTime.now().minusHours(9)).isSecret(false).answer("스판이 들어가 잘 안 늘어나요").build(),
                QuestionDto.builder().id(18L).nickname("lucky").content("방금 결제했어요")
                        .createdAt(LocalDateTime.now().minusMinutes(30)).isSecret(true).answer("주문 확인되었습니다 :)").build(),
                QuestionDto.builder().id(19L).nickname("고3").content("교환도 가능한가요?")
                        .createdAt(LocalDateTime.now().minusDays(1)).isSecret(false).answer("수령 후 7일 내 가능합니다").build(),
                QuestionDto.builder().id(20L).nickname("소현").content("아이보리 색상은 언제 들어오나요?")
                        .createdAt(LocalDateTime.now().minusDays(5)).isSecret(false).answer("이번 주 금요일 입고 예정입니다").build()
        );
    }

}
