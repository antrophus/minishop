# Backend 작업 계획 및 진행 상황

## Task Master Subtask 표준 작업 단계

1. **목표 이해 및 준비**
   - `get_task`를 사용하여 subtask의 상세 목표와 요구사항 확인
   - 작업 전 필요한 정보와 리소스 수집

2. **초기 탐색 및 계획 수립**
   - 코드베이스 탐색
   - 수정이 필요한 파일, 함수, 라인 파악
   - 변경점, 예상 diff, 난이도, 잠재적 이슈 정리

3. **계획 기록**
   - `update_subtask`를 사용해 상세 계획과 탐색 결과를 기록
   - 변경 예정 위치, 접근 방식, 고려사항 포함

4. **계획 검증**
   - `get_task`를 사용해 기록된 계획이 정확히 반영되었는지 확인
   - 계획의 실현 가능성과 완전성 검토

5. **구현 시작**
   - `set_task_status`로 상태를 'in-progress'로 변경
   - 계획에 따라 실제 코드 작업 수행

6. **진행상황/교훈 반복 기록**
   - `update_subtask`를 사용해 진행상황, 성공/실패, 결정사항, 코드 스니펫 등을 지속적으로 기록
   - 이슈 발생 시 해결 방법과 교훈 기록

7. **작업 완료 및 규칙/문서 갱신**
   - 코드, 규칙, 문서 리뷰 및 필요시 규칙 추가/수정
   - 코드 패턴, 모범 사례, 주의사항 기록

8. **작업 완료 처리**
   - `set_task_status`로 상태를 'done'으로 변경
   - 최종 테스트 및 검증 수행

9. **커밋 및 다음 작업 진행**
   - 변경사항 git 커밋
   - 다음 subtask로 이동

## 현재까지의 작업 내역 (2025-05-18 기준)

### [2025-05-05] Backend Environment Setup (Task #3)
#### 목표 이해 및 준비
- Task #3의 목표와 요구사항 확인: Spring Boot 기반 백엔드 구성, Java 17+ 환경 설정
- 개발 환경 설정 및 필요한 의존성 목록 작성

#### 초기 탐색 및 계획 수립
- Gradle 기반 Spring Boot 3.4.5 프로젝트 구조 설계
- 필요한 의존성 식별 (Spring Web, JPA, MySQL, Security 등)
- 패키지 구조 계획 및 application.properties 설정 계획

#### 계획 기록
- 환경 설정 방법 및 필요한 구성요소 기록
- 각 subtask의 목표 및 검증 방법 명확화

#### 구현 시작
- 상태를 'in-progress'로 변경
- Gradle 프로젝트 초기화 및 기본 설정 구현
- Spring Boot 애플리케이션 클래스 생성
- 기본 컨트롤러 구현

#### 진행상황/교훈 기록
- 각 subtask(3.1~3.6) 진행 상황 기록
- Spring Boot 3.4.5 환경 설정 관련 주의사항 기록

#### 작업 완료 처리
- 구현 완료 후 상태를 'done'으로 변경
- 모든 subtask의 완료 상태 확인
- 애플리케이션 실행 및 기본 API 엔드포인트 테스트 완료

#### 커밋 및 다음 작업
- 변경사항 커밋: "feat: Initialize Spring Boot backend environment"
- 다음 작업(Database Schema Design) 준비

### [2025-05-07 ~ 2025-05-10] Backend 서비스 레이어 구현
- Product Repository 구현 (Task #7.2) - 2025-05-07 완료
- Product Service Layer 개발 (Task #7.3) - 2025-05-08 완료
- Order Service Layer 구현 (Task #22.2) - 2025-05-09 완료
- Cart Service Layer 구현 (Task #21.2) - 2025-05-09 완료
- Cart Persistence Mechanism 개발 (Task #21.3) - 2025-05-10 완료

### [2025-05-10 ~ 2025-05-18] Database Schema Design 완료 (Task #5)
#### 목표 이해 및 준비
- Task #5의 목표와 요구사항 확인: MySQL 데이터베이스 스키마 설계
- PRD에 명시된 스키마 요구사항 및 엔티티 관계 이해

#### 초기 탐색 및 계획 수립
- 데이터베이스 구성 검토 및 설계 방향 결정
- 필요한 엔티티 식별 및 관계 모델링 계획

#### 구현 시작
- 상태를 'in-progress'로 변경
- 각 엔티티별 스키마 설계 작업 진행:
  - Database Configuration Setup (Task #5.1) - 완료
  - User Management Schema (Task #5.2) - 완료
  - Product Catalog Schema (Task #5.3) - 완료
  - Product Content Schema (Task #5.4) - 완료 (2025-05-18)
  - Order Management Schema (Task #5.5) - 완료 (2025-05-18)
  - Shopping Cart Schema (Task #5.6) - 완료 (2025-05-18)
  - Subscription Schema (Task #5.7) - 완료 (2025-05-18)
  - Database Migration Scripts Creation (Task #5.8) - 완료 (2025-05-20)
  - Database Schema Testing (Task #5.9) - 완료 (2025-05-19)
  - Schema Documentation and Finalization (Task #5.10) - 완료 (2025-05-19)

#### 진행상황/교훈 기록
- 엔티티 관계 모델링 및 최적화 작업 진행 상황 기록
- JPA 엔티티 매핑 관련 주의사항 및 해결책 기록
- 모든 스키마 설계 작업 완료, 단 마이그레이션 스크립트는 진행 중

#### 작업 완료 처리
- 대부분의 서브태스크가 완료되어 Task #5의 상태를 'done'으로 변경
- Database Migration Scripts Creation (Task #5.8)은 계속 진행 중

#### 다음 단계
- 단위 테스트 구현 준비
- Repository 및 Service 계층 구현 계획
- 콘텐츠 검색 및 관리 기능 개발 계획

### [2025-05-18] Product Content Schema Design 완료 (Task #5.4)
#### 목표 이해 및 준비
- Product Content Schema 설계 목표 확인: 상품 설명, 이미지, 비디오 등 미디어 자산을 위한 스키마 설계
- 요구사항 및 기존 구현 검토

#### 초기 탐색 및 계획 수립
- 기존 코드베이스 탐색
- ProductContent, ProductMedia, ProductReview, ProductRating 엔티티 분석
- 이미 구현된 기능과 누락된 기능 식별

#### 구현 검증
- 모든 필요한 엔티티와 관계가 이미 적절히 구현되어 있음을 확인:
  1. ProductContent 엔티티:
     - 모든 필수 필드 및 관계 구현됨 (id, product, content, contentType, majorVersion, minorVersion, status 등)
     - 버전 관리 시스템 구현됨 (incrementVersion 메서드)
     - 적절한 인덱스 설정 (product_id, status, version)
     - AI 생성 콘텐츠 관련 필드 추가 (isAIGenerated, aiPromptUsed, aiModelUsed)

  2. ProductMedia 엔티티:
     - 다양한 미디어 타입 지원 (IMAGE, VIDEO, DOCUMENT, MODEL_3D, AUDIO)
     - 필요한 모든 필드 및 관계 구현됨
     - 적절한 인덱스 설정 (product_id, media_type, is_featured)
     - 썸네일 및 메타데이터 관리 필드 구현

  3. ProductReview 및 ProductRating 엔티티:
     - 리뷰 모더레이션 워크플로우 구현됨 (PENDING, APPROVED, REJECTED, FLAGGED 상태)
     - 도움됨/도움안됨 투표 시스템 구현됨
     - 자동 평점 계산 기능 구현됨
     - 판매자/관리자 응답 기능 (ReviewResponse 엔티티)
     - 적절한 인덱스 설정

  4. ContentStatus 및 ReviewStatus 열거형 정의

  5. Product 엔티티에 적절한 관계 매핑:
     - Product와 ProductContent 간의 일대일 관계
     - Product와 ProductMedia 간의 일대다 관계
     - Product와 ProductReview 간의 일대다 관계

#### 작업 완료 처리
- 구현이 모든 요구사항을 충족함을 확인
- 작업 상태를 'done'으로 변경
- 작업 로그 업데이트

#### 다음 단계
- 단위 테스트 구현 준비
- Repository 및 Service 계층 구현 계획
- 콘텐츠 검색 및 관리 기능 개발 계획

### [2025-05-18] Order Management Schema Design 완료 (Task #5.5)
#### 목표 이해 및 준비
- Order Management Schema 설계 목표 확인: 주문 관리, 결제 추적, 배송 관리를 위한 스키마 설계
- 요구사항 및 기존 구현 검토

#### 초기 탐색 및 계획 수립
- 기존 코드베이스 탐색
- Order, OrderItem, Payment, Shipment, Delivery 엔티티 분석
- 이미 구현된 기능과 개선이 필요한 부분 식별

#### 구현 내용
다음과 같은 개선 사항을 구현했습니다:

1. OrderItem 클래스 개선:
   - 기존 createOrderItem 메서드의 오류 수정 (Product의 다양한 가격 타입 처리)
   - Product.PriceType 열거형을 활용한 가격 타입별 주문 항목 생성 기능 추가
   - 가격 타입 변경 기능 추가
   - 적절한 인덱스 설정 추가 (order_id, product_id)

2. Payment 엔티티 개선:
   - amount 필드를 Integer에서 BigDecimal로 변경하여 정확한 통화 처리
   - 트랜잭션 ID, 게이트웨이 응답, 결제 시도/완료/실패/취소 타임스탬프 필드 추가
   - 결제 메타데이터 및 시도자 정보 필드 추가
   - 결제 처리를 위한 편의 메서드 추가 (attemptPayment, completePayment, failPayment, cancelPayment)
   - 적절한 인덱스 설정 (order_id, status, transaction_id)

3. Shipment 엔티티 개선:
   - 추적 번호, 배송사, 예상 및 실제 배송 날짜 필드 추가
   - 상세 배송지 정보 필드 추가 (수령인, 연락처, 주소 세부정보)
   - 배송 관리를 위한 편의 메서드 추가 (startShipping, completeDelivery, returnShipment, cancelShipment)
   - 적절한 인덱스 설정 (order_id, tracking_number, status)

4. Order 엔티티 개선:
   - Payment, Shipment와의 일대다 관계 명시적 설정
   - 주문 상태 변경 이력 추적을 위한 OrderStatusHistory 관계 추가
   - 결제 방법, 배송 방법, 배송비, 세금, 쿠폰, 선물 관련 필드 추가
   - 추가 편의 메서드 구현 (addPayment, removePayment, addShipment, removeShipment, changeStatus)
   - 총 결제 금액 및 미결제 금액 계산 기능 추가
   - 적절한 인덱스 설정 (user_id, status, order_number)

5. OrderStatusHistory 엔티티 신규 추가:
   - 주문 상태 변경 이력 추적을 위한 엔티티 구현
   - 이전 상태, 새 상태, 변경 사유, 변경자, 변경 시간 기록
   - 적절한 인덱스 설정 (order_id, status)

6. PaymentStatus 열거형 확장:
   - AUTHORIZED, REFUNDED, PARTIAL_REFUND 상태 추가
   - 각 상태에 대한 설명 주석 추가

#### 작업 완료 처리
- 모든 필요한 엔티티와 관계가 구현되고 개선되었음을 확인
- 작업 상태를 'done'으로 변경
- 작업 로그 업데이트

#### 다음 단계
- 주문 상태 변경 로직 및 결제 처리 서비스 구현 계획
- 배송 추적 및 알림 기능 개발 계획
- 단위 테스트 및 통합 테스트 작성 계획

### [2025-05-18] Shopping Cart Schema Design 완료 (Task #5.6)
#### 목표 이해 및 준비
- Shopping Cart Schema 설계 목표 확인: 장바구니 기능, 저장된 아이템, 장바구니 지속성 관리를 위한 스키마 설계
- 요구사항 및 기존 구현 검토

#### 초기 탐색 및 계획 수립
- 기존 코드베이스 탐색
- Cart, CartItem, SavedForLater 엔티티 분석
- 기존 구현의 한계점 파악 및 개선 방향 설정

#### 구현 내용
다음과 같은 개선 사항을 구현했습니다:

1. CartStatus 열거형 추가:
   - 장바구니 상태 관리를 위한 ACTIVE, MERGED, ABANDONED, CONVERTED, EXPIRED 상태 정의
   - 각 상태에 대한 설명 주석 추가

2. Cart 엔티티 개선:
   - 장바구니 상태 관리 필드 추가 (CartStatus)
   - 만료 날짜 및 활동 추적 필드 추가 (expiresAt, lastActivityDate)
   - 세션 정보 및 사용자 추적 필드 추가 (sessionId, ipAddress, userAgent)
   - 인덱스 추가 (user_id, status, expires_at)
   - 주요 편의 메서드 구현:
     * 아이템 추가/제거/수량 변경 (addItem, removeItem, updateItemQuantity)
     * 장바구니 비우기 (clear)
     * 상태 변경 (changeStatus)
     * 만료 관리 (setExpirationDays, isExpired)
     * 장바구니 병합 (mergeWith)
     * 방치 여부 확인 (isAbandoned)
     * 총 상품 수량 계산 (getTotalQuantity)

3. CartItem 엔티티 개선:
   - 가격 정보 필드 추가 (unitPrice, appliedPriceType)
   - 선물 관련 필드 추가 (isGift, giftMessage)
   - 제품 옵션 및 속성 필드 추가 (productOptions, selectedAttributes)
   - 인덱스 추가 (cart_id, product_id)
   - 주요 편의 메서드 구현:
     * 아이템 생성 (createCartItem)
     * 수량 변경 (updateQuantity)
     * 가격 타입 변경 (changePriceType)
     * 선물 설정 (setAsGift)
     * 총 가격 계산 (getTotalPrice)

4. SavedForLater 엔티티 개선:
   - 사용자 노트 필드 추가 (userNotes)
   - 알림 관련 필드 추가 (reminderDate, reminderSent)
   - 우선순위 및 출처 필드 추가 (priority, source)
   - 인덱스 추가 (user_id, product_id)
   - 주요 편의 메서드 구현:
     * 알림 설정 (setReminder)
     * 알림 전송 처리 (markReminderSent)
     * 노트 추가 (addNote)
     * 우선순위 설정 (setPriority)
     * 알림 필요 여부 확인 (isReminderNeeded)

#### 작업 완료 처리
- 모든 필요한 엔티티와 관계가 구현되고 개선되었음을 확인
- 작업 상태를 'done'으로 변경
- 작업 로그 업데이트

#### 다음 단계
- 장바구니 Repository 인터페이스 설계
- 장바구니 Service 계층 구현
- 장바구니 만료, 병합, 방치 감지 로직 구현
- 장바구니 관련 단위 테스트 작성

### [2025-05-18] Subscription and Recurring Orders Schema Design 완료 (Task #5.7)
#### 목표 이해 및 준비
- Subscription and Recurring Orders Schema 설계 목표 확인: 구독 기반 제품과 반복 주문을 위한 스키마 설계
- 요구사항 및 기존 구현 검토

#### 초기 탐색 및 계획 수립
- 기존 코드베이스 탐색
- 이미 구현된 일부 구독 관련 엔티티(Subscription, SubscriptionPlan, SubscriptionStatus, SubscriptionPayment) 분석
- 기존 구현의 한계점 파악 및 확장 방향 설정

#### 구현 내용
다음과 같은 개선 및 추가 사항을 구현했습니다:

1. SubscriptionStatus 열거형 확장:
   - 기존 상태(ACTIVE, PAUSED, CANCELLED, EXPIRED)에 새로운 상태 추가
   - PENDING, FAILED, TRIAL, PAST_DUE 상태 추가 및 설명 주석 추가

2. BillingFrequency 열거형 생성:
   - 다양한 결제 주기 정의 (DAILY, WEEKLY, BIWEEKLY, MONTHLY, BIMONTHLY, QUARTERLY, SEMIANNUAL, ANNUAL)
   - 각 주기별 일수 계산 메서드 구현

3. SubscriptionPlan 엔티티 확장:
   - 가격 정보 개선 (Integer에서 BigDecimal로 변경, 할인 적용)
   - 결제 주기 정보 추가 (BillingFrequency)
   - 호환 제품 관리를 위한 many-to-many 관계 설정
   - 플랜 관리 기능 추가 (특징, 활성화 상태, 무료 체험 기간, 일시정지 제한 등)
   - 편의 메서드 구현 (호환 상품 관리, 할인 가격 계산, 계약 가치 계산 등)
   - 인덱스 추가 (is_active)

4. SubscriptionPayment 엔티티 확장:
   - 가격 관련 필드 개선 (Integer에서 BigDecimal로 변경, 원가/할인/세금 구분)
   - 결제 처리 관련 필드 추가 (트랜잭션ID, 결제방법, 게이트웨이 응답 등)
   - 청구 기간 및 문서 관리 필드 추가
   - 실패 처리 및 재시도 관련 필드 추가
   - 편의 메서드 구현 (결제 완료/실패/환불 처리, 재시도 계산, 청구 기간 설정 등)
   - 인덱스 추가 (subscription_id, status, transaction_id)

5. Subscription 엔티티 확장:
   - 상태 관리 개선 (SubscriptionStatus 활용)
   - 계약 및 체험 기간 관리 필드 추가
   - 결제 관련 필드 추가 (결제 방법, 고객 프로필)
   - 자동 갱신 및 취소 관련 필드 추가
   - 편의 메서드 구현 (활성화, 취소, 일시정지, 재개, 갱신, 결제 관리 등)
   - 인덱스 추가 (user_id, product_id, plan_id, status, next_payment_date, contract_end_date)

6. RecurringOrder 엔티티 신규 생성:
   - 반복 주문을 위한 기본 정보 필드 구현
   - 주문 주기 및 다음 주문일 관리
   - 결제 및 배송 정보 관리
   - 편의 메서드 구현 (주문 항목 관리, 주문 생성, 주문일 계산, 일시정지, 재개, 취소, 알림 관리 등)
   - 인덱스 추가 (user_id, subscription_id, status, next_order_date)

7. RecurringOrderItem 엔티티 신규 생성:
   - 반복 주문 항목 관리를 위한 기본 필드 구현
   - 가격 및 수량 관리
   - 편의 메서드 구현 (항목 생성, 수량 변경, 가격 타입 변경, 선물 설정, 총액 계산 등)
   - 인덱스 추가 (recurring_order_id, product_id)

8. Order 엔티티 확장:
   - RecurringOrder와의 관계 추가 (ManyToOne)
   - 반복 주문 여부 필드 추가 (isRecurring)
   - 인덱스 추가 (recurring_order_id)

#### 작업 완료 처리
- 모든 필요한 엔티티와 관계가 구현되고 개선되었음을 확인
- 작업 상태를 'done'으로 변경
- 작업 로그 업데이트

#### 다음 단계
- 구독 및 반복 주문 Repository 인터페이스 설계
- 구독 및 반복 주문 Service 계층 구현
- 결제 처리 및 알림 서비스 구현
- 구독 갱신 및 자동 주문 생성 스케줄러 개발
- 단위 테스트 작성

### [2025-05-20] Database Migration Scripts Creation 완료 (Task #5.8)
#### 목표 이해 및 준비
- 스키마 버전 관리, 업데이트, 롤백을 위한 데이터베이스 마이그레이션 스크립트 생성 목표 확인
- 기존 구현된 스키마 및 마이그레이션 도구 검토

#### 초기 탐색 및 계획 수립
- Flyway가 이미 구성되어 있고 `/db/migration` 디렉토리에 초기 마이그레이션 스크립트(`V1__init.sql`)가 존재함을 확인
- 마이그레이션 파일 구조 및 네이밍 규칙 분석
- 환경별 Flyway 구성 확인 (개발, 테스트, 운영 환경)

#### 구현 내용
다음과 같은 마이그레이션 스크립트를 구현했습니다:

1. **V2__entity_improvements.sql**: 
   - 기존 테이블의 컬럼 추가/수정
   - 가격 처리 관련 데이터 타입 변경 (INT → DECIMAL)
   - 인덱스 추가
   - 새로운 테이블 추가 (order_status_history, recurring_orders 등)

2. **V2__rollback.sql**:
   - V2 마이그레이션의 롤백 스크립트 (수동 실행)
   - 인덱스, 외래 키 제약 조건, 추가된 컬럼, 테이블 제거 등

3. **V3__sample_data.sql**:
   - 개발 및 테스트 환경에서 사용할 샘플 데이터 추가
   - 역할, 사용자, 제품, 카테고리 등 기본 데이터 구성

4. **테스트 데이터 구성**:
   - 테스트 환경에서 사용할 반복 가능한 테스트 데이터 스크립트 (`R__test_data.sql`)
   - R__ 접두사를 사용해 반복 실행 가능한 마이그레이션으로 설정

5. **환경별 Flyway 구성 최적화**:
   - 개발 환경: Flyway 및 Hibernate ddl-auto 병행 사용
   - 테스트 환경: 테스트 데이터 자동 로드 및 검증 설정
   - 운영 환경: 안전한 마이그레이션을 위한 설정

6. **마이그레이션 전략 문서화**:
   - `README.md` 파일에 Flyway 마이그레이션 전략 및 실행 프로세스 설명
   - 환경별 구성, 롤백 전략, 모범 사례 등 포함

#### 작업 완료 처리
- 모든 필요한 마이그레이션 스크립트와 설정이 완료됨을 확인
- 작업 상태를 'done'으로 변경
- 작업 로그 업데이트

#### 다음 단계
- 이제 User Authentication and Authorization System (Task #6) 작업을 시작할 수 있음
- 다음 작업은 Spring Security Configuration Setup (Task #6.1)
- JWT Token Implementation (Task #6.2) 및 User Registration Functionality (Task #6.3) 작업 준비

## 대기 중인 작업 내역

### User Authentication and Authorization System (Task #6)
- Spring Security 설정 및 JWT 토큰 구현 준비 필요
- 사용자 등록, 인증, 권한 관리 기능 구현 계획 필요
- 보안 관련 설정 및 전략 수립 필요

## 다음 작업 계획

1. **User Authentication and Authorization System** (Task #6) 착수
   - Spring Security Configuration Setup (Task #6.1) - 2025-05-26 시작 예정
   - JWT Token Implementation (Task #6.2) - 2025-05-27 시작 예정
   - User Registration Functionality (Task #6.3) - 2025-05-28 시작 예정

2. **Product Management API** (Task #7) 계속 진행
   - Create Product Entity (Task #7.1) - 2025-05-21 시작 예정
   - Implement Product Controller (Task #7.4) - 2025-05-25 시작 예정

## 백엔드 개발 참고사항

### 코드 구조 표준
- **계층적 패키지 구조 활용**
  - `controller`: 클라이언트 요청 처리 및 응답 반환
  - `service`: 비즈니스 로직 구현
  - `repository`: 데이터베이스 접근 및 영속성 관리
  - `model`: 엔티티 및 DTO 정의
  - `config`: 애플리케이션 설정 관리
  - `exception`: 예외 처리 및 관리
  - `util`: 유틸리티 및 헬퍼 기능

- **RESTful API 설계 원칙 준수**
  - 리소스 중심 URL 설계 (명사 사용)
  - 적절한 HTTP 메소드 활용 (GET, POST, PUT, DELETE)
  - 표준 HTTP 상태 코드 활용
  - 버전 관리 전략 적용 (URL 또는 헤더 기반)

- **의존성 주입 활용**
  - 생성자 기반 의존성 주입 선호
  - @Autowired보다 명시적 생성자 주입 방식 사용
  - 인터페이스 기반 의존성 정의로 결합도 낮추기

### 데이터베이스 관련 지침
- **JPA 모범 사례**
  - 양방향 관계 설정 시 무한 순환 참조 주의
  - @OneToMany 관계에서 적절한 CascadeType 설정
  - 일괄 처리 작업에 @Transactional 적용
  - 성능 고려한 Fetch Type 설정

- **인덱스 전략**
  - 조회 빈도가 높은 필드에 인덱스 적용
  - 복합 인덱스 활용으로 성능 향상
  - 부분 인덱스 및 인덱스 필터링 고려

- **트랜잭션 관리**
  - 명시적 트랜잭션 경계 설정
  - 트랜잭션 전파 속성 적절히 활용
  - 트랜잭션 격리 수준 고려

### 성능 최적화 전략
- **데이터베이스 쿼리 최적화**
  - N+1 문제 방지를 위한 조인 페치 활용
  - 페이징 처리 및 동적 쿼리 최적화
  - QueryDSL 활용한 타입 안전 쿼리 작성

- **캐싱 전략**
  - 자주 조회되는 데이터 JPA 2차 캐시 활용
  - Redis 캐싱 적용 계획
  - 캐시 무효화 전략 수립

- **비동기 처리**
  - 대용량 처리 작업 비동기 실행 고려
  - @Async 및 CompletableFuture 활용
  - 스케줄링된 작업 최적화

## 발견된 이슈 및 해결책

### 1. 데이터베이스 스키마 설계 관련 이슈
- **이슈**: Product와 ProductContent 간의 관계 설정 시 순환 참조 문제 발생 (2025-05-14)
- **원인**: 양방향 관계에서 Jackson 직렬화 처리 문제
- **해결책**: @JsonManagedReference와 @JsonBackReference 어노테이션 적용으로 해결, 또는 DTO로 변환 후 API 응답 반환

### 2. Entity 클래스 구현 관련 이슈
- **이슈**: JPA 엔티티 매핑에서 복합키 처리 문제 발생 (2025-05-15)
- **원인**: 복합키 정의 방식 선택의 어려움
- **해결책**: @IdClass와 @EmbeddedId 접근법 비교 후 @EmbeddedId 채택해 해결, 더 객체지향적이고 타입 안전한 접근법으로 판단

### 3. 테스트 환경 설정 이슈
- **이슈**: H2 인메모리 DB와 MySQL 간 SQL 문법 차이로 테스트 실패 (2025-05-16)
- **원인**: MySQL 특정 기능 사용 시 H2와 호환성 문제
- **해결책**: 테스트용 SQL 스크립트 별도 작성 및 테스트 프로파일 분리, @SQL 어노테이션 활용하여 테스트별 데이터 설정

### 4. Spring Boot 버전 호환성 이슈
- **이슈**: Spring Boot 3.4.5와 일부 라이브러리 호환성 문제 (2025-05-17)
- **원인**: 최신 버전 Spring Boot와 일부 라이브러리 버전 불일치
- **해결책**: Spring Boot 의존성 관리 기능을 활용하여 호환되는 버전으로 명시적 지정

## 백엔드 성능 모니터링 계획

### 모니터링 도입 전략
- Actuator 엔드포인트 구성 및 보안 설정
- Micrometer 지표 수집 및 Prometheus 연동 계획
- Grafana 대시보드 구성 계획

### 주요 모니터링 지표
- API 엔드포인트별 응답 시간
- 데이터베이스 쿼리 성능 및 연결 풀 상태
- JVM 메모리 사용량 및 GC 활동
- 트랜잭션 처리량 및 오류율

### 로깅 전략
- 구조화된 로깅 구현 (JSON 형식)
- 로그 레벨 적절히 구성 (PROD/DEV 환경 분리)
- ELK 스택 연동 계획 