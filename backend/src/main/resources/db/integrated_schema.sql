/*
 * Gemma Korea E-commerce Platform
 * 통합 데이터베이스 스키마
 * 
 * 이 스크립트는 Gemma Korea 화장품 전자상거래 웹사이트의 데이터베이스 스키마를 정의합니다.
 * MVP 개발 단계에서는 이 단일 파일로 스키마를 관리하고, 이후 필요에 따라 마이그레이션 스크립트로 분리할 수 있습니다.
 */


DROP DATABASE IF EXISTS mylittleshop;
create database mylittleshop;
use mylittleshop;
/* 사용자 관련 테이블 */

/* 사용자 테이블 */
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    is_gemma_member BOOLEAN NOT NULL DEFAULT FALSE,
    name VARCHAR(100),
    gender VARCHAR(10),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    account_expire_date DATE,
    last_login_at DATETIME,
    failed_login_attempts INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

/* 비밀번호 재설정 토큰 테이블 */
CREATE TABLE password_reset_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token VARCHAR(128) NOT NULL UNIQUE,
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    used_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_token (token),
    INDEX idx_user_id (user_id)
);

/* 역할 테이블 */
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

/* 권한 테이블 */
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

/* 사용자-역할 매핑 */
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

/* 역할-권한 매핑 */
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

/* 사용자 프로필 */
CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    gender VARCHAR(10),
    birth_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 제품 관련 테이블 */

/* 제품 카테고리 테이블 */
CREATE TABLE product_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES product_categories(id)
);

/* 제품 테이블 */
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    gm_price DECIMAL(12,2) NOT NULL,           /* 소비자가 (General Market Price) */
    gbm_price DECIMAL(12,2) NOT NULL,          /* 회원가 (Gemma Member Price) */
    shop_price DECIMAL(12,2) NOT NULL,         /* 총판가 (Shop Price) */
    cost_price DECIMAL(12,2),                  /* 원가 (Cost Price) - 내부 계산용 */
    original_gm_price DECIMAL(12,2),           /* 원래 소비자가 (Original GM Price) - 할인 전 가격 */
    stock_quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_active BOOLEAN DEFAULT TRUE,
    category_id BIGINT,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    bestseller BOOLEAN NOT NULL DEFAULT FALSE,
    new_arrival BOOLEAN NOT NULL DEFAULT FALSE,
    sku VARCHAR(50) UNIQUE,
    barcode VARCHAR(50),
    brand VARCHAR(100),
    manufacturer VARCHAR(100),
    minimum_order_quantity INT NOT NULL DEFAULT 1,
    maximum_order_quantity INT NOT NULL DEFAULT 0,
    subscription_available BOOLEAN NOT NULL DEFAULT FALSE,
    recommended_subscription_period INT,
    shipping_fee DECIMAL(12,2) DEFAULT 0.00,
    free_shipping_threshold DECIMAL(12,2),
    country_of_origin VARCHAR(100),
    weight DECIMAL(10,2),
    original_weight DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES product_categories(id)
);

/* 재고 테이블 */
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    available_stock INT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 재고 이력 테이블 */
CREATE TABLE inventory_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    change_type VARCHAR(30) NOT NULL, -- IN, OUT, ADJUST
    quantity INT NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    note VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 제품 속성 테이블 */
CREATE TABLE product_attributes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* 제품 속성값 테이블 */
CREATE TABLE product_attribute_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    value VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (attribute_id) REFERENCES product_attributes(id)
);

/* 제품 컨텐츠 테이블 */
CREATE TABLE product_content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    version INT DEFAULT 1,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 제품 미디어 테이블 */
CREATE TABLE product_media (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    media_url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(255),
    media_type VARCHAR(20) NOT NULL, -- IMAGE, VIDEO, DOCUMENT, MODEL_3D, AUDIO
    alt_text VARCHAR(255),
    title VARCHAR(255),
    display_order INT DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    is_ai_generated BOOLEAN DEFAULT FALSE,
    ai_prompt_used VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    dimensions VARCHAR(20),
    file_size BIGINT,
    file_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_product_media_product (product_id),
    INDEX idx_product_media_type (media_type),
    INDEX idx_product_media_featured (is_featured)
);

/* 제품 이미지 테이블 */
CREATE TABLE product_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    is_main BOOLEAN DEFAULT FALSE,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 제품 평점 테이블 */
CREATE TABLE product_ratings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL UNIQUE,
    average_rating DECIMAL(3,2) NOT NULL DEFAULT 0.0,
    rating_count INT NOT NULL DEFAULT 0,
    one_star_count INT NOT NULL DEFAULT 0,
    two_star_count INT NOT NULL DEFAULT 0,
    three_star_count INT NOT NULL DEFAULT 0,
    four_star_count INT NOT NULL DEFAULT 0,
    five_star_count INT NOT NULL DEFAULT 0,
    verified_purchase_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_rating_product (product_id),
    INDEX idx_rating_average (average_rating)
);

/* 제품 리뷰 테이블 */
CREATE TABLE product_reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    rating INT NOT NULL,
    content TEXT,
    status VARCHAR(20),
    helpful_votes INT NOT NULL DEFAULT 0,
    unhelpful_votes INT NOT NULL DEFAULT 0,
    verified_purchase BOOLEAN NOT NULL DEFAULT FALSE,
    moderation_notes VARCHAR(500),
    moderated_by VARCHAR(100),
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 리뷰 응답 테이블 */
CREATE TABLE review_responses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    review_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_seller_response BOOLEAN NOT NULL DEFAULT FALSE,
    is_admin_response BOOLEAN NOT NULL DEFAULT FALSE,
    helpful_votes INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES product_reviews(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_response_review (review_id),
    INDEX idx_response_user (user_id)
);

/* 제품 Q&A 테이블 */
CREATE TABLE product_questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 프로모션 관련 테이블 */

/* 프로모션 테이블 */
CREATE TABLE promotions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    discount_rate DECIMAL(5,2),
    start_date DATETIME,
    end_date DATETIME,
    description VARCHAR(255),
    code VARCHAR(32) UNIQUE,
    type VARCHAR(30),
    discount_type VARCHAR(30),
    discount_value DECIMAL(15,2),
    minimum_order_amount DECIMAL(15,2),
    max_usage_count INT,
    current_usage_count INT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

/* 제품-프로모션 매핑 테이블 */
CREATE TABLE product_promotion (
    product_id BIGINT NOT NULL,
    promotion_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, promotion_id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);

/* 쿠폰 테이블 */
CREATE TABLE coupons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    discount_type VARCHAR(20) NOT NULL, -- PERCENT, FIXED
    discount_value DECIMAL(10,2) NOT NULL,
    min_order_amount INT,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* 사용자 쿠폰 테이블 */
CREATE TABLE user_coupons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    used_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(id)
);

/* 포인트 테이블 */
CREATE TABLE points (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    total_points INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 포인트 거래 이력 테이블 */
CREATE TABLE point_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    points_id BIGINT,
    change_type VARCHAR(30) NOT NULL, -- EARN, USE, EXPIRE
    amount INT NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (points_id) REFERENCES points(id)
);

/* 장바구니 관련 테이블 */

/* 장바구니 테이블 */
CREATE TABLE carts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    last_activity_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_id VARCHAR(100),
    ip_address VARCHAR(50),
    user_agent TEXT,
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 장바구니 아이템 테이블 */
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) DEFAULT 0.0,
    applied_price_type VARCHAR(20) DEFAULT 'REGULAR',
    is_gift BOOLEAN DEFAULT FALSE,
    gift_message TEXT,
    product_options JSON,
    selected_attributes JSON,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 위시리스트 테이블 */
CREATE TABLE wishlists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 나중에 구매하기 테이블 */
CREATE TABLE saved_for_later (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_notes TEXT,
    reminder_date DATETIME,
    reminder_sent BOOLEAN DEFAULT FALSE,
    priority VARCHAR(20) DEFAULT '0',
    source VARCHAR(50) DEFAULT 'WISHLIST',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 주문 및 배송 관련 테이블 */

/* 배송 테이블 */
CREATE TABLE deliveries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipient_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    status VARCHAR(20)
);

/* 주문 테이블 */
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_amount DECIMAL(12,2) DEFAULT 0.00,
    final_amount DECIMAL(12,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    shipping_method VARCHAR(50),
    shipping_fee DECIMAL(12,2) DEFAULT 0.00,
    tax_amount DECIMAL(12,2) DEFAULT 0.00,
    discount_amount DECIMAL(12,2) DEFAULT 0.00,
    coupon_code VARCHAR(50),
    is_gift BOOLEAN DEFAULT FALSE,
    gift_message TEXT,
    order_notes TEXT,
    transaction_id VARCHAR(100),
    estimated_delivery_date DATETIME,
    is_recurring BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    delivery_id BIGINT,
    recurring_order_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (delivery_id) REFERENCES deliveries(id)
);

/* 주문 상태 변경 이력 테이블 */
CREATE TABLE order_status_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    previous_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100),
    change_reason TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

/* 주문 아이템 테이블 */
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 결제 테이블 */
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    transaction_id VARCHAR(100),
    gateway_response TEXT,
    payment_attempt_timestamp TIMESTAMP,
    payment_completion_timestamp TIMESTAMP,
    payment_failed_timestamp TIMESTAMP,
    payment_cancelled_timestamp TIMESTAMP,
    payment_metadata JSON,
    payment_method VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

/* 배송지 주소 테이블 */
CREATE TABLE delivery_addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 배송 테이블 */
CREATE TABLE shipments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    shipped_at TIMESTAMP,
    status VARCHAR(20),
    tracking_number VARCHAR(100),
    carrier VARCHAR(50),
    estimated_delivery_date DATE,
    actual_delivery_date DATE,
    recipient_name VARCHAR(100),
    recipient_phone VARCHAR(20),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255), 
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(50),
    delivery_instructions TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

/* 구독 관련 테이블 */

/* 구독 플랜 테이블 */
CREATE TABLE subscription_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    duration INT NOT NULL,
    duration_months INT,
    billing_frequency VARCHAR(20) DEFAULT 'MONTHLY',
    description VARCHAR(255),
    discount_percentage DECIMAL(5,2) DEFAULT 0.0,
    trial_period_days INT DEFAULT 0,
    features JSON,
    max_pause_count INT DEFAULT 3,
    includes_recurring_order BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

/* 구독 테이블 */
CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    frequency VARCHAR(30) NOT NULL,
    contract_start_date DATE,
    contract_end_date DATE,
    next_payment_date TIMESTAMP NOT NULL,
    next_billing_date DATE,
    next_delivery_date TIMESTAMP,
    trial_end_date DATE,
    payment_method VARCHAR(50),
    payment_method_id BIGINT,
    customer_profile_id VARCHAR(100),
    auto_renew BOOLEAN DEFAULT TRUE,
    cancellation_date TIMESTAMP,
    cancellation_reason TEXT,
    pause_start_date TIMESTAMP,
    pause_end_date TIMESTAMP,
    pause_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (plan_id) REFERENCES subscription_plans(id)
);

/* 구독 결제 테이블 */
CREATE TABLE subscription_payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    original_amount DECIMAL(12,2),
    discount_amount DECIMAL(12,2) DEFAULT 0.0,
    tax_amount DECIMAL(12,2) DEFAULT 0.0,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    transaction_id VARCHAR(100),
    payment_method VARCHAR(50),
    gateway_response TEXT,
    billing_period_start DATE,
    billing_period_end DATE,
    invoice_id VARCHAR(100),
    failure_reason TEXT,
    retry_count INT DEFAULT 0,
    next_retry_date TIMESTAMP,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

/* 정기 주문 테이블 */
CREATE TABLE recurring_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    frequency VARCHAR(20) NOT NULL,
    next_order_date DATE NOT NULL,
    last_order_date DATE,
    shipping_address_id BIGINT,
    payment_method_id BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

/* 정기 주문 아이템 테이블 */
CREATE TABLE recurring_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recurring_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(12,2) NOT NULL,
    applied_price_type VARCHAR(20) DEFAULT 'REGULAR',
    is_gift BOOLEAN DEFAULT FALSE,
    gift_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recurring_order_id) REFERENCES recurring_orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

/* 시스템 및 컨텐츠 관련 테이블 */

/* 감사 로그 테이블 */
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity VARCHAR(100),
    entity_id BIGINT,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 알림 테이블 */
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

/* 이벤트 테이블 */
CREATE TABLE events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* 배너 테이블 */
CREATE TABLE banner (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    image_url VARCHAR(500) NOT NULL,
    link_url VARCHAR(500),
    position VARCHAR(50),
    sort_order INT DEFAULT 0,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* FAQ 테이블 */
CREATE TABLE faq (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question VARCHAR(255) NOT NULL,
    answer TEXT NOT NULL,
    category VARCHAR(50),
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* 이용약관 테이블 */
CREATE TABLE terms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    version VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* 이메일 인증 토큰 테이블 */
CREATE TABLE email_verification_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token VARCHAR(128) NOT NULL UNIQUE,
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    used_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_email_token (token),
    INDEX idx_email_user_id (user_id)
);

/* 인덱스 생성 */

/* 주문 관련 인덱스 */
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_recurring_order_id ON orders(recurring_order_id);

/* 결제 관련 인덱스 */
CREATE INDEX idx_payment_order_id ON payments(order_id);
CREATE INDEX idx_payment_status ON payments(status);
CREATE INDEX idx_payment_transaction_id ON payments(transaction_id);

/* 배송 관련 인덱스 */
CREATE INDEX idx_shipment_order_id ON shipments(order_id);
CREATE INDEX idx_shipment_tracking_number ON shipments(tracking_number);
CREATE INDEX idx_shipment_status ON shipments(status);

/* 장바구니 관련 인덱스 */
CREATE INDEX idx_cart_user_id ON carts(user_id);
CREATE INDEX idx_cart_status ON carts(status);
CREATE INDEX idx_cart_expires_at ON carts(expires_at);

CREATE INDEX idx_cart_item_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_item_product_id ON cart_items(product_id);

/* 선호 제품 관련 인덱스 */
CREATE INDEX idx_saved_for_later_user_id ON saved_for_later(user_id);
CREATE INDEX idx_saved_for_later_product_id ON saved_for_later(product_id);

/* 구독 관련 인덱스 */
CREATE INDEX idx_subscription_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscription_product_id ON subscriptions(product_id);
CREATE INDEX idx_subscription_plan_id ON subscriptions(plan_id);
CREATE INDEX idx_subscription_status ON subscriptions(status);
CREATE INDEX idx_subscription_next_payment_date ON subscriptions(next_payment_date);
CREATE INDEX idx_subscription_contract_end_date ON subscriptions(contract_end_date);

CREATE INDEX idx_subscription_plan_is_active ON subscription_plans(is_active);

CREATE INDEX idx_subscription_payment_subscription_id ON subscription_payments(subscription_id);
CREATE INDEX idx_subscription_payment_status ON subscription_payments(status);
CREATE INDEX idx_subscription_payment_transaction_id ON subscription_payments(transaction_id);

/* 정기 주문 관련 인덱스 */
CREATE INDEX idx_recurring_order_user_id ON recurring_orders(user_id);
CREATE INDEX idx_recurring_order_subscription_id ON recurring_orders(subscription_id);
CREATE INDEX idx_recurring_order_status ON recurring_orders(status);
CREATE INDEX idx_recurring_order_next_order_date ON recurring_orders(next_order_date);

CREATE INDEX idx_recurring_order_item_recurring_order_id ON recurring_order_items(recurring_order_id);
CREATE INDEX idx_recurring_order_item_product_id ON recurring_order_items(product_id);

/* 주문 상태 이력 관련 인덱스 */
CREATE INDEX idx_order_status_history_order_id ON order_status_history(order_id);
CREATE INDEX idx_order_status_history_status ON order_status_history(new_status);

/* 제품 관련 인덱스 */
CREATE INDEX idx_product_category_id ON products(category_id);
CREATE INDEX idx_product_status ON products(status);
CREATE INDEX idx_product_is_active ON products(is_active);
CREATE INDEX idx_product_sku ON products(sku);
CREATE INDEX idx_product_name ON products(name);

/* 사용자 관련 인덱스 */
CREATE INDEX idx_user_profile_user_id ON user_profiles(user_id);
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_user_email ON users(email);

/* Promotion 엔티티의 productIds, categoryIds, userIds용 테이블 생성 */
CREATE TABLE promotion_product_ids (
    promotion_id BIGINT NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (promotion_id, product_id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);

CREATE TABLE promotion_category_ids (
    promotion_id BIGINT NOT NULL,
    category_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (promotion_id, category_id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);

CREATE TABLE promotion_user_ids (
    promotion_id BIGINT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (promotion_id, user_id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);