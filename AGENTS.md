# 백엔드 작업 보충 규칙

루트 `AGENTS.md`가 프로젝트 전체 기준이다. 이 문서는 `backend` 디렉터리 안에서 Spring Boot 코드를 수정할 때만 적용하는 보충 규칙이다.

## 1. 명령 실행 위치

- 백엔드 명령은 `backend` 디렉터리에서 실행한다.
- 기본 검증은 `./gradlew test`다.
- 로컬 실행은 MySQL과 `JWT_SECRET_KEY`가 준비된 상태에서 `./gradlew bootRun`으로 한다.
- `build`, `.gradle`은 Gradle 산출물/캐시이므로 직접 수정하지 않는다.

## 2. 패키지 경계

- `member`: 회원 controller, service, repository, dto, entity, enum, utils
- `post`: 게시글 entity, repository, service
- `batch.member`: 회원 대량 생성 배치 job, reader, processor, writer, property
- `common`: config, security, exception, response, properties, 공통 entity

새 기능은 우선 도메인 패키지 아래에 배치한다. 여러 도메인에서 실제로 공유되는 코드만 `common`으로 이동한다.

## 3. 구현 스타일

- Java 17과 Spring Boot 4.1.0 기준을 따른다.
- 클래스와 enum은 PascalCase, 메서드와 필드는 camelCase, 상수는 UPPER_SNAKE_CASE를 사용한다.
- 패키지명은 소문자로 유지한다.
- Spring bean은 생성자 주입을 우선한다.
- DTO 이름은 요청/응답 방향이 드러나게 유지한다. 예: `MemberCreateReqDto`, `MemberCreateResDto`
- Lombok은 기존 사용 패턴과 맞을 때만 사용한다.

## 4. API와 Swagger

- controller 변경 시 request DTO, response DTO, validation annotation, 공통 응답 래핑, 예외 코드를 함께 확인한다.
- Swagger-visible 변경은 문서 영향까지 같이 본다.
- validation은 타입에 맞게 선택한다. 문자열은 `@NotBlank`, enum/object는 `@NotNull`을 우선 검토한다.
- 공통 응답은 `common.response` 규약과 맞춘다.

## 5. 보안과 설정

- JWT secret, DB 비밀번호, 개인 로컬 설정을 새로 커밋하지 않는다.
- `application.yml`의 로컬 DB/JWT 설정을 바꾸면 실행 조건과 테스트 영향도 같이 적는다.
- 인증/인가 변경 시 `SecurityConfig`, `JwtProvider`, `JwtAuthenticationFilter`, `CustomUserDetailsService`를 함께 확인한다.
- 현재 접근 제어가 개발 편의 설정일 수 있으므로 `permitAll` 변경은 API 영향 범위를 확인한 뒤 한다.

## 6. 테스트 기준

- 테스트는 `src/test/java` 아래 main 패키지 구조를 따른다.
- context 확인은 `@SpringBootTest`, 좁은 범위는 service/repository/controller 단위 테스트를 우선한다.
- DB가 필요한 테스트를 추가할 때는 로컬 MySQL 의존성을 만들지 않도록 test profile 또는 대체 datasource 필요성을 먼저 검토한다.
- 백엔드 코드 변경 후에는 가능한 한 `./gradlew test` 결과를 확인한다.
