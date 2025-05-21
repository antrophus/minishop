/*
 * V2__rollback.sql
 * 이 스크립트는 V2__entity_improvements.sql의 변경사항을 롤백하기 위한 것입니다.
 * 롤백이 필요한 경우 수동으로 실행해야 합니다.
 * (Flyway는 자동 롤백을 지원하지 않음)
 *
 * 실행 방법:
 * 1. 마지막 마이그레이션을 제거: DELETE FROM flyway_schema_history WHERE version = '2';
 * 2. 이 롤백 스크립트 실행
 * 3. 필요한 경우 repair 명령 실행: flyway repair
 */

-- 인덱스 제거
DROP INDEX IF EXISTS idx_recurring_order_item_product_id;
DROP INDEX IF EXISTS idx_recurring_order_item_recurring_order_id;
DROP INDEX IF EXISTS idx_recurring_order_next_order_date;
DROP INDEX IF EXISTS idx_recurring_order_status;
DROP INDEX IF EXISTS idx_recurring_order_subscription_id;
DROP INDEX IF EXISTS idx_recurring_order_user_id;
DROP INDEX IF EXISTS idx_subscription_payment_transaction_id;
DROP INDEX IF EXISTS idx_subscription_payment_status;
DROP INDEX IF EXISTS idx_subscription_payment_subscription_id;
DROP INDEX IF EXISTS idx_subscription_plan_is_active;
DROP INDEX IF EXISTS idx_subscription_contract_end_date;
DROP INDEX IF EXISTS idx_subscription_next_payment_date;
DROP INDEX IF EXISTS idx_subscription_status;
DROP INDEX IF EXISTS idx_subscription_plan_id;
DROP INDEX IF EXISTS idx_subscription_product_id;
DROP INDEX IF EXISTS idx_subscription_user_id;
DROP INDEX IF EXISTS idx_saved_for_later_product_id;
DROP INDEX IF EXISTS idx_saved_for_later_user_id;
DROP INDEX IF EXISTS idx_cart_item_product_id;
DROP INDEX IF EXISTS idx_cart_item_cart_id;
DROP INDEX IF EXISTS idx_cart_expires_at;
DROP INDEX IF EXISTS idx_cart_status;
DROP INDEX IF EXISTS idx_cart_user_id;
DROP INDEX IF EXISTS idx_shipment_status;
DROP INDEX IF EXISTS idx_shipment_tracking_number;
DROP INDEX IF EXISTS idx_shipment_order_id;
DROP INDEX IF EXISTS idx_payment_transaction_id;
DROP INDEX IF EXISTS idx_payment_status;
DROP INDEX IF EXISTS idx_payment_order_id;
DROP INDEX IF EXISTS idx_order_recurring_order_id;
DROP INDEX IF EXISTS idx_order_order_number;
DROP INDEX IF EXISTS idx_order_status;
DROP INDEX IF EXISTS idx_order_user_id;
DROP INDEX IF EXISTS idx_order_status_history_status;
DROP INDEX IF EXISTS idx_order_status_history_order_id;

-- 테이블 제거
DROP TABLE IF EXISTS recurring_order_items;
DROP TABLE IF EXISTS recurring_orders;
DROP TABLE IF EXISTS order_status_history;

-- 변경된 외래 키 제약 조건 삭제
ALTER TABLE orders DROP FOREIGN KEY orders_ibfk_3;
ALTER TABLE orders DROP COLUMN recurring_order_id;
ALTER TABLE orders DROP COLUMN is_recurring;

-- 수정된 필드 복원 (Cart)
ALTER TABLE carts DROP COLUMN status;
ALTER TABLE carts DROP COLUMN expires_at;
ALTER TABLE carts DROP COLUMN last_activity_date;
ALTER TABLE carts DROP COLUMN session_id;
ALTER TABLE carts DROP COLUMN ip_address;
ALTER TABLE carts DROP COLUMN user_agent;
ALTER TABLE carts DROP COLUMN updated_at;

-- 수정된 필드 복원 (Cart Item)
ALTER TABLE cart_items DROP COLUMN unit_price;
ALTER TABLE cart_items DROP COLUMN applied_price_type;
ALTER TABLE cart_items DROP COLUMN is_gift;
ALTER TABLE cart_items DROP COLUMN gift_message;
ALTER TABLE cart_items DROP COLUMN product_options;
ALTER TABLE cart_items DROP COLUMN selected_attributes;
ALTER TABLE cart_items DROP COLUMN added_at;
ALTER TABLE cart_items DROP COLUMN updated_at;

-- 수정된 필드 복원 (Saved For Later)
ALTER TABLE saved_for_later DROP COLUMN user_notes;
ALTER TABLE saved_for_later DROP COLUMN reminder_date;
ALTER TABLE saved_for_later DROP COLUMN reminder_sent;
ALTER TABLE saved_for_later DROP COLUMN priority;
ALTER TABLE saved_for_later DROP COLUMN source;

-- 수정된 필드 복원 (Subscription Plan)
ALTER TABLE subscription_plans CHANGE COLUMN price price INT NOT NULL;
ALTER TABLE subscription_plans DROP COLUMN billing_frequency;
ALTER TABLE subscription_plans DROP COLUMN discount_percentage;
ALTER TABLE subscription_plans DROP COLUMN trial_period_days;
ALTER TABLE subscription_plans DROP COLUMN features;
ALTER TABLE subscription_plans DROP COLUMN max_pause_count;
ALTER TABLE subscription_plans DROP COLUMN created_at;
ALTER TABLE subscription_plans DROP COLUMN updated_at;

-- 수정된 필드 복원 (Subscription Payment)
ALTER TABLE subscription_payments CHANGE COLUMN amount amount INT NOT NULL;
ALTER TABLE subscription_payments DROP COLUMN original_amount;
ALTER TABLE subscription_payments DROP COLUMN discount_amount;
ALTER TABLE subscription_payments DROP COLUMN tax_amount;
ALTER TABLE subscription_payments DROP COLUMN transaction_id;
ALTER TABLE subscription_payments DROP COLUMN payment_method;
ALTER TABLE subscription_payments DROP COLUMN gateway_response;
ALTER TABLE subscription_payments DROP COLUMN billing_period_start;
ALTER TABLE subscription_payments DROP COLUMN billing_period_end;
ALTER TABLE subscription_payments DROP COLUMN invoice_id;
ALTER TABLE subscription_payments DROP COLUMN failure_reason;
ALTER TABLE subscription_payments DROP COLUMN retry_count;
ALTER TABLE subscription_payments DROP COLUMN next_retry_date;

-- 수정된 필드 복원 (Subscription)
ALTER TABLE subscriptions DROP COLUMN contract_start_date;
ALTER TABLE subscriptions DROP COLUMN contract_end_date;
ALTER TABLE subscriptions DROP COLUMN trial_end_date;
ALTER TABLE subscriptions DROP COLUMN payment_method;
ALTER TABLE subscriptions DROP COLUMN customer_profile_id;
ALTER TABLE subscriptions DROP COLUMN auto_renew;
ALTER TABLE subscriptions DROP COLUMN cancellation_date;
ALTER TABLE subscriptions DROP COLUMN cancellation_reason;
ALTER TABLE subscriptions DROP COLUMN pause_start_date;
ALTER TABLE subscriptions DROP COLUMN pause_end_date;
ALTER TABLE subscriptions DROP COLUMN pause_reason;
ALTER TABLE subscriptions DROP COLUMN updated_at;

-- 수정된 필드 복원 (Shipment)
ALTER TABLE shipments DROP COLUMN carrier;
ALTER TABLE shipments DROP COLUMN estimated_delivery_date;
ALTER TABLE shipments DROP COLUMN actual_delivery_date;
ALTER TABLE shipments DROP COLUMN recipient_name;
ALTER TABLE shipments DROP COLUMN recipient_phone;
ALTER TABLE shipments DROP COLUMN address_line1;
ALTER TABLE shipments DROP COLUMN address_line2;
ALTER TABLE shipments DROP COLUMN city;
ALTER TABLE shipments DROP COLUMN state;
ALTER TABLE shipments DROP COLUMN postal_code;
ALTER TABLE shipments DROP COLUMN country;
ALTER TABLE shipments DROP COLUMN delivery_instructions;

-- 수정된 필드 복원 (Payment)
ALTER TABLE payments CHANGE COLUMN amount amount INT NOT NULL;
ALTER TABLE payments DROP COLUMN transaction_id;
ALTER TABLE payments DROP COLUMN gateway_response;
ALTER TABLE payments DROP COLUMN payment_attempt_timestamp;
ALTER TABLE payments DROP COLUMN payment_completion_timestamp;
ALTER TABLE payments DROP COLUMN payment_failed_timestamp;
ALTER TABLE payments DROP COLUMN payment_cancelled_timestamp;
ALTER TABLE payments DROP COLUMN payment_metadata;
ALTER TABLE payments DROP COLUMN payment_method;

-- 수정된 필드 복원 (Order)
ALTER TABLE orders DROP COLUMN order_number;
ALTER TABLE orders DROP COLUMN total_amount;
ALTER TABLE orders DROP COLUMN payment_method;
ALTER TABLE orders DROP COLUMN shipping_method;
ALTER TABLE orders DROP COLUMN shipping_cost;
ALTER TABLE orders DROP COLUMN tax_amount;
ALTER TABLE orders DROP COLUMN discount_amount;
ALTER TABLE orders DROP COLUMN coupon_code;
ALTER TABLE orders DROP COLUMN is_gift;
ALTER TABLE orders DROP COLUMN gift_message;
ALTER TABLE orders DROP COLUMN notes;
ALTER TABLE orders DROP COLUMN updated_at;

-- 수정된 필드 복원 (Product)
ALTER TABLE products DROP COLUMN consumer_price;
ALTER TABLE products DROP COLUMN member_price;
ALTER TABLE products DROP COLUMN description;
ALTER TABLE products DROP COLUMN is_active;
ALTER TABLE products DROP COLUMN updated_at;
ALTER TABLE products CHANGE COLUMN regular_price price INT NOT NULL; 