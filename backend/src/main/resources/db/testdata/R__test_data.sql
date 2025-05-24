/*
 * R__test_data.sql
 * 이 스크립트는 테스트 환경에서 사용할 테스트 데이터를 제공합니다.
 * R__ 접두사를 사용해 반복 실행 가능한 마이그레이션으로 설정합니다.
 * 따라서 테스트 실행 시마다 항상 같은 초기 데이터 상태를 보장합니다.
 */

-- 테스트 데이터 삽입 전 기존 데이터 삭제
DELETE FROM product_reviews;
DELETE FROM product_media;
DELETE FROM product_content;
DELETE FROM cart_items;
DELETE FROM carts;
DELETE FROM saved_for_later;
DELETE FROM subscription_payments;
DELETE FROM subscriptions;
DELETE FROM products;
DELETE FROM product_categories;
DELETE FROM user_profiles;
DELETE FROM user_roles;
DELETE FROM role_permissions;
DELETE FROM permissions;
DELETE FROM roles;
DELETE FROM users;

-- 역할 및 권한 생성
INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_ADMIN'), 
(2, 'ROLE_USER');

INSERT INTO permissions (id, name) VALUES
(1, 'READ_PRODUCTS'), 
(2, 'WRITE_PRODUCTS'),
(3, 'READ_ORDERS'), 
(4, 'MANAGE_ORDERS');

-- 역할-권한 매핑
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), -- 관리자: 모든 권한
(2, 1), (2, 3);                  -- 일반 사용자: 조회 권한만

-- 테스트 사용자 생성
INSERT INTO users (id, username, password, email, created_at) VALUES
(1, 'testadmin', '$2a$10$qBMveHBqri8AjZ3ydO5OQe7yoUPVKx1iMvG.ps74PXmCRbO5hDvAm', 'testadmin@test.com', NOW()),  -- 비밀번호: admin123
(2, 'testuser', '$2a$10$qBMveHBqri8AjZ3ydO5OQe7yoUPVKx1iMvG.ps74PXmCRbO5hDvAm', 'testuser@test.com', NOW());   -- 비밀번호: admin123

-- 사용자-역할 매핑
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- testadmin: ROLE_ADMIN
(2, 2); -- testuser: ROLE_USER

-- 사용자 프로필
INSERT INTO user_profiles (id, user_id, full_name, phone, gender, birth_date) VALUES
(1, 1, 'Test Admin', '010-1234-5678', 'MALE', '1990-01-01'),
(2, 2, 'Test User', '010-8765-4321', 'FEMALE', '1995-05-05');

-- 제품 카테고리
INSERT INTO product_categories (id, name, parent_id) VALUES
(1, '테스트 카테고리1', NULL),
(2, '테스트 카테고리2', NULL),
(3, '테스트 서브카테고리1', 1);

-- 제품 데이터
INSERT INTO products (id, name, gm_price, gbm_price, shop_price, cost_price, stock_quantity, status, is_active, category_id, description, created_at) VALUES
(1, '테스트 제품1', 10000.00, 12000.00, 9000.00, 5000.00, 100, 'AVAILABLE', TRUE, 1, '테스트 제품1 설명입니다.', NOW()),
(2, '테스트 제품2', 20000.00, 22000.00, 18000.00, 10000.00, 50, 'AVAILABLE', TRUE, 2, '테스트 제품2 설명입니다.', NOW()),
(3, '테스트 제품3', 15000.00, 17000.00, 13500.00, 9000.00, 75, 'AVAILABLE', TRUE, 3, '테스트 제품3 설명입니다.', NOW());

-- 제품 내용
INSERT INTO product_content (id, product_id, content, version, status, created_at) VALUES
(1, 1, '<h1>테스트 제품1</h1><p>테스트 제품1의 상세 내용입니다.</p>', 1, 'PUBLISHED', NOW()),
(2, 2, '<h1>테스트 제품2</h1><p>테스트 제품2의 상세 내용입니다.</p>', 1, 'PUBLISHED', NOW());

-- 제품 미디어
INSERT INTO product_media (id, product_id, media_url, media_type, display_order, created_at) VALUES
(1, 1, 'https://example.com/test1.jpg', 'IMAGE', 1, NOW()),
(2, 1, 'https://example.com/test2.jpg', 'IMAGE', 2, NOW()),
(3, 2, 'https://example.com/test3.jpg', 'IMAGE', 1, NOW());

-- 장바구니
INSERT INTO carts (id, user_id, status, created_at, expires_at) VALUES
(1, 2, 'ACTIVE', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY));

-- 장바구니 아이템
INSERT INTO cart_items (id, cart_id, product_id, quantity, unit_price, applied_price_type) VALUES
(1, 1, 1, 2, 10000.00, 'REGULAR'),
(2, 1, 3, 1, 13500.00, 'MEMBER');

-- 제품 리뷰
INSERT INTO product_reviews (id, product_id, user_id, rating, content, status, created_at) VALUES
(1, 1, 2, 5, '테스트 리뷰입니다. 아주 좋아요!', 'APPROVED', NOW()); 