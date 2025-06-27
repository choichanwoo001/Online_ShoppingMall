-- ========================================
-- 완전한 버전: PRODUCT부터 모든 더미데이터
-- 처음부터 전체 데이터를 생성하는 완전한 스크립트
-- ========================================

-- 1. PRODUCT 테이블 더미 데이터 (먼저 생성)
INSERT INTO `PRODUCT` (`CATEGORY_LV3_ID`, `PRODUCT_TITLE`, `PRODUCT_PRICE`, `PRODUCT_SUMMARY`, `PRODUCT_THUMBNAIL`, `PRODUCT_TAG`, `PRODUCT_SEASON`, `PRODUCT_AVERAGE_RATING`, `PRODUCT_SALE_COUNT`, `PRODUCT_VIEW_COUNT`, `PRODUCT_STOCK`, `PRODUCT_AVAILABLE_DATE`, `IS_ENABLED`, `IS_BEST`, `IS_NEW`)
SELECT * FROM (
                  SELECT 'C12' as CATEGORY_LV3_ID, '베이직 면 티셔츠' as PRODUCT_TITLE, 29900.00 as PRODUCT_PRICE,
                         '편안한 착용감의 기본 면 티셔츠입니다. 데일리룩으로 완벽!' as PRODUCT_SUMMARY,
                         '/images/tshirt_001.jpg' as PRODUCT_THUMBNAIL, '베이직,면,티셔츠,데일리' as PRODUCT_TAG,
                         '1111' as PRODUCT_SEASON, 4.5 as PRODUCT_AVERAGE_RATING, 156 as PRODUCT_SALE_COUNT,
                         1234 as PRODUCT_VIEW_COUNT, 50 as PRODUCT_STOCK, '2024-01-15 00:00:00' as PRODUCT_AVAILABLE_DATE,
                         1 as IS_ENABLED, 1 as IS_BEST, 0 as IS_NEW
                  UNION ALL SELECT 'C23', '슬림핏 청바지', 89000.00,
                                   '몸매를 살려주는 슬림핏 청바지. 어떤 상의와도 잘 어울립니다.',
                                   '/images/jeans_001.jpg', '청바지,슬림핏,데님,캐주얼', '1111', 4.2, 89, 967, 25,
                                   '2024-02-01 00:00:00', 1, 0, 0
                  UNION ALL SELECT 'C10', '플리스 후드집업', 59900.00,
                                   '따뜻하고 편안한 플리스 소재의 후드집업. 겨울철 필수템!',
                                   '/images/hoodie_001.jpg', '플리스,후드,집업,겨울,따뜻함', '0001', 4.7, 234, 1567, 15,
                                   '2024-10-01 00:00:00', 1, 1, 1
                  UNION ALL SELECT 'C22', '스트라이프 긴팔 티셔츠', 39900.00,
                                   '깔끔한 스트라이프 패턴의 긴팔 티셔츠. 봄가을 시즌 추천!',
                                   '/images/stripe_tshirt.jpg', '스트라이프,긴팔,봄,가을', '1001', 4.1, 67, 543, 30,
                                   '2024-03-15 00:00:00', 1, 0, 0
                  UNION ALL SELECT 'C20', '체크 셔츠', 69900.00,
                                   '클래식한 체크 패턴의 셔츠. 비즈니스 캐주얼로 완벽!',
                                   '/images/check_shirt.jpg', '체크,셔츠,비즈니스,캐주얼,클래식', '1001', 3.9, 78, 456, 35,
                                   '2024-04-01 00:00:00', 1, 0, 0
                  UNION ALL SELECT 'C30', '운동화', 129000.00,
                                   '편안한 착용감의 런닝화. 운동할 때나 일상생활에서 모두 활용!',
                                   '/images/sneakers_001.jpg', '운동화,런닝,편안함,스포츠', '1111', 4.6, 312, 2145, 20,
                                   '2024-02-15 00:00:00', 1, 1, 0
                  UNION ALL SELECT 'C25', '카고 팬츠', 79000.00,
                                   '실용적인 포켓이 많은 카고 팬츠. 아웃도어 활동에 최적!',
                                   '/images/cargo_pants.jpg', '카고,팬츠,실용적,아웃도어,포켓', '1110', 4.4, 92, 678, 0,
                                   '2024-05-01 00:00:00', 0, 0, 0
                  UNION ALL SELECT 'C16', '코튼 가디건', 49900.00,
                                   '부드러운 코튼 소재의 가디건. 사계절 활용 가능한 아이템!',
                                   '/images/cardigan_001.jpg', '가디건,코튼,사계절,심플', '1111', 4.3, 123, 890, 40,
                                   '2024-03-20 00:00:00', 1, 0, 0
                  UNION ALL SELECT 'C31', '레더 백팩', 149000.00,
                                   '고급 가죽으로 제작된 백팩. 비즈니스와 캐주얼 모두 활용!',
                                   '/images/backpack_001.jpg', '백팩,가죽,비즈니스,고급', '1111', 4.8, 67, 234, 15,
                                   '2024-04-10 00:00:00', 1, 1, 1
                  UNION ALL SELECT 'C1', '울 코트', 299000.00,
                                   '고급 울 소재의 겨울 코트. 따뜻하고 세련된 디자인!',
                                   '/images/coat_001.jpg', '코트,울,겨울,고급,세련', '0001', 4.9, 45, 567, 8,
                                   '2024-11-01 00:00:00', 1, 1, 1
                  UNION ALL SELECT 'C26', '데님 반바지', 39900.00,
                                   '여름철 필수 아이템인 데님 반바지. 시원하고 편안한 착용감!',
                                   '/images/shorts_001.jpg', '반바지,데님,여름,시원함', '0100', 4.0, 89, 345, 60,
                                   '2024-05-15 00:00:00', 1, 0, 0
                  UNION ALL SELECT 'C13', '나시 티셔츠', 19900.00,
                                   '여름용 나시 티셔츠. 시원한 착용감과 깔끔한 핏!',
                                   '/images/tank_top_001.jpg', '나시,여름,시원함,심플', '0100', 3.8, 134, 678, 80,
                                   '2024-06-01 00:00:00', 1, 0, 0
              ) AS new_products
WHERE NOT EXISTS (
    SELECT 1 FROM `PRODUCT` p WHERE p.PRODUCT_TITLE = new_products.PRODUCT_TITLE
);

-- 2. USERS 테이블 더미 데이터 (관리자 + 일반사용자)
INSERT INTO `USERS` (`LOGIN_ID`, `PASSWORD`, `IS_ACTIVATE`, `ROLE`, `IS_DELETED`)
SELECT * FROM (
                  SELECT 'admin001' as LOGIN_ID, '$2b$10$abcdefghijklmnopqrstuvwxyz123456789' as PASSWORD, 1 as IS_ACTIVATE, 'ADMIN' as ROLE, 0 as IS_DELETED
                  UNION ALL SELECT 'admin002', '$2b$10$bcdefghijklmnopqrstuvwxyz1234567890', 1, 'ADMIN', 0
                  UNION ALL SELECT 'user001', '$2b$10$cdefghijklmnopqrstuvwxyz12345678901', 1, 'USER', 0
                  UNION ALL SELECT 'user002', '$2b$10$defghijklmnopqrstuvwxyz123456789012', 1, 'USER', 0
                  UNION ALL SELECT 'user003', '$2b$10$efghijklmnopqrstuvwxyz1234567890123', 1, 'USER', 0
                  UNION ALL SELECT 'user004', '$2b$10$fghijklmnopqrstuvwxyz12345678901234', 0, 'USER', 0
                  UNION ALL SELECT 'user005', '$2b$10$ghijklmnopqrstuvwxyz123456789012345', 1, 'USER', 0
                  UNION ALL SELECT 'user006', '$2b$10$hijklmnopqrstuvwxyz1234567890123456', 1, 'USER', 0
                  UNION ALL SELECT 'user007', '$2b$10$ijklmnopqrstuvwxyz12345678901234567', 1, 'USER', 1
                  UNION ALL SELECT 'user008', '$2b$10$jklmnopqrstuvwxyz123456789012345678', 1, 'USER', 0
                  UNION ALL SELECT 'user009', '$2b$10$klmnopqrstuvwxyz1234567890123456789', 1, 'USER', 0
                  UNION ALL SELECT 'user010', '$2b$10$lmnopqrstuvwxyz12345678901234567890', 1, 'USER', 0
              ) AS new_users
WHERE NOT EXISTS (
    SELECT 1 FROM `USERS` u WHERE u.LOGIN_ID = new_users.LOGIN_ID
);

-- 3. USER_DETAIL 테이블 더미 데이터 (USER 역할만)
INSERT INTO `USER_DETAIL` (`USER_ID`, `EMAIL`, `NAME`, `PHONE_NUMBER`, `BIRTH_DATE`, `GENDER`, `JOB_CODE`)
SELECT u.USER_ID, details.EMAIL, details.NAME, details.PHONE_NUMBER, details.BIRTH_DATE, details.GENDER, details.JOB_CODE
FROM `USERS` u
         INNER JOIN (
    SELECT 'user001' as LOGIN_ID, 'kim.minho@email.com' as EMAIL, '김민호' as NAME, '010-1234-5678' as PHONE_NUMBER, '1990-03-15' as BIRTH_DATE, 'M' as GENDER, 101 as JOB_CODE
    UNION ALL SELECT 'user002', 'lee.suji@email.com', '이수지', '010-2345-6789', '1985-07-22', 'F', 102
    UNION ALL SELECT 'user003', 'park.jiwon@email.com', '박지원', '010-3456-7890', '1992-11-08', 'F', 103
    UNION ALL SELECT 'user004', 'choi.donghyun@email.com', '최동현', '010-4567-8901', '1988-01-30', 'M', 104
    UNION ALL SELECT 'user005', 'jung.minji@email.com', '정민지', '010-5678-9012', '1995-05-12', 'F', 105
    UNION ALL SELECT 'user006', 'kang.seongho@email.com', '강성호', '010-6789-0123', '1987-09-25', 'M', 106
    UNION ALL SELECT 'user007', 'yoon.hyejin@email.com', '윤혜진', '010-7890-1234', '1993-12-03', 'F', 107
    UNION ALL SELECT 'user008', 'lim.jaewook@email.com', '임재욱', '010-8901-2345', '1991-06-18', 'M', 108
    UNION ALL SELECT 'user009', 'song.eunha@email.com', '송은하', '010-9012-3456', '1994-08-27', 'F', 109
    UNION ALL SELECT 'user010', 'han.jiseok@email.com', '한지석', '010-0123-4567', '1989-11-14', 'M', 110
) AS details ON u.LOGIN_ID = details.LOGIN_ID
WHERE u.ROLE = 'USER'
  AND NOT EXISTS (
    SELECT 1 FROM `USER_DETAIL` ud WHERE ud.USER_ID = u.USER_ID
);

-- 4. ADMIN_DETAIL 테이블 더미 데이터 (ADMIN 역할만)
INSERT INTO `ADMIN_DETAIL` (`USER_ID`, `NICKNAME`, `NAME`)
SELECT u.USER_ID, details.NICKNAME, details.NAME
FROM `USERS` u
         INNER JOIN (
    SELECT 'admin001' as LOGIN_ID, 'SuperAdmin' as NICKNAME, '관리자1' as NAME
    UNION ALL SELECT 'admin002', 'ServiceAdmin', '관리자2'
) AS details ON u.LOGIN_ID = details.LOGIN_ID
WHERE u.ROLE = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM `ADMIN_DETAIL` ad WHERE ad.USER_ID = u.USER_ID
);

-- 5. INQUIRY 테이블 더미 데이터 (상품과 사용자 동적 연결)
INSERT INTO `INQUIRY` (`USER_ID`, `PRODUCT_ID`, `TITLE`, `CONTENT`, `INQUIRY_CATEGORY`, `STATUS`, `IS_SECRET`, `CREATED_AT`)
SELECT
    u.USER_ID,
    CASE
        WHEN inq.PRODUCT_TITLE IS NOT NULL THEN
            (SELECT p.PRODUCT_ID FROM `PRODUCT` p WHERE p.PRODUCT_TITLE = inq.PRODUCT_TITLE LIMIT 1)
        ELSE NULL
        END as PRODUCT_ID,
    inq.TITLE,
    inq.CONTENT,
    inq.INQUIRY_CATEGORY,
    inq.STATUS,
    inq.IS_SECRET,
    inq.CREATED_AT
FROM (
         -- 최근 문의들 (다양한 시간대)
         SELECT 'user001' as LOGIN_ID, '베이직 면 티셔츠' as PRODUCT_TITLE, '사이즈 문의드립니다' as TITLE,
                '안녕하세요. 이 티셔츠 L사이즈 실측 사이즈가 어떻게 되나요? 평소 105 입는데 맞을까요?' as CONTENT,
                'PRODUCT' as INQUIRY_CATEGORY, 'ANSWERED' as STATUS, 0 as IS_SECRET,
                DATE_SUB(NOW(), INTERVAL 2 DAY) as CREATED_AT
         UNION ALL SELECT 'user002', '슬림핏 청바지', '배송 언제 되나요?',
                          '어제 주문했는데 언제쯤 받을 수 있을까요? 급하게 필요해서요.',
                          'DELIVERY', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 1 DAY)
         UNION ALL SELECT 'user003', NULL, '결제 오류 발생',
                          '카드 결제했는데 실패했다고 나왔는데 돈은 빠져나갔어요. 확인 부탁드립니다.',
                          'PAYMENT', 'PENDING', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)
         UNION ALL SELECT 'user004', '플리스 후드집업', '색상이 다릅니다',
                          '주문한 네이비색과 받은 상품 색이 달라요. 교환 가능한가요?',
                          'PRODUCT', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 5 HOUR)
         UNION ALL SELECT 'user005', '베이직 면 티셔츠', '환불 신청합니다',
                          '사이즈가 너무 커서 환불하고 싶습니다. 택도 안 떼었어요.',
                          'REFUND', 'CLOSED', 0, DATE_SUB(NOW(), INTERVAL 1 WEEK)
         UNION ALL SELECT 'user006', NULL, '적립금 문제',
                          '구매 후 적립금이 안들어왔어요. 언제 적립되나요?',
                          'ETC', 'PENDING', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR)
         UNION ALL SELECT 'user007', '스트라이프 긴팔 티셔츠', '재고 문의',
                          'M사이즈 언제 재입고 되나요? 계속 품절이네요.',
                          'PRODUCT', 'PENDING', 0, DATE_SUB(NOW(), INTERVAL 6 HOUR)
         UNION ALL SELECT 'user008', '체크 셔츠', '배송지 변경 요청',
                          '배송지를 변경하고 싶은데 어떻게 하나요? 아직 발송 전이에요.',
                          'DELIVERY', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 4 HOUR)
         UNION ALL SELECT 'user009', '운동화', '사이즈 교환',
                          '신발이 작아서 한 사이즈 큰 것으로 교환하고 싶어요.',
                          'PRODUCT', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 8 HOUR)
         UNION ALL SELECT 'user010', NULL, '회원 탈퇴 문의',
                          '개인 사정으로 회원 탈퇴하고 싶어요. 어떻게 하나요?',
                          'ETC', 'PENDING', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR)
         -- 추가 문의들
         UNION ALL SELECT 'user001', '카고 팬츠', '세탁 방법 문의',
                          '이 제품 세탁할 때 주의사항이 있나요?',
                          'PRODUCT', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 7 DAY)
         UNION ALL SELECT 'user002', NULL, '쿠폰 사용법',
                          '발급받은 쿠폰을 어떻게 사용하나요?',
                          'ETC', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 6 DAY)
         UNION ALL SELECT 'user003', '코튼 가디건', '추가 구매 할인',
                          '같은 상품 여러 개 사면 할인 되나요?',
                          'PAYMENT', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 5 DAY)
         UNION ALL SELECT 'user004', '레더 백팩', '교환 절차 문의',
                          '색상이 마음에 안들어서 교환하려는데 절차가 어떻게 되나요?',
                          'REFUND', 'CLOSED', 0, DATE_SUB(NOW(), INTERVAL 4 DAY)
         UNION ALL SELECT 'user005', NULL, '배송비 정책',
                          '무료배송 조건이 어떻게 되나요?',
                          'DELIVERY', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 30 DAY)
         UNION ALL SELECT 'user006', '울 코트', '품질 문의',
                          '소재가 정확히 뭔가요? 알레르기가 있어서요.',
                          'PRODUCT', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 25 DAY)
         UNION ALL SELECT 'user007', NULL, '멤버십 혜택',
                          'VIP 회원 혜택이 뭐가 있나요?',
                          'ETC', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 20 DAY)
         UNION ALL SELECT 'user008', '데님 반바지', 'A/S 문의',
                          '구매한 지 한달 됐는데 실밥이 나와요. A/S 가능한가요?',
                          'PRODUCT', 'CLOSED', 0, DATE_SUB(NOW(), INTERVAL 15 DAY)
         UNION ALL SELECT 'user009', '나시 티셔츠', '반품 신청',
                          '착용 한번 했는데 반품 가능한가요?',
                          'REFUND', 'CLOSED', 0, DATE_SUB(NOW(), INTERVAL 10 DAY)
         UNION ALL SELECT 'user010', NULL, '포인트 적립',
                          '리뷰 작성하면 포인트 얼마나 주나요?',
                          'ETC', 'ANSWERED', 0, DATE_SUB(NOW(), INTERVAL 8 DAY)
     ) AS inq
         INNER JOIN `USERS` u ON u.LOGIN_ID = inq.LOGIN_ID
WHERE u.ROLE = 'USER'
  AND NOT EXISTS (
    SELECT 1 FROM `INQUIRY` i
    WHERE i.USER_ID = u.USER_ID AND i.TITLE = inq.TITLE
);

-- 6. INQUIRY_ANSWER 테이블 더미 데이터 (답변된 문의만)
INSERT INTO `INQUIRY_ANSWER` (`INQUIRY_ID`, `ADMIN_ID`, `CONTENT`, `CREATED_AT`)
SELECT
    i.INQUIRY_ID,
    a.USER_ID as ADMIN_ID,
    ans.CONTENT,
    COALESCE(ans.CREATED_AT, DATE_ADD(i.CREATED_AT, INTERVAL FLOOR(RAND() * 24) + 1 HOUR)) as CREATED_AT
FROM (
         SELECT '사이즈 문의드립니다' as INQUIRY_TITLE, 'admin001' as ADMIN_LOGIN,
                '안녕하세요. L사이즈 실측은 가슴둘레 56cm, 총장 71cm입니다. 평소 105 착용하시면 딱 맞으실 것 같습니다!' as CONTENT,
                NULL as CREATED_AT
         UNION ALL SELECT '배송 언제 되나요?', 'admin002',
                          '주문해주신 상품은 오늘 오후 발송 예정이며, 내일 오전 중 받아보실 수 있습니다. 송장번호는 별도 문자로 안내드리겠습니다.',
                          NULL
         UNION ALL SELECT '색상이 다릅니다', 'admin001',
                          '불편을 드려 죄송합니다. 즉시 교환 처리해드리겠습니다. 택배 기사가 방문하여 상품 수거 후 새 제품으로 배송해드리겠습니다.',
                          NULL
         UNION ALL SELECT '배송지 변경 요청', 'admin002',
                          '아직 발송 전이라 변경 가능합니다. 새로운 주소로 업데이트해드렸으며, 내일 새 주소로 발송됩니다.',
                          NULL
         UNION ALL SELECT '사이즈 교환', 'admin001',
                          '사이즈 교환 신청 접수되었습니다. 택배기사 방문 예약은 고객센터로 연락주시면 안내해드리겠습니다.',
                          NULL
         UNION ALL SELECT '세탁 방법 문의', 'admin002',
                          '30도 이하 찬물에 중성세제로 단독세탁 권장하며, 건조기 사용은 피해주세요. 그늘에서 자연건조 해주시면 됩니다.',
                          NULL
         UNION ALL SELECT '쿠폰 사용법', 'admin001',
                          '결제 시 쿠폰 선택란에서 보유하신 쿠폰을 선택하시면 자동으로 할인 적용됩니다. 유효기간 확인하시고 사용해주세요.',
                          NULL
         UNION ALL SELECT '추가 구매 할인', 'admin002',
                          '동일 상품 2개 이상 구매시 10% 추가할인 적용됩니다. 장바구니에서 자동으로 할인가가 표시됩니다.',
                          NULL
         UNION ALL SELECT '배송비 정책', 'admin001',
                          '3만원 이상 구매시 무료배송이며, 제주/도서산간 지역은 추가 3000원입니다.',
                          NULL
         UNION ALL SELECT '품질 문의', 'admin002',
                          '폴리에스터 80%, 면 20% 혼방 소재입니다. 화학섬유 알레르기가 있으시다면 주의해서 착용해주세요.',
                          NULL
         UNION ALL SELECT '멤버십 혜택', 'admin001',
                          'VIP 회원은 모든 상품 5% 할인, 무료배송, 생일쿠폰, 신상품 우선구매 혜택이 제공됩니다.',
                          NULL
         UNION ALL SELECT '포인트 적립', 'admin002',
                          '포토리뷰 작성시 1000포인트, 일반리뷰 500포인트가 적립됩니다. 구매확정 후 작성 가능합니다.',
                          NULL
     ) AS ans
         INNER JOIN `INQUIRY` i ON i.TITLE = ans.INQUIRY_TITLE
         INNER JOIN `USERS` a ON a.LOGIN_ID = ans.ADMIN_LOGIN AND a.ROLE = 'ADMIN'
WHERE (i.STATUS = 'ANSWERED' OR i.STATUS = 'CLOSED')
  AND NOT EXISTS (
    SELECT 1 FROM `INQUIRY_ANSWER` ia
    WHERE ia.INQUIRY_ID = i.INQUIRY_ID AND ia.ADMIN_ID = a.USER_ID
);

-- ========================================
-- 데이터 확인 쿼리 (실행 후 확인용)
-- ========================================

-- 생성된 데이터 개수 확인
-- SELECT 'PRODUCT' as TABLE_NAME, COUNT(*) as COUNT FROM PRODUCT
-- UNION ALL SELECT 'USERS', COUNT(*) FROM USERS
-- UNION ALL SELECT 'USER_DETAIL', COUNT(*) FROM USER_DETAIL
-- UNION ALL SELECT 'ADMIN_DETAIL', COUNT(*) FROM ADMIN_DETAIL
-- UNION ALL SELECT 'INQUIRY', COUNT(*) FROM INQUIRY
-- UNION ALL SELECT 'INQUIRY_ANSWER', COUNT(*) FROM INQUIRY_ANSWER;

-- 문의 현황 요약
-- SELECT
--     INQUIRY_CATEGORY as 문의유형,
--     STATUS as 상태,
--     COUNT(*) as 건수
-- FROM INQUIRY
-- GROUP BY INQUIRY_CATEGORY, STATUS
-- ORDER BY INQUIRY_CATEGORY, STATUS;

-- 최근 문의 목록 (답변 여부 포함)
-- SELECT
--     i.INQUIRY_ID as 문의번호,
--     u.LOGIN_ID as 작성자,
--     p.PRODUCT_TITLE as 상품명,
--     i.TITLE as 제목,
--     i.INQUIRY_CATEGORY as 유형,
--     i.STATUS as 상태,
--     i.IS_SECRET as 비밀글,
--     i.CREATED_AT as 작성일,
--     CASE WHEN ia.INQUIRY_ANSWER_ID IS NOT NULL THEN 'Y' ELSE 'N' END as 답변여부
-- FROM INQUIRY i
-- INNER JOIN USERS u ON i.USER_ID = u.USER_ID
-- LEFT JOIN PRODUCT p ON i.PRODUCT_ID = p.PRODUCT_ID
-- LEFT JOIN INQUIRY_ANSWER ia ON i.INQUIRY_ID = ia.INQUIRY_ID
-- ORDER BY i.CREATED_AT DESC;