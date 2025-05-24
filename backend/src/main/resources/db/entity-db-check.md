# 엔티티-데이터베이스 스키마 일치 검사 체크리스트

## 개요
- 검사 일시: 2025년 5월 20일
- 검사자: 데이터관리 담당자
- 검사 방법: 각 엔티티 클래스와 데이터베이스 스키마 간의 직접 대조

## 체크 항목
각 엔티티에 대해 다음 항목을 검증합니다:
- [ ] 테이블 이름 일치 확인 (@Table 어노테이션과 CREATE TABLE 문)
- [ ] 모든 필드에 해당하는 컬럼 존재 확인
- [ ] 컬럼 타입 적절성 확인
- [ ] 제약조건 일치 확인 (PK, FK, Unique, Not Null 등)
- [ ] 인덱스 정의 일치 확인

## 그룹별 엔티티 검사 목록

### 그룹 1
- [x] AuditLog - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] Banner - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] BillingFrequency - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Cart - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] CartItem - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)

### 그룹 2
- [x] CartStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] ContentStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Coupon - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] Delivery - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] DeliveryAddress - 일치 (테이블명, 컬럼명, 제약조건 일치)

### 그룹 3
- [x] DeliveryStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Event - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] FAQ - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] Inventory - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] InventoryHistory - 일치 (테이블명, 컬럼명, 제약조건 일치)
  - 작은 차이: 엔티티에서는 changedAt 필드에 @CreationTimestamp가 없고 명시적으로 설정하도록 되어 있지만, 스키마에서는 DEFAULT CURRENT_TIMESTAMP로 설정됨 (기능적으로 동일)

### 그룹 4
- [x] Notification - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] Order - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] OrderItem - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] OrderStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] OrderStatusHistory - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)

### 그룹 5
- [x] Payment - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] PaymentStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Permission - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] Points - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] PointTransaction - 일치 (테이블명, 컬럼명, 제약조건 일치)

### 그룹 6
- [x] Product - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] ProductAttribute - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] ProductAttributeValue - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] ProductCategory - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] ProductContent - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)

### 그룹 7
- [x] ProductImage - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] ProductMedia - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] ProductQuestion - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] ProductRating - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] ProductReview - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)

### 그룹 8
- [x] ProductStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Promotion - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] RecurringOrder - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] RecurringOrderItem - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] ReviewResponse - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)

### 그룹 9
- [x] ReviewStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Role - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] SavedForLater - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] Shipment - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] ShipmentStatus - 일치 (Enum 클래스로 별도 테이블 없음)

### 그룹 10
- [x] Subscription - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] SubscriptionPayment - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] SubscriptionPlan - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] SubscriptionStatus - 일치 (Enum 클래스로 별도 테이블 없음)
- [x] Terms - 일치 (테이블명, 컬럼명, 제약조건 일치)

### 그룹 11
- [x] User - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] UserCoupon - 일치 (테이블명, 컬럼명, 제약조건 일치)
- [x] UserProfile - 일치 (테이블명, 컬럼명, 제약조건, 인덱스 일치)
- [x] Wishlist - 일치 (테이블명, 컬럼명, 제약조건 일치)

## 불일치 발견 및 해결 기록
각 불일치 발견 시 아래 형식으로 기록합니다:
```
- 엔티티: [엔티티 이름]
- 문제: [불일치 설명]
- 해결 방법: [스키마 또는 엔티티 수정 사항]
- 상태: [해결 완료/진행 중]
```
