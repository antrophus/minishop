## MyLittleShop 프로젝트 데이터베이스와 Java 엔티티 교차 검증 보고서

### 종합 요약

- **총 데이터베이스 테이블**: 43개
- **총 Java 엔티티 파일**: 42개

### 엔티티 유형 분류

- **테이블과 매핑되는 Entity 클래스**: 27개
- **임베디드 클래스(Embeddable)**: 1개 (Delivery.java)
- **열거형 상수 클래스(Enum)**: 11개 (BillingFrequency, CartStatus 등)
- **특별한 경우(불명확한 매핑 등)**: 3개 (ProductMedia, RecurringOrderItem, ReviewResponse)

### 매핑 분석 결과

#### DB에 있지만 Java 엔티티로 매핑되지 않은 테이블 (17개)

- audit_logs
- banner
- coupons
- deliveries
- events
- faq
- flyway_schema_history
- inventory_history
- notifications
- point_transactions
- points
- product_promotion
- product_questions
- role_permissions
- terms
- user_coupons
- user_roles

#### 특별한 케이스

- **RecurringOrder.java와 Subscription.java**: 두 엔티티 모두 구독 관련 기능을 담당하지만, DB에는 'subscriptions' 테이블만 존재합니다. RecurringOrder.java에는 '@Table(name = "recurring_orders")' 어노테이션이 있는데, 이 테이블은 데이터베이스에 존재하지 않습니다.

### 불일치 원인 분석

1. **데이터베이스 마이그레이션 도구 사용**: flyway_schema_history 테이블이 존재하는 것으로 보아 Flyway와 같은 데이터베이스 마이그레이션 도구를 사용하고 있습니다. 이는 테이블 구조가 코드보다 먼저 변경되었을 가능성을 시사합니다.
    
2. **스크립트 기반 테이블 생성**: 일부 테이블은 JPA 엔티티 자동 생성이 아닌 SQL 스크립트로 직접 생성되었을 가능성이 있습니다. 이런 경우 테이블에 해당하는 엔티티 클래스가 없을 수 있습니다.
    
3. **관리 기능용 테이블**: audit_logs, banner, notifications와 같은 일부 테이블은 관리 기능을 위해 존재하지만, 해당 엔티티는 검사한 경로에 없을 수 있습니다. 다른 패키지에 정의되어 있을 가능성이 있습니다.
    
4. **코드 리팩토링 진행 중**: RecurringOrder와 Subscription 엔티티는 비슷한 기능을 하지만 데이터베이스에는 subscriptions 테이블만 있는 것으로 보아, 코드 리팩토링이 진행 중이거나 하나의 구현만 활성화되어 있을 수 있습니다.
    
5. **복합 키 또는 연관 테이블**: user_roles, role_permissions, product_promotion과 같은 테이블은 JPA의 @ManyToMany 연관관계를 통해 자동 생성된 연결 테이블일 수 있습니다. 이 경우 별도의 엔티티 클래스가 없어도 됩니다.
    

### 권장 사항

1. **누락된 엔티티 추가**: DB에는 있지만 Java 엔티티로 매핑되지 않은 17개 테이블에 대해 엔티티 클래스 추가를 고려하세요.
    
2. **중복 엔티티 정리**: RecurringOrder와 Subscription과 같이 기능이 중복되는 엔티티는 하나로 통합하는 것이 좋습니다.
    
3. **명명 규칙 통일**: 일부 엔티티의 테이블 이름 매핑이 실제 DB의 테이블 이름과 다릅니다 (예: DeliveryAddress.java -> delivery_addresses). 명명 규칙을 통일하는 것이 유지보수에 도움이 됩니다.
    
4. **코드와 DB 동기화**: Flyway와 같은 마이그레이션 도구를 사용하는 경우, 코드의 엔티티 정의와 DB 스키마가 동기화되도록 관리하는 것이 중요합니다.
    
5. **주석 및 문서화**: 특별한 경우의 엔티티(예: 임베디드 클래스, 다른 엔티티로 통합된 클래스)에 대해서는 명확한 주석을 추가하여 다른 개발자들이 이해하기 쉽게 만드는 것이 좋습니다.
    

이 분석은 제공된 정보를 기반으로 한 것으로, 프로젝트의 전체 구조와 설계 의도에 따라 실제 상황은 다를 수 있습니다. 정확한 분석을 위해서는 추가적인 코드 검토와, 가능하다면 프로젝트의 개발 히스토리를 확인하는 것이 도움이 될 것입니다.