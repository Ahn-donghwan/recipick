# Member 서비스 헥사고날 아키텍처 구현 체크리스트

이 문서는 `backend/member` 서비스를 헥사고날 아키텍처로 구현하기 위한 작업 순서와 완료 기준을 정리한다.
처음부터 모든 클래스를 만들지 않고, 회원가입 유스케이스를 도메인부터 API까지 완성하면서 필요한 구조를 단계적으로 추가한다.

## 1. 기본 원칙

- [ ] 모든 명령은 `backend/member`에서 실행한다.
- [ ] 기본 검증 명령은 `./gradlew test`를 사용한다.
- [ ] `domain`은 Spring, JPA, Web 기술에 의존하지 않는다.
- [ ] `application`은 유스케이스를 조율하고 `domain`과 port에만 의존한다.
- [ ] inbound adapter는 inbound port를 통해 애플리케이션을 호출한다.
- [ ] outbound adapter는 outbound port를 구현한다.
- [ ] Controller가 JPA Repository 또는 persistence adapter를 직접 호출하지 않는다.
- [ ] API DTO, 도메인 모델, JPA Entity를 분리한다.
- [ ] 원문 비밀번호, 비밀번호 해시, 인증 토큰과 개인정보를 로그에 남기지 않는다.
- [ ] Auth와 Member는 하나의 `member` 마이크로서비스에서 운영하되 유스케이스와 Web API 책임을 구분한다.

의존성 방향은 다음 흐름을 따른다.

```text
HTTP 요청
  -> adapter.in.web
  -> application.port.in
  -> application.service
  -> domain
  -> application.port.out
  -> adapter.out.persistence 또는 adapter.out.security
  -> MySQL 또는 보안 라이브러리
```

## 2. 목표 패키지 구조

기본 구조는 다음과 같이 시작하고, 실제 유스케이스에 필요한 파일만 추가한다.

```text
com.recipick.member
├── domain
│   ├── model
│   │   ├── Member.java
│   │   ├── Gender.java
│   │   ├── MemberRole.java
│   │   └── MemberStatus.java
│   ├── policy
│   │   └── PasswordPolicy.java
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
│   │       ├── dto
│   │       └── mapper
│   └── out
│       ├── persistence
│       │   ├── MemberJpaEntity.java
│       │   ├── MemberJpaRepository.java
│       │   ├── MemberPersistenceAdapter.java
│       │   └── MemberPersistenceMapper.java
│       └── security
└── config
```

빈 패키지를 유지하기 위한 파일은 만들지 않는다. 각 패키지는 실제 구현 클래스가 필요할 때 생성한다.

## 3. 0단계: 회원 정책 확정

### 식별자

- [ ] 내부 PK는 `Long`을 사용한다.
- [ ] 외부 API에는 내부 PK를 노출하지 않는다.
- [ ] 외부 식별자는 `UUID`를 사용한다.
- [ ] UUID 생성 책임을 정한다.
- [ ] UUID DB 저장 방식을 `CHAR(36)` 또는 `BINARY(16)` 중 선택한다.
- [ ] UUID에 `UNIQUE`, `NOT NULL` 제약을 적용한다.

### enum과 상태 전이

- [ ] `Gender` 값을 확정한다.
- [ ] `MemberRole` 값을 확정한다.
- [ ] `MemberStatus` 값을 확정한다.
- [ ] 신규 회원의 기본 권한을 확정한다.
- [ ] 신규 회원의 기본 상태를 확정한다.
- [ ] `ACTIVE`, `SUSPENDED`, `WITHDRAWN` 사이의 허용 상태 전이를 정의한다.
- [ ] enum은 ordinal이 아닌 문자열 또는 명시적인 코드로 저장한다.

### 회원 정보 정책

- [ ] 이메일을 소문자로 정규화할지 정한다.
- [ ] 휴대폰 번호 저장 형식을 정한다.
- [ ] 닉네임 길이와 허용 문자를 정한다.
- [ ] 회원 이름 길이와 허용 문자를 정한다.
- [ ] 가입 가능한 생년월일 또는 최소 연령 정책을 정한다.
- [ ] 이메일, 휴대폰, 닉네임 중복 정책을 정한다.
- [ ] 탈퇴 회원의 개인정보 보존 및 재가입 정책을 정한다.

### 비밀번호 정책

- [ ] 비밀번호를 8~20자로 제한한다.
- [ ] 영문 대문자 포함 여부를 검사한다.
- [ ] 숫자 포함 여부를 검사한다.
- [ ] 특수문자 포함 여부를 검사한다.
- [ ] 원문 비밀번호와 해시된 비밀번호의 변수명을 구분한다.
- [ ] DB에는 단방향 해시된 비밀번호만 저장한다.
- [ ] 비밀번호 컬럼은 해시값을 저장할 수 있도록 `VARCHAR(255)`를 사용한다.

### 완료 조건

- [ ] 신규 회원의 권한과 상태를 설명할 수 있다.
- [ ] 회원 상태별 허용 동작을 설명할 수 있다.
- [ ] 중복 이메일 가입 시 처리 방식을 설명할 수 있다.
- [ ] 외부에 노출하는 식별자와 내부 PK의 차이를 설명할 수 있다.

## 4. 1단계: 순수 도메인 구현

### 구현

- [ ] `domain.model.Gender`를 구현한다.
- [ ] `domain.model.MemberRole`을 구현한다.
- [ ] `domain.model.MemberStatus`를 구현한다.
- [ ] `domain.policy.PasswordPolicy`를 구현한다.
- [ ] `domain.model.Member`를 구현한다.
- [ ] 필요한 도메인 예외를 `domain.exception`에 정의한다.
- [ ] 신규 회원 생성을 의미하는 정적 팩토리 메서드를 검토한다.
- [ ] 회원 상태 변경을 의미 있는 도메인 메서드로 표현한다.
- [ ] `Member`에는 원문 비밀번호가 아닌 해시된 비밀번호만 보관한다.

### 도메인 테스트

- [ ] 정상 입력으로 신규 회원을 생성할 수 있다.
- [ ] 신규 회원의 기본 권한이 올바르다.
- [ ] 신규 회원의 기본 상태가 올바르다.
- [ ] 필수 회원 정보가 없으면 생성할 수 없다.
- [ ] 비밀번호 정책의 각 조건을 검증한다.
- [ ] 허용된 회원 상태 전이가 성공한다.
- [ ] 허용되지 않은 회원 상태 전이가 거부된다.
- [ ] 도메인 테스트가 Spring 실행 없이 동작한다.

### 완료 조건

- [ ] `domain` 패키지에 Spring import가 없다.
- [ ] `domain` 패키지에 JPA import가 없다.
- [ ] `Member`에 `@Entity`, `@Table`, `@Column`이 없다.
- [ ] 도메인의 핵심 정책이 테스트로 표현되어 있다.

## 5. 2단계: 회원가입 인바운드 포트 정의

다음 파일 구성을 기준으로 실제 이름을 확정한다.

```text
application.port.in
├── RegisterMemberUseCase.java
├── RegisterMemberCommand.java
└── RegisterMemberResult.java
```

- [ ] `RegisterMemberUseCase`를 정의한다.
- [ ] 회원가입 입력값을 담는 `RegisterMemberCommand`를 정의한다.
- [ ] 회원가입 결과를 담는 `RegisterMemberResult`를 정의한다.
- [ ] 결과에 내부 PK를 포함하지 않는다.
- [ ] 결과에 원문 또는 해시된 비밀번호를 포함하지 않는다.
- [ ] inbound port에서 HTTP 및 Servlet 타입을 사용하지 않는다.
- [ ] inbound port에서 Web DTO와 JPA Entity를 사용하지 않는다.

### 완료 조건

- [ ] 회원가입 기능이 기술과 무관한 인터페이스로 표현되어 있다.
- [ ] HTTP가 아닌 입력 방식에서도 같은 유스케이스를 호출할 수 있다.

## 6. 3단계: 아웃바운드 포트 정의

```text
application.port.out
├── MemberRepository.java
└── PasswordEncoderPort.java
```

- [ ] `MemberRepository`를 정의한다.
- [ ] 이메일 중복 확인 기능을 정의한다.
- [ ] 휴대폰 중복 확인 기능을 정의한다.
- [ ] 닉네임 중복 확인 기능을 정의한다.
- [ ] 회원 저장 기능을 정의한다.
- [ ] `PasswordEncoderPort`를 정의한다.
- [ ] 비밀번호 해시 생성 기능을 정의한다.
- [ ] 원문 비밀번호 일치 검사 기능을 정의한다.
- [ ] 현재 유스케이스에서 사용하지 않는 저장소 메서드를 미리 추가하지 않는다.

### 완료 조건

- [ ] `MemberRepository`가 `JpaRepository`를 상속하지 않는다.
- [ ] 포트에 Spring Data JPA 타입이 없다.
- [ ] 포트에 BCrypt 등 특정 보안 라이브러리 타입이 없다.

## 7. 4단계: 회원가입 애플리케이션 서비스 구현

- [ ] `application.service.RegisterMemberService`를 구현한다.
- [ ] `RegisterMemberUseCase`를 구현한다.
- [ ] 입력값 정규화 위치를 확정한다.
- [ ] 비밀번호 정책을 검사한다.
- [ ] 이메일 중복을 검사한다.
- [ ] 휴대폰 중복을 검사한다.
- [ ] 닉네임 중복을 검사한다.
- [ ] `PasswordEncoderPort`로 비밀번호를 해시한다.
- [ ] 순수 도메인 `Member`를 생성한다.
- [ ] `MemberRepository`로 회원을 저장한다.
- [ ] 저장 결과를 `RegisterMemberResult`로 변환한다.
- [ ] 필요한 트랜잭션 경계를 설정한다.

### 애플리케이션 테스트

- [ ] 정상적인 회원가입이 성공한다.
- [ ] 이메일 중복 시 가입이 거부된다.
- [ ] 휴대폰 중복 시 가입이 거부된다.
- [ ] 닉네임 중복 시 가입이 거부된다.
- [ ] 저장 전에 비밀번호가 해시된다.
- [ ] 저장소에 원문 비밀번호가 전달되지 않는다.
- [ ] DB 없이 port의 fake 또는 mock 구현으로 테스트할 수 있다.

### 완료 조건

- [ ] 서비스가 persistence 구현체가 아닌 outbound port에만 의존한다.
- [ ] 서비스가 Web DTO 또는 JPA Entity를 참조하지 않는다.
- [ ] 회원가입 흐름이 애플리케이션 서비스에서 명확하게 읽힌다.

## 8. 5단계: JPA 영속성 어댑터 구현

- [ ] `MemberJpaEntity`를 구현한다.
- [ ] 내부 PK에 `@Id`와 생성 전략을 설정한다.
- [ ] UUID, 이메일, 휴대폰, 닉네임에 DB UNIQUE 제약을 설정한다.
- [ ] 필수 필드에 `nullable = false`를 설정한다.
- [ ] enum을 `EnumType.STRING`으로 저장한다.
- [ ] 생년월일을 `LocalDate`로 매핑한다.
- [ ] 생성일시와 수정일시 타입 및 생성 방식을 결정한다.
- [ ] `MemberJpaRepository`를 구현한다.
- [ ] `MemberPersistenceMapper`를 구현한다.
- [ ] `Member`를 `MemberJpaEntity`로 변환한다.
- [ ] `MemberJpaEntity`를 `Member`로 복원한다.
- [ ] `MemberPersistenceAdapter`가 `MemberRepository`를 구현하게 한다.
- [ ] 애플리케이션의 사전 중복 검사와 DB UNIQUE 제약을 함께 적용한다.

### 영속성 테스트

- [ ] 회원을 저장하고 조회할 수 있다.
- [ ] 도메인과 JPA Entity 사이의 변환에서 값이 유실되지 않는다.
- [ ] enum이 문자열로 저장된다.
- [ ] UUID 중복 저장이 거부된다.
- [ ] 이메일 중복 저장이 거부된다.
- [ ] 휴대폰 중복 저장이 거부된다.
- [ ] 닉네임 중복 저장이 거부된다.
- [ ] H2 또는 Testcontainers 기반 테스트 datasource를 결정한다.
- [ ] 로컬 MySQL 설치 여부와 무관하게 테스트할 수 있다.

### 완료 조건

- [ ] JPA 관련 구현이 `adapter.out.persistence` 내부에 있다.
- [ ] application과 domain이 JPA 클래스를 참조하지 않는다.
- [ ] DB UNIQUE 제약이 동시 요청의 중복 저장을 최종 방어한다.

## 9. 6단계: 비밀번호 암호화 어댑터 구현

```text
adapter.out.security
└── BCryptPasswordEncoderAdapter.java
```

- [ ] 필요한 Spring Security 암호화 의존성을 추가한다.
- [ ] `PasswordEncoderPort` 구현체를 작성한다.
- [ ] BCrypt 설정값을 검토한다.
- [ ] 해시 생성 테스트를 작성한다.
- [ ] 원문과 해시값 일치 검사 테스트를 작성한다.
- [ ] 원문 비밀번호와 해시값을 로그에 남기지 않는다.

### 완료 조건

- [ ] application이 BCrypt 구현 클래스를 직접 참조하지 않는다.
- [ ] 보안 라이브러리 교체가 outbound adapter 변경으로 제한된다.

## 10. 7단계: 회원가입 Web 어댑터 구현

```text
adapter.in.web
├── controller
│   └── MemberController.java
├── dto
│   ├── MemberRegisterReqDto.java
│   └── MemberRegisterResDto.java
└── mapper
    └── MemberWebMapper.java
```

- [ ] Validation 의존성을 확인하고 필요한 경우 추가한다.
- [ ] `MemberRegisterReqDto`를 구현한다.
- [ ] 문자열 필드에 `@NotBlank` 사용을 검토한다.
- [ ] enum과 객체 필드에 `@NotNull` 사용을 검토한다.
- [ ] 이메일에 `@Email`을 적용한다.
- [ ] 비밀번호 정책을 표현하는 validation을 적용한다.
- [ ] 요청 DTO를 `RegisterMemberCommand`로 변환한다.
- [ ] `MemberController`가 `RegisterMemberUseCase`를 호출하게 한다.
- [ ] 결과를 `MemberRegisterResDto`로 변환한다.
- [ ] 상위 프로젝트의 공통 응답 규약을 적용한다.
- [ ] Swagger-visible 요청, 응답과 validation 정보를 확인한다.

### Web 테스트

- [ ] 정상 요청이 성공한다.
- [ ] 필수값 누락 시 400 응답을 반환한다.
- [ ] 이메일 형식 오류 시 400 응답을 반환한다.
- [ ] 비밀번호 정책 위반 시 400 응답을 반환한다.
- [ ] 중복 회원 정보에 정의된 오류 응답을 반환한다.
- [ ] 응답에 내부 PK가 노출되지 않는다.
- [ ] 응답에 원문 또는 해시된 비밀번호가 노출되지 않는다.

### 완료 조건

- [ ] Controller가 inbound port만 호출한다.
- [ ] Controller가 JPA Repository를 직접 호출하지 않는다.
- [ ] Web DTO와 application command가 분리되어 있다.
- [ ] JPA Entity를 API 요청 또는 응답으로 사용하지 않는다.

## 11. 8단계: 예외와 API 오류 처리

- [ ] 잘못된 도메인 상태에 대한 예외를 정의한다.
- [ ] 이메일 중복 예외를 정의한다.
- [ ] 휴대폰 중복 예외를 정의한다.
- [ ] 닉네임 중복 예외를 정의한다.
- [ ] DB UNIQUE 제약 위반을 의미 있는 애플리케이션 오류로 변환한다.
- [ ] 예외와 HTTP 상태 및 공통 오류 코드를 연결한다.
- [ ] validation 오류 응답 형식을 통일한다.
- [ ] 예외 메시지에 비밀번호와 개인정보가 포함되지 않는지 확인한다.
- [ ] Swagger 오류 응답 문서 영향을 확인한다.

## 12. 9단계: Auth 기능 확장

Auth는 별도 마이크로서비스로 분리하지 않고 같은 `member` 서비스 안에서 책임을 구분한다.

- [ ] 회원가입 기능을 먼저 완성한다.
- [ ] `LoginUseCase`와 로그인 command/result를 정의한다.
- [ ] 로그인 애플리케이션 서비스를 구현한다.
- [ ] `PasswordEncoderPort.matches`로 비밀번호를 확인한다.
- [ ] `TokenProviderPort`를 정의한다.
- [ ] JWT 구현을 `adapter.out.security`에 둔다.
- [ ] `AuthController`를 `adapter.in.web.controller`에 둔다.
- [ ] 토큰 갱신 정책을 정의한다.
- [ ] 로그아웃 및 refresh token 저장 정책을 정의한다.
- [ ] 인증 응답에 민감한 회원 정보가 포함되지 않는지 확인한다.

다음 요구가 실제로 발생하기 전에는 Auth를 별도 마이크로서비스로 분리하지 않는다.

- 여러 서비스가 독립 인증 서버를 공통으로 사용해야 한다.
- OAuth2/OIDC 인증 서버 역할이 필요하다.
- 소셜 로그인, MFA, 기기 관리 등 인증 기능이 크게 복잡해진다.
- 인증 기능을 Member와 독립적으로 배포하거나 확장해야 한다.

## 13. 10단계: DB 마이그레이션

- [ ] Flyway 또는 Liquibase 사용 여부를 결정한다.
- [ ] 회원 테이블 생성 migration을 작성한다.
- [ ] PK, UNIQUE, NOT NULL 제약을 migration에 반영한다.
- [ ] 제약 조건 이름을 명시적으로 정한다.
- [ ] enum 컬럼 길이를 충분히 설정한다.
- [ ] 생성일시와 수정일시 정밀도를 설정한다.
- [ ] 운영 환경의 `ddl-auto` 정책을 확인한다.
- [ ] JPA 매핑과 실제 migration 스키마가 일치하는지 검증한다.

## 14. 최종 검증

- [ ] `./gradlew test`가 통과한다.
- [ ] `domain`에 Spring, JPA, Web 의존성이 없다.
- [ ] `application`에 Web 또는 JPA 구현 의존성이 없다.
- [ ] adapter 사이에 직접 의존성이 없다.
- [ ] 계층 사이에 순환 의존성이 없다.
- [ ] Controller가 inbound port만 호출한다.
- [ ] persistence adapter가 outbound port를 구현한다.
- [ ] 원문 비밀번호가 DB, 응답 또는 로그에 남지 않는다.
- [ ] 내부 회원 PK가 외부 API에 노출되지 않는다.
- [ ] UUID, 이메일, 휴대폰과 닉네임 중복이 최종적으로 DB에서 방어된다.
- [ ] 테스트가 개인 로컬 MySQL 설정에 의존하지 않는다.
- [ ] Swagger 요청, 응답, validation과 오류 문서를 확인한다.
- [ ] DB 비밀번호와 JWT secret이 Git에 포함되지 않았다.

## 15. 권장 작업 및 커밋 순서

한 체크포인트를 구현하고 테스트한 뒤 목적별로 커밋한다.

1. [ ] enum과 회원 정책 확정
   - `docs(member): 회원 도메인 정책 정의`
2. [ ] 순수 회원 도메인과 테스트 구현
   - `feat(member): 회원 도메인 모델과 정책 추가`
3. [ ] 회원가입 inbound port 정의
   - `feat(member): 회원 가입 인바운드 포트 정의`
4. [ ] 저장소와 암호화 outbound port 정의
   - `feat(member): 회원 저장소와 비밀번호 암호화 포트 정의`
5. [ ] 회원가입 application service와 테스트 구현
   - `feat(member): 회원 가입 유스케이스 구현`
6. [ ] JPA persistence adapter와 테스트 구현
   - `feat(member): 회원 JPA 영속성 어댑터 구현`
7. [ ] BCrypt security adapter 구현
   - `feat(member): BCrypt 비밀번호 암호화 어댑터 추가`
8. [ ] 회원가입 Web API와 테스트 구현
   - `feat(member): 회원 가입 API 추가`
9. [ ] 회원가입 예외 처리 구현
   - `feat(member): 회원 가입 예외 처리 추가`
10. [ ] DB migration 구성 및 스키마 추가
    - `build(member): 데이터베이스 마이그레이션 환경 구성`
    - `feat(member): 회원 테이블 마이그레이션 추가`
11. [ ] 회원가입 통합 테스트 구현
    - `test(member): 회원 가입 통합 테스트 추가`
12. [ ] 로그인과 JWT 기능을 별도 작업 단위로 확장
    - 구현 범위에 따라 `feat(member): ...` 형식으로 작성

## 16. 현재 시작점

가장 먼저 다음 항목만 진행한다.

- [ ] `Gender` 값 확정
- [ ] `MemberRole` 값과 기본 권한 확정
- [ ] `MemberStatus` 값과 상태 전이 확정
- [ ] 이메일, 휴대폰, 닉네임 정규화 및 중복 정책 확정
- [ ] 비밀번호 정책 확정
- [ ] 정책 결정 내용을 검토한 뒤 첫 도메인 구현을 시작

이 단계가 끝나기 전에는 Controller, JPA Entity, JWT 구현을 먼저 만들지 않는다.
