# MyLittleShop 코드 생성 규칙

## 1. 일반 규칙

- 모든 코드는 Java 코딩 표준을 준수해야 합니다.
- 클래스, 메소드, 변수 이름은 명확하고 의미있게 지정합니다.
- 중복 코드를 최소화하고 재사용 가능한 컴포넌트를 활용합니다.
- 모든 클래스와 주요 메소드에는 JavaDoc 주석을 포함합니다.

## 2. 패키지 구조

```
com.mylittleshop.backend
├── model          # 엔티티 클래스
├── repository     # 데이터 접근 인터페이스
├── service        # 비즈니스 로직
├── controller     # API 엔드포인트
├── dto            # 데이터 전송 객체
├── config         # 애플리케이션 설정
├── security       # 보안 관련 구성
├── util           # 유틸리티 클래스
└── exception      # 예외 처리
```

## 3. 엔티티 클래스 규칙

### 위치
- 모든 엔티티 클래스는 `com.mylittleshop.backend.model` 패키지에 위치합니다.

### 명명 규칙
- 엔티티 클래스 이름은 대문자로 시작하는 카멜 케이스를 사용합니다 (예: `Product`, `OrderItem`).
- 데이터베이스 테이블 이름은 소문자 스네이크 케이스로 지정합니다 (예: `product`, `order_item`).

### 필드 규칙
- 기본 키 필드는 항상 `id`로 명명합니다.
- 날짜/시간 필드의 경우:
  - 생성 시간: `createdAt`
  - 수정 시간: `updatedAt`
- 외래 키 필드는 참조하는 엔티티 이름 + "Id"로 명명합니다 (예: `userId`, `productId`).
- boolean 타입 필드는 `is` 또는 `has`로 시작합니다 (예: `isActive`, `hasExpired`).

### 관계 규칙
- OneToMany 관계의 컬렉션 필드는 복수형으로 명명합니다 (예: `orders`, `products`).
- ManyToOne 관계의 필드는 단수형으로 명명합니다 (예: `user`, `category`).

## 4. 리포지토리 인터페이스 규칙

### 위치
- 모든 리포지토리는 `com.mylittleshop.backend.repository` 패키지에 위치합니다.

### 명명 규칙
- 리포지토리 이름은 엔티티 이름 + "Repository"로 명명합니다 (예: `ProductRepository`).
- 리포지토리는 Spring Data JPA의 `JpaRepository`를 상속받습니다.

### 메소드 규칙
- 조회 메소드는 `findBy` + 필드명으로 시작합니다 (예: `findByUserId`).
- 존재 여부 확인 메소드는 `existsBy` + 필드명으로 시작합니다 (예: `existsByEmail`).
- 삭제 메소드는 `deleteBy` + 필드명으로 시작합니다 (예: `deleteByOrderId`).
- 카운트 메소드는 `countBy` + 필드명으로 시작합니다 (예: `countByCategory`).

## 5. 서비스 클래스 규칙

### 위치
- 모든 서비스는 `com.mylittleshop.backend.service` 패키지에 위치합니다.

### 명명 규칙
- 서비스 인터페이스는 도메인 이름 + "Service"로 명명합니다 (예: `ProductService`).
- 서비스 구현체는 인터페이스 이름 + "Impl"로 명명합니다 (예: `ProductServiceImpl`).

### 메소드 규칙
- 조회 메소드는 `get` + 엔티티명으로 시작합니다 (예: `getProduct`, `getAllProducts`).
- 생성 메소드는 `create` + 엔티티명으로 시작합니다 (예: `createProduct`).
- 수정 메소드는 `update` + 엔티티명으로 시작합니다 (예: `updateProduct`).
- 삭제 메소드는 `delete` + 엔티티명으로 시작합니다 (예: `deleteProduct`).

## 6. DTO 클래스 규칙

### 위치
- 모든 DTO는 `com.mylittleshop.backend.dto` 패키지에 위치합니다.

### 명명 규칙
- 요청 DTO: 엔티티명 + "Request" (예: `ProductRequest`, `UserRegistrationRequest`).
- 응답 DTO: 엔티티명 + "Response" (예: `ProductResponse`, `OrderDetailResponse`).

### 구조 규칙
- DTO는 필요한 필드만 포함하며, 엔티티와 1:1 매핑될 필요는 없습니다.
- 중첩된 데이터 구조의 경우 내부 클래스로 구현합니다.
- 모든 DTO는 기본 생성자와 모든 필드를 포함하는 생성자를 가집니다.

## 7. 컨트롤러 클래스 규칙

### 위치
- 모든 컨트롤러는 `com.mylittleshop.backend.controller` 패키지에 위치합니다.

### 명명 규칙
- 컨트롤러 이름은 도메인 이름 + "Controller"로 명명합니다 (예: `ProductController`).

### 엔드포인트 규칙
- REST API 표준을 따릅니다:
  - GET: 리소스 조회
  - POST: 리소스 생성
  - PUT: 리소스 전체 수정
  - PATCH: 리소스 부분 수정
  - DELETE: 리소스 삭제
- URL 경로는 복수형 소문자로 명명합니다 (예: `/products`, `/orders`).
- 특정 리소스는 ID로 식별합니다 (예: `/products/{id}`).

## 8. 예외 처리 규칙

### 위치
- 모든 예외 클래스는 `com.mylittleshop.backend.exception` 패키지에 위치합니다.

### 명명 규칙
- 사용자 정의 예외 클래스는 문제 상황 + "Exception"으로 명명합니다 (예: `ResourceNotFoundException`).

### 처리 규칙
- 예외는 `@ControllerAdvice`를 사용하여 글로벌하게 처리합니다.
- 각 예외 유형에 따라 적절한 HTTP 상태 코드를 반환합니다.
- 예외 응답은 일관된 형식을 유지합니다 (timestamp, message, details 등).

## 9. 유틸리티 클래스 규칙

### 위치
- 모든 유틸리티 클래스는 `com.mylittleshop.backend.util` 패키지에 위치합니다.

### 명명 규칙
- 유틸리티 클래스 이름은 기능 + "Utils"로 명명합니다 (예: `DateUtils`, `StringUtils`).

### 구현 규칙
- 유틸리티 클래스는 정적 메소드만 포함하고, 인스턴스화를 방지하기 위해 private 생성자를 갖습니다.