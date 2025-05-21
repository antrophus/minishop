/* 
 * V2__entity_improvements.sql
 * 이 마이그레이션 스크립트는 V1 이후 개선된 엔티티 구조와 관계를 적용합니다.
 * - 기존 테이블의 컬럼 추가/수정
 * - 가격 처리 관련 데이터 타입 변경 (INT → DECIMAL)
 * - 인덱스 개선
 * - 일부 누락된 제약조건 추가
 */

-- Product 테이블은 이미 변경되어 있으므로 생략합니다.

-- Payment 테이블 개선: 정확한 통화 처리를 위한 데이터 타입 변경
ALTER TABLE payments
  CHANGE COLUMN amount amount DECIMAL(12,2) NOT NULL,
  ADD COLUMN transaction_id VARCHAR(100) AFTER status,
  ADD COLUMN gateway_response TEXT AFTER transaction_id,
  ADD COLUMN payment_attempt_timestamp TIMESTAMP AFTER gateway_response,
  ADD COLUMN payment_completion_timestamp TIMESTAMP AFTER payment_attempt_timestamp,
  ADD COLUMN payment_failed_timestamp TIMESTAMP AFTER payment_completion_timestamp,
  ADD COLUMN payment_cancelled_timestamp TIMESTAMP AFTER payment_failed_timestamp,
  ADD COLUMN payment_metadata JSON AFTER payment_cancelled_timestamp,
  ADD COLUMN payment_method VARCHAR(50) AFTER payment_metadata;

-- Shipment 테이블 개선: 배송 추적 및 주소 정보 추가
ALTER TABLE shipments
  ADD COLUMN carrier VARCHAR(50) AFTER tracking_number,
  ADD COLUMN estimated_delivery_date DATE AFTER carrier,
  ADD COLUMN actual_delivery_date DATE AFTER estimated_delivery_date,
  ADD COLUMN recipient_name VARCHAR(100) AFTER actual_delivery_date,
  ADD COLUMN recipient_phone VARCHAR(20) AFTER recipient_name,
  ADD COLUMN address_line1 VARCHAR(255) AFTER recipient_phone,
  ADD COLUMN address_line2 VARCHAR(255) AFTER address_line1, 
  ADD COLUMN city VARCHAR(100) AFTER address_line2,
  ADD COLUMN state VARCHAR(100) AFTER city,
  ADD COLUMN postal_code VARCHAR(20) AFTER state,
  ADD COLUMN country VARCHAR(50) AFTER postal_code,
  ADD COLUMN delivery_instructions TEXT AFTER country;

-- Order 테이블 개선: 주문 상태 변경 이력 및 결제/배송 메타데이터 추가
ALTER TABLE orders
  ADD COLUMN order_number VARCHAR(50) AFTER id,
  ADD COLUMN total_amount DECIMAL(12,2) DEFAULT 0.0 AFTER order_date,
  ADD COLUMN payment_method VARCHAR(50) AFTER status,
  ADD COLUMN shipping_method VARCHAR(50) AFTER payment_method,
  ADD COLUMN shipping_cost DECIMAL(12,2) DEFAULT 0.0 AFTER shipping_method,
  ADD COLUMN tax_amount DECIMAL(12,2) DEFAULT 0.0 AFTER shipping_cost,
  ADD COLUMN discount_amount DECIMAL(12,2) DEFAULT 0.0 AFTER tax_amount,
  ADD COLUMN coupon_code VARCHAR(50) AFTER discount_amount,
  ADD COLUMN is_gift BOOLEAN DEFAULT FALSE AFTER coupon_code,
  ADD COLUMN gift_message TEXT AFTER is_gift,
  ADD COLUMN notes TEXT AFTER gift_message,
  ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER order_date;

-- 주문 상태 변경 이력 테이블 추가
CREATE TABLE order_status_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  previous_status VARCHAR(20),
  new_status VARCHAR(20) NOT NULL,
  changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  changed_by VARCHAR(100),
  reason TEXT,
  FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Cart 테이블 개선: 장바구니 상태 및 만료 관리
ALTER TABLE carts
  ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE' AFTER user_id,
  ADD COLUMN expires_at TIMESTAMP AFTER created_at,
  ADD COLUMN last_activity_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER expires_at,
  ADD COLUMN session_id VARCHAR(100) AFTER last_activity_date,
  ADD COLUMN ip_address VARCHAR(50) AFTER session_id,
  ADD COLUMN user_agent TEXT AFTER ip_address,
  ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- Cart Item 테이블 개선: 가격 및 선물 관련 필드 추가
ALTER TABLE cart_items
  ADD COLUMN unit_price DECIMAL(12,2) DEFAULT 0.0 AFTER quantity,
  ADD COLUMN applied_price_type VARCHAR(20) DEFAULT 'REGULAR' AFTER unit_price,
  ADD COLUMN is_gift BOOLEAN DEFAULT FALSE AFTER applied_price_type,
  ADD COLUMN gift_message TEXT AFTER is_gift,
  ADD COLUMN product_options JSON AFTER gift_message,
  ADD COLUMN selected_attributes JSON AFTER product_options,
  ADD COLUMN added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER selected_attributes,
  ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER added_at;

-- Saved For Later 테이블 개선: 알림 및 우선순위 관련 필드 추가
ALTER TABLE saved_for_later
  ADD COLUMN user_notes TEXT AFTER created_at,
  ADD COLUMN reminder_date DATE AFTER user_notes,
  ADD COLUMN reminder_sent BOOLEAN DEFAULT FALSE AFTER reminder_date,
  ADD COLUMN priority INT DEFAULT 0 AFTER reminder_sent,
  ADD COLUMN source VARCHAR(50) DEFAULT 'WISHLIST' AFTER priority;

-- Subscription Plan 테이블 개선: 가격 및 결제 주기 관련 필드 개선
ALTER TABLE subscription_plans
  CHANGE COLUMN price price DECIMAL(12,2) NOT NULL,
  ADD COLUMN billing_frequency VARCHAR(20) DEFAULT 'MONTHLY' AFTER duration,
  ADD COLUMN discount_percentage DECIMAL(5,2) DEFAULT 0.0 AFTER description,
  ADD COLUMN trial_period_days INT DEFAULT 0 AFTER discount_percentage,
  ADD COLUMN features JSON AFTER trial_period_days,
  ADD COLUMN max_pause_count INT DEFAULT 3 AFTER features,
  ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER max_pause_count,
  ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- Subscription Payment 테이블 개선: 가격 처리 및 청구 기간 관련 필드 개선
ALTER TABLE subscription_payments
  CHANGE COLUMN amount amount DECIMAL(12,2) NOT NULL,
  ADD COLUMN original_amount DECIMAL(12,2) AFTER amount,
  ADD COLUMN discount_amount DECIMAL(12,2) DEFAULT 0.0 AFTER original_amount,
  ADD COLUMN tax_amount DECIMAL(12,2) DEFAULT 0.0 AFTER discount_amount,
  ADD COLUMN transaction_id VARCHAR(100) AFTER status,
  ADD COLUMN payment_method VARCHAR(50) AFTER transaction_id,
  ADD COLUMN gateway_response TEXT AFTER payment_method,
  ADD COLUMN billing_period_start DATE AFTER gateway_response,
  ADD COLUMN billing_period_end DATE AFTER billing_period_start,
  ADD COLUMN invoice_id VARCHAR(100) AFTER billing_period_end,
  ADD COLUMN failure_reason TEXT AFTER invoice_id,
  ADD COLUMN retry_count INT DEFAULT 0 AFTER failure_reason,
  ADD COLUMN next_retry_date TIMESTAMP AFTER retry_count;

-- Subscription 테이블 개선: 구독 상태 및 갱신 관련 필드 개선
ALTER TABLE subscriptions
  ADD COLUMN contract_start_date DATE AFTER next_payment_date,
  ADD COLUMN contract_end_date DATE AFTER contract_start_date,
  ADD COLUMN trial_end_date DATE AFTER contract_end_date,
  ADD COLUMN payment_method VARCHAR(50) AFTER trial_end_date,
  ADD COLUMN customer_profile_id VARCHAR(100) AFTER payment_method,
  ADD COLUMN auto_renew BOOLEAN DEFAULT TRUE AFTER customer_profile_id,
  ADD COLUMN cancellation_date TIMESTAMP AFTER auto_renew,
  ADD COLUMN cancellation_reason TEXT AFTER cancellation_date,
  ADD COLUMN pause_start_date TIMESTAMP AFTER cancellation_reason,
  ADD COLUMN pause_end_date TIMESTAMP AFTER pause_start_date,
  ADD COLUMN pause_reason TEXT AFTER pause_end_date,
  ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- Recurring Order 테이블 추가
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

-- Recurring Order Item 테이블 추가
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

-- Order 테이블에 Recurring Order 참조 추가
ALTER TABLE orders
  ADD COLUMN recurring_order_id BIGINT AFTER delivery_id,
  ADD COLUMN is_recurring BOOLEAN DEFAULT FALSE AFTER recurring_order_id,
  ADD FOREIGN KEY (recurring_order_id) REFERENCES recurring_orders(id);

-- 인덱스 추가
CREATE INDEX idx_order_user_id ON orders(member_id);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_order_number ON orders(order_number);
CREATE INDEX idx_order_recurring_order_id ON orders(recurring_order_id);

CREATE INDEX idx_payment_order_id ON payments(order_id);
CREATE INDEX idx_payment_status ON payments(status);
CREATE INDEX idx_payment_transaction_id ON payments(transaction_id);

CREATE INDEX idx_shipment_order_id ON shipments(order_id);
CREATE INDEX idx_shipment_tracking_number ON shipments(tracking_number);
CREATE INDEX idx_shipment_status ON shipments(status);

CREATE INDEX idx_cart_user_id ON carts(user_id);
CREATE INDEX idx_cart_status ON carts(status);
CREATE INDEX idx_cart_expires_at ON carts(expires_at);

CREATE INDEX idx_cart_item_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_item_product_id ON cart_items(product_id);

CREATE INDEX idx_saved_for_later_user_id ON saved_for_later(user_id);
CREATE INDEX idx_saved_for_later_product_id ON saved_for_later(product_id);

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

CREATE INDEX idx_recurring_order_user_id ON recurring_orders(user_id);
CREATE INDEX idx_recurring_order_subscription_id ON recurring_orders(subscription_id);
CREATE INDEX idx_recurring_order_status ON recurring_orders(status);
CREATE INDEX idx_recurring_order_next_order_date ON recurring_orders(next_order_date);

CREATE INDEX idx_recurring_order_item_recurring_order_id ON recurring_order_items(recurring_order_id);
CREATE INDEX idx_recurring_order_item_product_id ON recurring_order_items(product_id);

CREATE INDEX idx_order_status_history_order_id ON order_status_history(order_id);
CREATE INDEX idx_order_status_history_status ON order_status_history(new_status); 