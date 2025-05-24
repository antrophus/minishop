# MyLittleShop 백엔드 데이터베이스 구성

## 개요

MyLittleShop 백엔드는 Spring Boot 기반으로 MySQL 데이터베이스를 사용하며, 환경별로 최적화된 구성을 제공합니다. 데이터베이스 스키마 관리를 위해 Flyway를 사용하고, 연결 풀은 HikariCP를 통해 관리됩니다.

## 환경별 설정

### 공통 설정 (application.yml)
- 기본 데이터베이스 드라이버 및 공통 설정
- HikariCP 연결 풀 설정
- JPA 공통 설정
- Flyway 마이그레이션 기본 설정

### 개발 환경 (application-dev.yml)
- 로컬 MySQL 데이터베이스 연결 설정
- 자세한 SQL 로깅 활성화
- Hibernate DDL 자동 생성 모드: `update`
- Actuator 엔드포인트 노출 (개발용)

### 테스트 환경 (application-test.yml)
- H2 인메모리 데이터베이스 사용 (MySQL 호환 모드)
- Hibernate DDL 자동 생성 모드: `create-drop`
- Flyway 비활성화 (테스트를 위한 스키마 자동 생성)
- H2 콘솔 활성화

### 운영 환경 (application-prod.yml)
- 환경 변수 기반 데이터베이스 연결 설정
- 보안 강화 설정 (SSL 활성화 등)
- Hibernate DDL 자동 생성 비활성화 (스키마 변경 방지)
- 최적화된 HikariCP 연결 풀 설정
- 로깅 최소화 및 파일 로깅 구성

## 데이터베이스 마이그레이션

데이터베이스 스키마 변경은 Flyway 마이그레이션을 통해 관리됩니다. 모든 마이그레이션 스크립트는 `src/main/resources/db/migration` 디렉토리에 저장되며, 다음 명명 규칙을 따릅니다:

- `V{버전}__{설명}.sql` 형식 사용 (예: `V1__init.sql`, `V2__add_user_preferences.sql`)
- 한 번 적용된 마이그레이션은 수정하지 않음
- 변경이 필요한 경우 새로운 마이그레이션 스크립트 생성

## 연결 풀 설정 (HikariCP)

HikariCP 연결 풀 설정은 환경별로 최적화되어 있습니다:

- 개발 환경: 최대 10개 연결
- 테스트 환경: 최대 5개 연결
- 운영 환경: 최대 20개 연결, 연결 타임아웃 최적화

## 프로필 전환 방법

애플리케이션 실행 시 다음과 같이 활성 프로필을 지정할 수 있습니다:

```bash
# 개발 환경 (기본값)
./gradlew bootRun

# 테스트 환경
./gradlew bootRun --args='--spring.profiles.active=test'

# 운영 환경
./gradlew bootRun --args='--spring.profiles.active=prod'
```

또는 JAR 파일 실행 시:

```bash
java -jar backend.jar --spring.profiles.active=prod
``` 