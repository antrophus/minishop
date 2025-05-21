# 엔티티-서비스/리포지터리 점검 및 수정 작업 일지

## [업데이트 로그]
- 2024-05-21: 그룹 1(AuditLog, Banner) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 2(Coupon) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 3(Event, FAQ, InventoryHistory) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 4(Notification) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 5(Points, PointTransaction) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 7(ProductQuestion, ProductRating) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 8(ReviewResponse) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)
- 2024-05-21: 그룹 11(ProductRating) 서비스/리포지터리 생성 완료. 미해결 항목 [상태]를 '완료'로 변경, [조치]를 '서비스/리포지터리 생성 완료'로 업데이트. (by AI assistant)

## 개요
- 점검 일시: 2025년 5월 20일
- 점검자: 데이터관리 담당자
- 목적: 엔티티별 서비스/리포지터리 존재 및 연결 상태 점검, 미존재 시 생성 필요 기록

---

## 그룹별 점검 내역

### 그룹 1
- **AuditLog**
  - [문제] AuditLogService, AuditLogRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **Banner**
  - [문제] BannerService, BannerRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **BillingFrequency**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [상태] 완료

- **Cart**
  - [정상] CartService/CartRepository 정상 연결
  - [점검] CartService, CartRepository에서 Cart import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **CartItem**
  - [정상] CartItemService/CartItemRepository 정상 연결
  - [점검] CartItemService, CartItemRepository에서 CartItem import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

### 그룹 2
- **CartStatus**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [점검] CartRepository에서 CartStatus import 및 사용 정상. 오타, 잘못된 import, 불필요 import, 미사용 Enum 없음.
  - [상태] 완료

- **ContentStatus**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [점검] ContentStatus import/사용처 없음. 문제 없음.
  - [상태] 완료

- **Coupon**
  - [문제] CouponService, CouponRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **Delivery**
  - [정상] DeliveryService/DeliveryRepository 정상 연결
  - [점검] DeliveryService, DeliveryRepository에서 Delivery import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **DeliveryAddress**
  - [정상] DeliveryAddressService/DeliveryAddressRepository 정상 연결
  - [점검] DeliveryAddressService, DeliveryAddressRepository에서 DeliveryAddress import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

### 그룹 3
- **DeliveryStatus**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [점검] DeliveryStatus import/사용처 없음. 문제 없음.
  - [상태] 완료

- **Event**
  - [문제] EventService, EventRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **FAQ**
  - [문제] FAQService, FAQRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **Inventory**
  - [정상] InventoryService/InventoryRepository 정상 연결
  - [점검] InventoryService, InventoryRepository에서 Inventory import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **InventoryHistory**
  - [문제] InventoryHistoryService, InventoryHistoryRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

### 그룹 4
- **Notification**
  - [문제] NotificationService, NotificationRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **Order**
  - [정상] OrderService/OrderRepository 정상 연결
  - [점검] OrderRepository에서 Order, OrderStatus import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티/Enum 없음.
  - [상태] 완료

- **OrderItem**
  - [정상] OrderItemService/OrderItemRepository 정상 연결
  - [점검] OrderItemService, OrderItemRepository에서 OrderItem import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **OrderStatus**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [점검] OrderRepository, OrderStatusHistoryService, OrderStatusHistoryRepository에서 OrderStatus import 및 사용 정상. 오타, 잘못된 import, 불필요 import, 미사용 Enum 없음.
  - [상태] 완료

- **OrderStatusHistory**
  - [정상] OrderStatusHistoryService/OrderStatusHistoryRepository 정상 연결
  - [점검] OrderStatusHistoryService, OrderStatusHistoryRepository에서 OrderStatusHistory import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

### 그룹 5
- **Payment**
  - [정상] PaymentService/PaymentRepository 정상 연결
  - [점검] PaymentService, PaymentRepository에서 Payment import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **PaymentStatus**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [점검] PaymentStatus import/사용처 없음. 문제 없음.
  - [상태] 완료

- **Permission**
  - [정상] PermissionService/PermissionRepository 정상 연결
  - [점검] PermissionService, PermissionRepository에서 Permission import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **Points**
  - [문제] PointsService, PointsRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **PointTransaction**
  - [문제] PointTransactionService, PointTransactionRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

### 그룹 6
- **Product**
  - [정상] ProductService/ProductRepository 정상 연결
  - [점검] ProductRepository에서 Product, ProductCategory import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductAttribute**
  - [정상] ProductAttributeService/ProductAttributeRepository 정상 연결
  - [점검] ProductAttributeService, ProductAttributeRepository에서 ProductAttribute import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductAttributeValue**
  - [정상] ProductAttributeValueService/ProductAttributeValueRepository 정상 연결
  - [점검] ProductAttributeValueService, ProductAttributeValueRepository에서 ProductAttributeValue import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductCategory**
  - [정상] ProductCategoryService/ProductCategoryRepository 정상 연결
  - [점검] ProductCategoryService, ProductCategoryRepository에서 ProductCategory import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductContent**
  - [정상] ProductContentService/ProductContentRepository 정상 연결
  - [점검] ProductContentService, ProductContentRepository에서 ProductContent import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

### 그룹 7
- **ProductQuestion**
  - [문제] ProductQuestionService, ProductQuestionRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **ProductRating**
  - [문제] ProductRatingService, ProductRatingRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **ProductReview**
  - [정상] ProductReviewService/ProductReviewRepository 정상 연결
  - [점검] ProductReviewService, ProductReviewRepository 등에서 ProductReview import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductImage**
  - [정상] ProductImageService/ProductImageRepository 정상 연결
  - [점검] ProductImageService, ProductImageRepository에서 ProductImage import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductMedia**
  - [정상] ProductMediaService/ProductMediaRepository 정상 연결
  - [점검] ProductMediaService, ProductMediaRepository에서 ProductMedia import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

### 그룹 8
- **ProductStatus**
  - [문제 없음] Enum, 별도 서비스/리포지터리 불필요
  - [상태] 완료

- **Promotion**
  - [정상] PromotionService/PromotionRepository 정상 연결
  - [점검] PromotionService, PromotionRepository에서 Promotion import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **RecurringOrder**
  - [정상] RecurringOrderService/RecurringOrderRepository 정상 연결
  - [점검] RecurringOrderService, RecurringOrderRepository에서 RecurringOrder import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **RecurringOrderItem**
  - [정상] RecurringOrderItemRepository 정상 연결 (Service에서 직접 관리)
  - [점검] RecurringOrderService, RecurringOrderItemRepository 등에서 RecurringOrderItem import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ReviewResponse**
  - [문제] ReviewResponseService, ReviewResponseRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

### 그룹 9
- **Role**
  - [정상] RoleService/RoleRepository 정상 연결
  - [점검] UserService, RoleService, UserRepository, RoleRepository 등에서 Role import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **SavedForLater**
  - [정상] SavedForLaterService/SavedForLaterRepository 정상 연결
  - [점검] SavedForLaterService, SavedForLaterRepository에서 SavedForLater import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **Shipment**
  - [정상] ShipmentService/ShipmentRepository 정상 연결
  - [점검] ShipmentService, ShipmentRepository, OrderService 등에서 Shipment import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **Terms**
  - [문제 없음] 별도 서비스/리포지터리 불필요 (Value Object)
  - [점검] Terms는 서비스/리포지터리에서 import/사용처 없음. 문제 없음.
  - [상태] 완료

### 그룹 10
- **Subscription**
  - [정상] SubscriptionService/SubscriptionRepository 정상 연결
  - [점검] SubscriptionService, SubscriptionRepository 등에서 Subscription import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **SubscriptionPlan**
  - [정상] SubscriptionPlanService/SubscriptionPlanRepository 정상 연결
  - [점검] SubscriptionPlanService, SubscriptionPlanRepository 등에서 SubscriptionPlan import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **SubscriptionPayment**
  - [정상] SubscriptionPaymentService/SubscriptionPaymentRepository 정상 연결
  - [점검] SubscriptionPaymentService, SubscriptionPaymentRepository 등에서 SubscriptionPayment import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **RecurringOrder**
  - [정상] RecurringOrderService/RecurringOrderRepository 정상 연결
  - [점검] RecurringOrderService, RecurringOrderRepository 등에서 RecurringOrder import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **RecurringOrderItem**
  - [정상] RecurringOrderItemRepository 정상 연결 (Service에서 직접 관리)
  - [점검] RecurringOrderService, RecurringOrderItemRepository 등에서 RecurringOrderItem import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

### 그룹 11
- **ProductReview**
  - [정상] ProductReviewService/ProductReviewRepository 정상 연결
  - [점검] ProductReviewService, ProductReviewRepository 등에서 ProductReview import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductRating**
  - [문제] ProductRatingService, ProductRatingRepository 미존재
  - [조치] 서비스/리포지터리 생성 완료
  - [상태] 완료

- **ProductMedia**
  - [정상] ProductMediaService/ProductMediaRepository 정상 연결
  - [점검] ProductMediaService, ProductMediaRepository 등에서 ProductMedia import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductContent**
  - [정상] ProductContentService/ProductContentRepository 정상 연결
  - [점검] ProductContentService, ProductContentRepository 등에서 ProductContent import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료

- **ProductAttribute**
  - [정상] ProductAttributeService/ProductAttributeRepository 정상 연결
  - [점검] ProductAttributeService, ProductAttributeRepository 등에서 ProductAttribute import 및 타입, 사용처 모두 정상. 오타, 잘못된 import, 불필요 import, 미사용 엔티티 없음.
  - [상태] 완료


<!--
====================
[그룹 9~11 점검 작업 지시사항]
====================

1. 각 그룹별 엔티티(5개씩)를 backend/backend/src/main/java/com/mylittleshop/backend/model에서 확인한다.
2. 각 엔티티에 대해 다음을 점검한다:
   - Service/Repository 클래스가 존재하는지 (service, repository 패키지 내)
   - import 경로가 올바른지 (예: com.mylittleshop.backend.model.XXX)
   - 타입 선언 및 사용이 올바른지 (오타, 잘못된 타입, 불필요 import, 미사용 엔티티 등)
   - Enum/Value Object는 별도 서비스/리포지터리 불필요 여부 명시
3. 점검 결과를 아래와 같은 포맷으로 이 파일에 바로 업데이트한다:

### 그룹 N
- **엔티티명**
  - [정상] ...
  - [점검] ...
  - [상태] 완료
- **엔티티명**
  - [문제] ...
  - [조치] ...
  - [상태] 미해결
...

4. 점검 시 참고할 파일 위치 예시:
   - Service: backend/backend/src/main/java/com/mylittleshop/backend/service
   - Repository: backend/backend/src/main/java/com/mylittleshop/backend/repository
   - Model(엔티티): backend/backend/src/main/java/com/mylittleshop/backend/model

5. 점검이 끝난 그룹마다 반드시 이 파일에 결과를 기록하고, 미해결 항목은 [조치]에 생성 필요 등 구체적으로 남긴다.
6. 점검 중간에 끊길 경우, 이 주석 아래에 "### 그룹 9"부터 이어서 작업하면 된다.

예시)
### 그룹 9
- **엔티티A**
  - [정상] ...
  - [점검] ...
  - [상태] 완료
- **엔티티B**
  - [문제] ...
  - [조치] ...
  - [상태] 미해결
...

-- 다음 작업자는 이 주석을 참고하여 그룹 9부터 순차적으로 점검 및 기록을 이어가세요. --
-- 점검이 끝나면 주석은 남겨두고, 실제 점검 결과만 추가/수정하세요. --
-- 점검 포인트, 파일 경로, 기록 포맷 등은 위 예시를 따르세요. --
-- 필요시 이전 그룹의 기록 포맷을 참고하세요. --
-- 작업 중 궁금한 점은 이전 커밋/작업 내역 또는 담당자에게 문의하세요. --
-- (이 주석은 삭제하지 말고 계속 유지하세요) --
-- 작업 완료 후에도 이 주석은 다음 점검자 안내용으로 남겨두세요. --
-- 점검이 끝난 그룹은 반드시 [상태] 완료로 표시하세요. --
-- 미해결 항목은 [상태] 미해결로 남기고, [조치]에 구체적 생성/수정 필요사항을 적으세요. --
-- 점검이 끝난 후, 전체 완료 시점에만 별도 안내/정리 커밋을 남기세요. --
-- (이 주석은 팀 내 엔티티-서비스/리포지터리 점검 표준입니다) --
--
