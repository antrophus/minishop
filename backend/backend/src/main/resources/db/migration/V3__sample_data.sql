/*
 * V3__sample_data.sql
 * 이 마이그레이션 스크립트는 개발 및 테스트 환경에서 사용할 샘플 데이터를 삽입합니다.
 * 주의: 프로덕션 환경에서는 별도의 프로파일 설정으로 이 스크립트가 실행되지 않도록 해야 합니다.
 */

-- 역할 및 권한 샘플 데이터
INSERT INTO roles (name) VALUES 
('ROLE_ADMIN'), ('ROLE_MANAGER'), ('ROLE_USER');

INSERT INTO permissions (name) VALUES
('READ_PRODUCTS'), ('WRITE_PRODUCTS'), ('READ_ORDERS'), ('MANAGE_ORDERS'), 
('READ_USERS'), ('MANAGE_USERS'), ('MANAGE_SYSTEM');

-- 역할-권한 매핑
-- 관리자 권한
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ROLE_ADMIN';

-- 매니저 권한
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ROLE_MANAGER' AND p.name IN ('READ_PRODUCTS', 'WRITE_PRODUCTS', 'READ_ORDERS', 'MANAGE_ORDERS', 'READ_USERS');

-- 일반 사용자 권한
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ROLE_USER' AND p.name IN ('READ_PRODUCTS', 'READ_ORDERS');

-- 테스트 사용자
INSERT INTO users (username, password, email, created_at) VALUES
('admin', '$2a$10$qBMveHBqri8AjZ3ydO5OQe7yoUPVKx1iMvG.ps74PXmCRbO5hDvAm', 'admin@mylittleshop.com', NOW()),  -- 비밀번호: admin123
('manager', '$2a$10$qBMveHBqri8AjZ3ydO5OQe7yoUPVKx1iMvG.ps74PXmCRbO5hDvAm', 'manager@mylittleshop.com', NOW()),  -- 비밀번호: admin123
('user1', '$2a$10$qBMveHBqri8AjZ3ydO5OQe7yoUPVKx1iMvG.ps74PXmCRbO5hDvAm', 'user1@example.com', NOW()),  -- 비밀번호: admin123
('user2', '$2a$10$qBMveHBqri8AjZ3ydO5OQe7yoUPVKx1iMvG.ps74PXmCRbO5hDvAm', 'user2@example.com', NOW());  -- 비밀번호: admin123

-- 사용자-역할 매핑
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'manager' AND r.name = 'ROLE_MANAGER';

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username IN ('user1', 'user2') AND r.name = 'ROLE_USER';

-- 사용자 프로필 정보
INSERT INTO user_profiles (user_id, full_name, phone, gender, birth_date) 
SELECT id, '관리자', '010-1234-5678', 'MALE', '1990-01-01' FROM users WHERE username = 'admin';

INSERT INTO user_profiles (user_id, full_name, phone, gender, birth_date) 
SELECT id, '매니저', '010-2345-6789', 'FEMALE', '1992-05-15' FROM users WHERE username = 'manager';

INSERT INTO user_profiles (user_id, full_name, phone, gender, birth_date) 
SELECT id, '홍길동', '010-3456-7890', 'MALE', '1995-10-20' FROM users WHERE username = 'user1';

INSERT INTO user_profiles (user_id, full_name, phone, gender, birth_date) 
SELECT id, '김철수', '010-4567-8901', 'MALE', '1988-03-08' FROM users WHERE username = 'user2';

-- 제품 카테고리
INSERT INTO product_categories (name, parent_id) VALUES
('전자제품', NULL),
('의류', NULL),
('식품', NULL),
('가구', NULL);

INSERT INTO product_categories (name, parent_id) 
SELECT '스마트폰', id FROM product_categories WHERE name = '전자제품';

INSERT INTO product_categories (name, parent_id) 
SELECT '노트북', id FROM product_categories WHERE name = '전자제품';

INSERT INTO product_categories (name, parent_id) 
SELECT '남성의류', id FROM product_categories WHERE name = '의류';

INSERT INTO product_categories (name, parent_id) 
SELECT '여성의류', id FROM product_categories WHERE name = '의류';

-- 제품 샘플 데이터
INSERT INTO products (name, regular_price, consumer_price, member_price, stock_quantity, status, is_active, category_id, description) 
SELECT 
  '최신 스마트폰', 
  1000000.00, 
  1100000.00, 
  950000.00, 
  100, 
  'AVAILABLE', 
  TRUE, 
  id,
  '최신 기술이 탑재된 프리미엄 스마트폰입니다. 고화질 카메라와 긴 배터리 수명이 특징입니다.'
FROM product_categories WHERE name = '스마트폰';

INSERT INTO products (name, regular_price, consumer_price, member_price, stock_quantity, status, is_active, category_id, description) 
SELECT 
  '게이밍 노트북', 
  1500000.00, 
  1600000.00, 
  1450000.00, 
  50, 
  'AVAILABLE', 
  TRUE, 
  id,
  '고성능 게이밍 노트북으로 최신 게임을 원활하게 즐길 수 있습니다. 고해상도 디스플레이와 빠른 프로세서가 특징입니다.'
FROM product_categories WHERE name = '노트북';

INSERT INTO products (name, regular_price, consumer_price, member_price, stock_quantity, status, is_active, category_id, description) 
SELECT 
  '남성 캐주얼 셔츠', 
  50000.00, 
  60000.00, 
  45000.00, 
  200, 
  'AVAILABLE', 
  TRUE, 
  id,
  '편안한 착용감의 남성용 캐주얼 셔츠입니다. 고급 소재로 제작되어 내구성이 뛰어납니다.'
FROM product_categories WHERE name = '남성의류';

INSERT INTO products (name, regular_price, consumer_price, member_price, stock_quantity, status, is_active, category_id, description) 
SELECT 
  '여성 원피스', 
  70000.00, 
  80000.00, 
  65000.00, 
  150, 
  'AVAILABLE', 
  TRUE, 
  id,
  '세련된 디자인의 여성용 원피스입니다. 다양한 행사에 착용하기 좋은 아이템입니다.'
FROM product_categories WHERE name = '여성의류';

-- 상품 내용 정보
INSERT INTO product_content (product_id, content, version, status) 
SELECT 
  id, 
  '<h1>최신 스마트폰</h1><p>혁신적인 기술과 세련된 디자인이 결합된 최신 스마트폰을 소개합니다.</p><h2>주요 특징</h2><ul><li>고해상도 카메라</li><li>긴 배터리 수명</li><li>빠른 프로세서</li><li>넉넉한 저장 공간</li></ul>', 
  1, 
  'PUBLISHED'
FROM products WHERE name = '최신 스마트폰';

INSERT INTO product_content (product_id, content, version, status) 
SELECT 
  id, 
  '<h1>게이밍 노트북</h1><p>최상의 게임 경험을 제공하는 고성능 게이밍 노트북입니다.</p><h2>주요 특징</h2><ul><li>최신 그래픽 카드</li><li>고해상도 디스플레이</li><li>빠른 SSD 저장장치</li><li>효율적인 쿨링 시스템</li></ul>', 
  1, 
  'PUBLISHED'
FROM products WHERE name = '게이밍 노트북';

-- 제품 미디어
INSERT INTO product_media (product_id, url, media_type, sort_order) 
SELECT id, 'https://example.com/images/smartphone1.jpg', 'IMAGE', 1 FROM products WHERE name = '최신 스마트폰';

INSERT INTO product_media (product_id, url, media_type, sort_order) 
SELECT id, 'https://example.com/images/smartphone2.jpg', 'IMAGE', 2 FROM products WHERE name = '최신 스마트폰';

INSERT INTO product_media (product_id, url, media_type, sort_order) 
SELECT id, 'https://example.com/videos/smartphone_review.mp4', 'VIDEO', 3 FROM products WHERE name = '최신 스마트폰';

INSERT INTO product_media (product_id, url, media_type, sort_order) 
SELECT id, 'https://example.com/images/laptop1.jpg', 'IMAGE', 1 FROM products WHERE name = '게이밍 노트북';

INSERT INTO product_media (product_id, url, media_type, sort_order) 
SELECT id, 'https://example.com/images/laptop2.jpg', 'IMAGE', 2 FROM products WHERE name = '게이밍 노트북';

-- 장바구니 샘플
INSERT INTO carts (user_id, status, created_at, expires_at) 
SELECT id, 'ACTIVE', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY) FROM users WHERE username = 'user1';

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price, applied_price_type) 
SELECT c.id, p.id, 1, p.regular_price, 'REGULAR' 
FROM carts c 
JOIN users u ON c.user_id = u.id 
JOIN products p ON p.name = '최신 스마트폰' 
WHERE u.username = 'user1';

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price, applied_price_type) 
SELECT c.id, p.id, 2, p.member_price, 'MEMBER' 
FROM carts c 
JOIN users u ON c.user_id = u.id 
JOIN products p ON p.name = '남성 캐주얼 셔츠' 
WHERE u.username = 'user1';

-- 구독 플랜 샘플
INSERT INTO subscription_plans (name, price, duration, billing_frequency, description, discount_percentage, trial_period_days) VALUES
('기본형', 9900.00, 1, 'MONTHLY', '매월 결제되는 기본 구독 서비스입니다.', 0.00, 7),
('표준형', 29900.00, 3, 'QUARTERLY', '3개월마다 결제되는 표준 구독 서비스입니다.', 10.00, 14),
('프리미엄', 99000.00, 12, 'ANNUAL', '연간 결제되는 프리미엄 구독 서비스입니다.', 20.00, 30);

-- 구독 샘플
INSERT INTO subscriptions (user_id, plan_id, product_id, next_delivery_date, status, created_at) 
SELECT 
  u.id, 
  sp.id, 
  p.id, 
  DATE_ADD(NOW(), INTERVAL 30 DAY), 
  'ACTIVE',
  NOW()
FROM users u 
JOIN subscription_plans sp ON sp.name = '기본형'
JOIN products p ON p.name = '최신 스마트폰'
WHERE u.username = 'user1';

-- 구독 결제 샘플
INSERT INTO subscription_payments (subscription_id, amount, original_amount, status, payment_date, created_at) 
SELECT 
  s.id, 
  sp.price,
  sp.price,
  'COMPLETED',
  NOW(),
  NOW()
FROM subscriptions s
JOIN subscription_plans sp ON s.plan_id = sp.id;

-- 배송 주소 샘플
INSERT INTO delivery_addresses (member_id, recipient_name, contact_number, address, is_default) 
SELECT 
  id, 
  '홍길동', 
  '010-1234-5678', 
  '서울시 강남구 테헤란로 123, 456동 7890호', 
  TRUE 
FROM users WHERE username = 'user1';

INSERT INTO delivery_addresses (member_id, recipient_name, contact_number, address, is_default) 
SELECT 
  id, 
  '홍길동(회사)', 
  '010-1234-5678', 
  '서울시 서초구 강남대로 123, 그랜드빌딩 3층', 
  FALSE 
FROM users WHERE username = 'user1';

-- FAQ 샘플 데이터
INSERT INTO faq (question, answer, category) VALUES
('배송은 얼마나 걸리나요?', '일반적으로 결제 확인 후 1-3일 내에 출고되며, 택배사 사정에 따라 1-2일 내에 수령하실 수 있습니다.', '배송'),
('교환/반품은 어떻게 하나요?', '마이페이지 > 주문내역에서 교환/반품 신청이 가능합니다. 상품 수령 후 7일 이내에 신청해주세요.', '교환/반품'),
('회원 탈퇴는 어떻게 하나요?', '마이페이지 > 회원정보수정 > 회원탈퇴 메뉴를 통해 가능합니다. 탈퇴 시 모든 개인정보는 삭제됩니다.', '회원정보'),
('적립금은 언제까지 사용 가능한가요?', '적립금은 지급일로부터 1년간 유효하며, 유효기간이 지나면 자동으로 소멸됩니다.', '적립금/쿠폰');

-- 이벤트 샘플 데이터
INSERT INTO events (title, description, start_date, end_date, is_active) VALUES
('여름 맞이 할인 이벤트', '모든 여름 상품 20% 할인', '2025-06-01', '2025-06-30', TRUE),
('신규 회원 특별 혜택', '신규 가입 시 5천원 적립금 지급', '2025-01-01', '2025-12-31', TRUE),
('추석 명절 이벤트', '추석 선물세트 특별 할인', '2025-09-01', '2025-09-15', FALSE);

-- 쿠폰 샘플 데이터
INSERT INTO coupons (code, name, discount_amount, discount_percentage, min_order_amount, max_discount_amount, start_date, end_date, is_active) VALUES
('WELCOME2025', '신규가입 환영 쿠폰', 5000.00, NULL, 10000.00, NULL, '2025-01-01', '2025-12-31', TRUE),
('SUMMER25', '여름 시즌 할인 쿠폰', NULL, 10.00, 30000.00, 10000.00, '2025-06-01', '2025-08-31', TRUE),
('SPECIAL50', 'VIP 전용 특별 할인', NULL, 50.00, 100000.00, 50000.00, '2025-05-01', '2025-05-31', FALSE);

-- 사용자 쿠폰 샘플
INSERT INTO user_coupons (user_id, coupon_id, is_used, issue_date, expire_date) 
SELECT u.id, c.id, FALSE, NOW(), c.end_date 
FROM users u, coupons c 
WHERE u.username = 'user1' AND c.code = 'WELCOME2025';

INSERT INTO user_coupons (user_id, coupon_id, is_used, issue_date, expire_date) 
SELECT u.id, c.id, FALSE, NOW(), c.end_date 
FROM users u, coupons c 
WHERE u.username = 'user1' AND c.code = 'SUMMER25';

-- 상품 리뷰 샘플
INSERT INTO product_reviews (product_id, user_id, rating, content, status) 
SELECT 
  p.id, 
  u.id, 
  5, 
  '정말 좋은 제품입니다! 배송도 빠르고 품질도 만족스럽습니다.', 
  'APPROVED' 
FROM products p, users u 
WHERE p.name = '최신 스마트폰' AND u.username = 'user2';

INSERT INTO product_reviews (product_id, user_id, rating, content, status) 
SELECT 
  p.id, 
  u.id, 
  4, 
  '가성비가 좋은 제품입니다. 디자인도 마음에 들고 사용감도 좋네요.', 
  'APPROVED' 
FROM products p, users u 
WHERE p.name = '게이밍 노트북' AND u.username = 'user1';

-- 배너 샘플
INSERT INTO banner (title, image_url, link_url, start_date, end_date, position, is_active) VALUES
('메인 슬라이드 배너 1', 'https://example.com/banners/main1.jpg', '/events/summer-sale', '2025-05-01', '2025-06-30', 'MAIN_SLIDER', TRUE),
('메인 슬라이드 배너 2', 'https://example.com/banners/main2.jpg', '/products/new-arrivals', '2025-05-01', '2025-06-30', 'MAIN_SLIDER', TRUE),
('팝업 배너', 'https://example.com/banners/popup1.jpg', '/events/special-offer', '2025-05-01', '2025-05-15', 'POPUP', TRUE); 