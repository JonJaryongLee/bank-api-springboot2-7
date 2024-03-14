-- 유저 데이터 생성
INSERT INTO User (username, password, is_superuser, nickname, age, money, salary, favorite_bank, tendency)
VALUES ('jony123', 'password1', TRUE, '조니', 40, 1000000, 500000, 'KB국민은행', '알뜰형'),
       ('sylvie123', 'password2', FALSE, '실비', 25, 2000000, 600000, '신한은행', '도전형'),
       ('nana123', 'password3', FALSE, '나나', 10, 1500000, 400000, '하나은행', '성실형');

-- 게시글 데이터 생성
INSERT INTO Article (user_id, title, content, created_at, updated_at)
VALUES (1, '투자 팁 드립니다.', '엔비디아는 꼭 오를거같아요', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, '반도체가 반등할까요?', '아직 잘 모르겠네요. 개인적으론 제약에 투자하는게 나을거같기도 해요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, '역시 코인보다 주식이네요', '코인은 정말 불안해서 못하겠어요', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 댓글 데이터 생성
INSERT INTO Comment (user_id, article_id, content, created_at, updated_at)
VALUES (1, 1, '진짜입니다. 저만 믿어보십시오.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, '엔비디아 안 샀는데 후회되네요...', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 3, '맞아요. 저도 결국 주식으로 돌아왔습니다.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
