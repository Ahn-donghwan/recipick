package com.recipick.member.domain.model;

import com.recipick.member.domain.exception.InvalidMemberStateTransitionException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 7, 10, 12, 0);

    @Test
    void createsNewMemberWithDefaultRoleAndStatus() {
        Member member = createMember();

        assertNull(member.getId());
        assertEquals(MemberRole.USER, member.getRole());
        assertEquals(MemberStatus.ACTIVE, member.getStatus());
        assertEquals(CREATED_AT, member.getCreatedAt());
        assertEquals(CREATED_AT, member.getUpdatedAt());
    }

    @Test
    void rejectsBlankRequiredInformation() {
        assertThrows(IllegalArgumentException.class, () -> Member.create(
                UUID.randomUUID(),
                " ",
                "encoded-password",
                "01012345678",
                "recipe-lover",
                "홍길동",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                CREATED_AT
        ));
    }

    @Test
    void changesStatusThroughAllowedTransitions() {
        Member member = createMember();
        LocalDateTime suspendedAt = CREATED_AT.plusDays(1);
        LocalDateTime activatedAt = suspendedAt.plusDays(1);

        member.suspend(suspendedAt);
        assertEquals(MemberStatus.SUSPENDED, member.getStatus());
        assertEquals(suspendedAt, member.getUpdatedAt());

        member.activate(activatedAt);
        assertEquals(MemberStatus.ACTIVE, member.getStatus());
        assertEquals(activatedAt, member.getUpdatedAt());
    }

    @Test
    void cannotChangeWithdrawnMemberStatus() {
        Member member = createMember();
        member.withdraw(CREATED_AT.plusDays(1));

        assertThrows(
                InvalidMemberStateTransitionException.class,
                () -> member.activate(CREATED_AT.plusDays(2))
        );
    }

    private Member createMember() {
        return Member.create(
                UUID.randomUUID(),
                "member@example.com",
                "encoded-password",
                "01012345678",
                "recipe-lover",
                "홍길동",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                CREATED_AT
        );
    }
}
