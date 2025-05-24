# 데이터베이스 마이그레이션 전략

이 문서는 MyLittleShop 프로젝트의 데이터베이스 마이그레이션 전략과 실행 프로세스를 설명합니다.

## 마이그레이션 도구: Flyway

MyLittleShop 프로젝트는 데이터베이스 스키마 버전 관리 및 마이그레이션을 위해 [Flyway](https://flywaydb.org/)를 사용합니다. Flyway는 SQL 기반의 마이그레이션 스크립트를 통해 데이터베이스 스키마 변경을 안전하게 관리하고 추적합니다.

## 마이그레이션 파일 네이밍 규칙

Flyway 마이그레이션 파일은 다음과 같은 네이밍 규칙을 따릅니다:

```
V<버전>__<설명>.sql
```

- `<버전>`: 마이그레이션 버전 번호 (예: 1, 2, 2.1 등)
- `<설명>`: 마이그레이션 내용에 대한 간략한 설명 (밑줄로 단어 구분)

예시:
- `V1__init.sql` - 초기 데이터베이스 스키마 생성
- `V2__entity_improvements.sql` - 엔티티 개선 사항 적용
- `V3__sample_data.sql` - 샘플 데이터 삽입

## 마이그레이션 실행 프로세스

### 자동 실행

Spring Boot 애플리케이션이 시작될 때 Flyway는 다음과 같은 과정으로 마이그레이션을 자동으로 실행합니다:

1. `flyway_schema_history` 테이블 확인 (없으면 생성)
2. 이전에 적용된 마이그레이션 버전 확인
3. 적용되지 않은 마이그레이션 파일 순서대로 실행
4. 각 마이그레이션 실행 결과를 `flyway_schema_history` 테이블에 기록

### 수동 실행

필요한 경우 Flyway 명령을 직접 실행할 수 있습니다:

```bash
# Maven을 사용하는 경우
./mvnw flyway:migrate
./mvnw flyway:info
./mvnw flyway:validate
./mvnw flyway:repair

# Gradle을 사용하는 경우
./gradlew flywayMigrate
./gradlew flywayInfo
./gradlew flywayValidate
./gradlew flywayRepair
```

## 환경별 구성

각 환경에 따라 Flyway 동작을 다르게 구성할 수 있습니다:

### 개발 환경

개발 환경(`application-dev.yml`)에서는 다음과 같이 구성됩니다:

```yaml
spring:
  flyway:
    enabled: true
  jpa:
    hibernate:
      ddl-auto: update
```

개발 환경에서는 Flyway를 사용하면서 Hibernate의 `ddl-auto: update` 옵션도 활성화되어 있습니다. 이는 개발 중 빠른 스키마 변경을 가능하게 하지만, 프로덕션 환경에서는 권장되지 않습니다.

### 테스트 환경

테스트 환경에서는 인메모리 데이터베이스를 초기화하기 위해 다음과 같이 구성하는 것이 좋습니다:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration,classpath:db/testdata
  jpa:
    hibernate:
      ddl-auto: validate
```

### 프로덕션 환경

프로덕션 환경에서는 다음과 같이 구성됩니다:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baselineOnMigrate: true
    validateOnMigrate: true
  jpa:
    hibernate:
      ddl-auto: validate
```

프로덕션 환경에서는 Hibernate의 스키마 자동 생성(`ddl-auto: update`)을 비활성화하고, Flyway를 통해서만 데이터베이스 변경을 관리합니다.

## 롤백 전략

Flyway는 자동 롤백 기능을 제공하지 않습니다. 따라서 롤백이 필요한 경우를 대비하여 다음과 같은 전략을 사용합니다:

1. 각 마이그레이션 스크립트에 대응하는 롤백 스크립트를 `V<버전>__rollback.sql` 형태로 같이 관리합니다.
2. 롤백이 필요한 경우, 다음 단계를 수행합니다:
   - `flyway_schema_history` 테이블에서 롤백할 마이그레이션 이후의 기록 삭제
   - 롤백 스크립트 수동 실행
   - `flyway repair` 명령 실행하여 테이블 정보 복구

예시:
```sql
-- V2 마이그레이션 롤백
DELETE FROM flyway_schema_history WHERE version > '2';
-- V2__rollback.sql 스크립트 수동 실행
-- flyway repair 명령 실행
```

## 마이그레이션 모범 사례

1. **원자성**: 각 마이그레이션 스크립트는 원자적이어야 합니다. 전체가 성공하거나 전체가 실패해야 합니다.
2. **반복성**: 각 마이그레이션 스크립트는 한 번만 적용되어야 합니다.
3. **독립성**: 마이그레이션 스크립트는 가능한 한 다른 스크립트와 독립적이어야 합니다.
4. **예외 처리**: 마이그레이션 스크립트에는 적절한 예외 처리가 포함되어야 합니다.
5. **버전 관리**: 모든 마이그레이션 스크립트는 버전 관리 시스템(Git)에 포함되어야 합니다.

## 마이그레이션 파일 설명

현재 프로젝트에는 다음과 같은 마이그레이션 파일이 있습니다:

1. **V1__init.sql**: 초기 데이터베이스 스키마 생성 (기본 테이블 및 관계 정의)
2. **V2__entity_improvements.sql**: 엔티티 개선 사항 적용
   - 기존 테이블의 컬럼 추가/수정
   - 가격 처리 관련 데이터 타입 변경 (INT → DECIMAL)
   - 인덱스 추가
   - 새로운 테이블 추가 (order_status_history, recurring_orders 등)
3. **V2__rollback.sql**: V2 마이그레이션 롤백 스크립트 (수동 실행)
4. **V3__sample_data.sql**: 개발 및 테스트용 샘플 데이터

## 새로운 마이그레이션 추가 방법

새로운 데이터베이스 변경이 필요할 때:

1. 새로운 마이그레이션 파일 생성: `V<다음버전>__<설명>.sql`
2. SQL 변경 스크립트 작성
3. 롤백 스크립트 작성: `V<다음버전>__rollback.sql`
4. 개발 환경에서 테스트
5. 변경 사항 버전 관리 시스템에 커밋

## 프로덕션 배포 시 고려사항

1. 배포 전 백업: 프로덕션 환경 마이그레이션 전에 항상 데이터베이스 백업을 수행
2. 다운타임 최소화: 큰 변경의 경우 서비스 다운타임을 계획
3. 성능 고려: 대용량 데이터 마이그레이션의 경우 배치 처리와 인덱스 처리 전략 고려
4. 롤백 계획: 문제 발생 시 롤백 절차 문서화

## 주의사항

1. **Hibernate DDL 자동 생성 vs Flyway**: 프로덕션 환경에서는 Hibernate의 `ddl-auto: update`를 사용하지 않고 Flyway만 사용합니다.
2. **샘플 데이터**: `V3__sample_data.sql`은 개발 및 테스트 환경에서만 실행되어야 합니다.
3. **롤백 스크립트**: 모든 마이그레이션에 대응하는 롤백 스크립트를 관리합니다.
4. **환경별 구성**: 환경에 따라 Flyway 구성을 적절히 조정합니다. 