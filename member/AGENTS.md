# Member 서비스 작업 보충 규칙

상위 `backend/AGENTS.md`의 공통 백엔드 규칙을 따른다. 이 문서는 독립 Gradle/Spring Boot 프로젝트인 `backend/member`에서 작업할 때 필요한 규칙만 보충하며, 상위 문서와 충돌하면 이 문서의 서비스별 규칙을 우선한다.

## 1. 서비스와 명령 실행 범위

- `member`는 독립적으로 빌드하고 배포하는 마이크로서비스다.
- 모든 Gradle 명령은 `backend/member` 디렉터리에서 실행한다.
- 기본 검증 명령은 `./gradlew test`다.
- `build`, `.gradle` 등 생성된 산출물과 캐시는 직접 수정하지 않는다.

## 2. 권장 아키텍처

기본 패키지는 `com.recipick.member`이며 다음 헥사고날 구조를 따른다.

```text
com.recipick.member
├── domain
│   ├── model
│   │   ├── Member.java
│   │   ├── Gender.java
│   │   ├── MemberRole.java
│   │   └── MemberStatus.java
│   └── exception
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
├── adapter
│   ├── in
│   │   └── web
│   │       ├── controller
│   │       └── dto
│   └── out
│       └── persistence
│           ├── MemberJpaEntity.java
│           ├── MemberJpaRepository.java
│           └── MemberPersistenceAdapter.java
└── config
```

의존성 방향은 다음과 같이 제한한다.

- `adapter.in`은 `application`을 거쳐 `domain`을 사용한다.
- `adapter.out`은 `application`의 아웃바운드 포트를 구현하며 필요한 `domain` 타입을 사용한다.
- `application`은 유스케이스를 조율하고 `domain`에 의존한다.
- `domain`은 Spring, JPA, Web 및 어댑터 구현에 의존하지 않는다.
- 계층 간 순환 의존성과 어댑터 간 직접 의존을 만들지 않는다.

## 3. 포트와 어댑터 규칙

- 인바운드 포트는 애플리케이션이 제공하는 유스케이스 인터페이스로 정의한다.
- 컨트롤러는 유스케이스를 직접 구현하거나 영속성 계층을 호출하지 않고 인바운드 포트를 호출한다.
- 아웃바운드 포트는 저장소와 외부 시스템 연동 인터페이스로 정의한다.
- 다른 마이크로서비스 연동은 아웃바운드 포트로 추상화한다.
- `MemberRepository`는 `application.port.out`의 아웃바운드 포트로 둔다.
- `MemberJpaRepository`와 JPA 관련 구현은 `adapter.out.persistence` 내부에 둔다.
- `MemberPersistenceAdapter`가 `MemberRepository`를 구현하고 도메인 모델과 JPA 모델을 변환한다.
- 마이크로서비스 간 DB 테이블, JPA Entity 또는 persistence repository를 공유하지 않는다.

## 4. 모델 분리

- `Member`는 영속성 애너테이션이 없는 순수 도메인 모델로 유지한다.
- `MemberJpaEntity`는 DB 저장을 위한 JPA 모델로 유지한다.
- API 요청·응답 DTO, 도메인 모델, JPA Entity를 서로 분리하고 계층 경계에서 명시적으로 변환한다.
- API나 DB의 제약을 도메인 모델에 무분별하게 노출하지 않는다.

회원 스키마를 작성하기 전에 각 필드의 null 허용 여부와 enum 저장 방식을 확정한다. 기본적으로 다음 정의를 권장한다.

| 필드 | 권장 타입 및 제약 |
| --- | --- |
| 회원 ID | `BIGINT`, PK, AUTO_INCREMENT |
| 회원 UUID | `BINARY(16)` 또는 `CHAR(36)`, UNIQUE, NOT NULL |
| 이메일 | `VARCHAR(255)`, UNIQUE, NOT NULL |
| 비밀번호 | `VARCHAR(255)`, NOT NULL |
| 휴대폰 | `VARCHAR(20)`, UNIQUE, NOT NULL |
| 닉네임 | `VARCHAR(50)`, UNIQUE, NOT NULL |
| 회원 명 | `VARCHAR(50)`, NOT NULL |
| 생년월일 | `DATE`, NOT NULL |
| 성별 | `Gender` enum 또는 안정적인 문자열 코드 |
| 권한 | `MemberRole` enum |
| 상태 | `MemberStatus` enum |
| 생성일시 | `DATETIME(6)`, NOT NULL |
| 수정일시 | `DATETIME(6)`, NOT NULL |

- UUID, 이메일, 휴대폰, 닉네임 등 필수 회원 정보에 `NonNull`과 `NULL` 정의를 혼용하지 않는다.
- enum을 DB에 저장할 때 ordinal 값에 의존하지 않고 변경에 안전한 문자열 또는 명시적 코드를 사용한다.
- 비밀번호의 길이·대문자·숫자·특수문자 정책은 회원가입 요청 DTO와 도메인 정책에서 검증한다.
- DB에는 원문이 아닌 단방향 해시된 비밀번호를 저장하므로 비밀번호 컬럼은 일반적으로 `VARCHAR(255)`를 사용한다.

## 5. 보안

- 비밀번호 원문, 비밀번호 해시, 인증 토큰 및 개인정보를 로그에 남기지 않는다.
- 예외 메시지와 API 응답에 민감 정보가 포함되지 않도록 한다.
- 민감 정보의 암호화·해시 처리는 포트로 추상화해 도메인이 보안 라이브러리 구현에 직접 의존하지 않게 한다.

## 6. 구현 및 검증 순서

새 회원 모델과 영속성 기능은 가능한 한 다음 순서로 작업한다.

1. `Gender`, `MemberRole`, `MemberStatus`의 값과 전이 정책을 확정한다.
2. 순수 도메인 `Member`와 도메인 정책을 구현한다.
3. 인바운드·아웃바운드 포트를 정의하고 애플리케이션 서비스를 구현한다.
4. `MemberJpaEntity`와 도메인-JPA 매퍼를 구현한다.
5. `MemberJpaRepository`와 `MemberPersistenceAdapter`를 구현한다.
6. 도메인 단위 테스트와 JPA 매핑 테스트를 추가한다.
7. `backend/member`에서 `./gradlew test`로 검증한다.

## 7. 작업 완료 안내

- 코드나 문서를 변경한 작업을 완료할 때마다 현재 변경 범위에 맞는 추천 커밋 메시지를 함께 안내한다.
- 추천 커밋 메시지와 함께 해당 커밋에 포함되는 주요 변경 내용을 간단히 설명한다.
- 커밋 메시지는 저장소의 `COMMIT_CONVENTION.md`와 기존 커밋 스타일을 따른다.
