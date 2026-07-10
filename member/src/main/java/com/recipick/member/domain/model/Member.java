package com.recipick.member.domain.model;

import com.recipick.member.domain.exception.InvalidMemberStateTransitionException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Member {

    private final Long id;
    private final UUID uuid;
    private final String email;
    private final String passwordHash;
    private final String phone;
    private final String nickname;
    private final String name;
    private final LocalDate birthDate;
    private final Gender gender;
    private final MemberRole role;
    private MemberStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 회원의 필수값과 시간 관계를 검증한 뒤 도메인 객체를 초기화한다.
    private Member(
            Long id,
            UUID uuid,
            String email,
            String passwordHash,
            String phone,
            String nickname,
            String name,
            LocalDate birthDate,
            Gender gender,
            MemberRole role,
            MemberStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.uuid = requireNonNull(uuid, "회원 UUID");
        this.email = requireText(email, "이메일");
        this.passwordHash = requireText(passwordHash, "비밀번호 해시");
        this.phone = requireText(phone, "휴대폰 번호");
        this.nickname = requireText(nickname, "닉네임");
        this.name = requireText(name, "회원 이름");
        this.birthDate = requireNonNull(birthDate, "생년월일");
        this.gender = requireNonNull(gender, "성별");
        this.role = requireNonNull(role, "회원 권한");
        this.status = requireNonNull(status, "회원 상태");
        this.createdAt = requireNonNull(createdAt, "생성일시");
        this.updatedAt = requireNonNull(updatedAt, "수정일시");

        if (updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("수정일시는 생성일시보다 이전일 수 없습니다.");
        }
    }

    // 신규 회원을 기본 권한 USER와 활성 상태 ACTIVE로 생성한다.
    public static Member create(
            UUID uuid,
            String email,
            String passwordHash,
            String phone,
            String nickname,
            String name,
            LocalDate birthDate,
            Gender gender,
            LocalDateTime createdAt
    ) {
        return new Member(
                null,
                uuid,
                email,
                passwordHash,
                phone,
                nickname,
                name,
                birthDate,
                gender,
                MemberRole.USER,
                MemberStatus.ACTIVE,
                createdAt,
                createdAt
        );
    }

    // 영속성 계층에서 조회한 기존 회원의 전체 상태를 도메인 객체로 복원한다.
    public static Member restore(
            Long id,
            UUID uuid,
            String email,
            String passwordHash,
            String phone,
            String nickname,
            String name,
            LocalDate birthDate,
            Gender gender,
            MemberRole role,
            MemberStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Member(
                requireNonNull(id, "회원 ID"),
                uuid,
                email,
                passwordHash,
                phone,
                nickname,
                name,
                birthDate,
                gender,
                role,
                status,
                createdAt,
                updatedAt
        );
    }

    // 활성 회원을 일시 정지 상태로 변경한다.
    public void suspend(LocalDateTime changedAt) {
        changeStatus(MemberStatus.SUSPENDED, changedAt);
    }

    // 일시 정지 회원을 다시 활성 상태로 변경한다.
    public void activate(LocalDateTime changedAt) {
        changeStatus(MemberStatus.ACTIVE, changedAt);
    }

    // 활성 또는 일시 정지 회원을 탈퇴 상태로 변경한다.
    public void withdraw(LocalDateTime changedAt) {
        changeStatus(MemberStatus.WITHDRAWN, changedAt);
    }

    // 현재 상태에서 목표 상태로의 전이 가능 여부와 변경일시를 검증한 뒤 상태를 변경한다.
    private void changeStatus(MemberStatus targetStatus, LocalDateTime changedAt) {
        requireNonNull(changedAt, "상태 변경일시");

        boolean allowed = switch (status) {
            case ACTIVE -> targetStatus == MemberStatus.SUSPENDED || targetStatus == MemberStatus.WITHDRAWN;
            case SUSPENDED -> targetStatus == MemberStatus.ACTIVE || targetStatus == MemberStatus.WITHDRAWN;
            case WITHDRAWN -> false;
        };

        if (!allowed) {
            throw new InvalidMemberStateTransitionException(status, targetStatus);
        }
        if (changedAt.isBefore(updatedAt)) {
            throw new IllegalArgumentException("상태 변경일시는 기존 수정일시보다 이전일 수 없습니다.");
        }

        status = targetStatus;
        updatedAt = changedAt;
    }

    // 필수 문자열이 null, 빈 문자열 또는 공백으로만 구성되지 않았는지 검증한다.
    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }

    // 필수 값이 null이 아닌지 검증한다.
    private static <T> T requireNonNull(T value, String fieldName) {
        return Objects.requireNonNull(value, fieldName + "은(는) null일 수 없습니다.");
    }

}
