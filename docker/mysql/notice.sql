-- 공지사항 데이터 INSERT 쿼리
INSERT INTO `NOTICE` (
    `TITLE`,
    `CONTENT`,
    `CATEGORY`,
    `IS_PINNED`,
    `IS_ACTIVE`,
    `VIEW_COUNT`,
    `CREATED_AT`,
    `CREATED_BY`
) VALUES
      ('📦 배송 지연 안내', '택배 파업으로 인해 일부 배송이 지연됩니다.', 'DELIVERY', 0, 1, 105, DATE_SUB(NOW(), INTERVAL 1 DAY), '관리자'),
      ('🎉 여름 이벤트 안내', '신규 가입 시 쿠폰 증정!', 'EVENT', 1, 1, 250, DATE_SUB(NOW(), INTERVAL 2 DAY), '관리자'),
      ('💳 무이자 할부 변경', '카드사별 무이자 혜택이 변경됩니다.', 'GENERAL', 0, 1, 180, DATE_SUB(NOW(), INTERVAL 3 DAY), '관리자'),
      ('🔧 시스템 점검 안내', '이번 주말 오전 2~4시에 서버 점검이 예정되어 있습니다.', 'SYSTEM', 0, 1, 95, DATE_SUB(NOW(), INTERVAL 4 DAY), '관리자'),
      ('📢 개인정보 처리방침 개정', '개정된 처리방침을 확인해주세요.', 'GENERAL', 1, 1, 130, DATE_SUB(NOW(), INTERVAL 5 DAY), '관리자'),
      ('📬 이메일 수신 설정 변경 안내', '스팸 필터에 따라 이메일 수신이 제한될 수 있습니다.', 'SYSTEM', 0, 1, 70, DATE_SUB(NOW(), INTERVAL 6 DAY), '관리자'),
      ('🛍️ 인기 상품 재입고', '품절되었던 인기 상품이 다시 입고되었습니다.', 'EVENT', 1, 1, 300, DATE_SUB(NOW(), INTERVAL 7 DAY), '관리자'),
      ('📆 추석 연휴 배송 일정', '추석 연휴 전 배송 마감 일정을 안내드립니다.', 'DELIVERY', 0, 1, 210, DATE_SUB(NOW(), INTERVAL 8 DAY), '관리자'),
      ('🎁 회원 등급별 혜택 안내', '등급별 혜택을 확인하고 더 많은 포인트를 받아보세요!', 'EVENT', 1, 1, 180, DATE_SUB(NOW(), INTERVAL 10 DAY), '관리자'),
      ('🧾 세금계산서 발급 지연', '세금계산서 발급이 시스템 점검으로 일시 중단됩니다.', 'SYSTEM', 0, 1, 42, DATE_SUB(NOW(), INTERVAL 11 DAY), '관리자'),
      ('📌 이용약관 변경 안내', '이용약관이 일부 변경되었습니다. 확인 바랍니다.', 'GENERAL', 1, 1, 122, DATE_SUB(NOW(), INTERVAL 12 DAY), '관리자'),
      ('⚠️ 로그인 오류 안내', '일부 사용자에게 로그인 장애가 발생했으며 복구 완료되었습니다.', 'SYSTEM', 0, 1, 300, DATE_SUB(NOW(), INTERVAL 13 DAY), '관리자');