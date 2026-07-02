package com.ahndonghwan.backend.member.entity;

import com.ahndonghwan.backend.common.entity.Timestamp;
import com.ahndonghwan.backend.member.enums.Gender;
import com.ahndonghwan.backend.member.enums.Role;
import com.ahndonghwan.backend.member.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID memberUuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Member(
            String email, String password, String phone,
            String nickname, String name, LocalDate birth, String gender
    ) {
        this.memberUuid = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.gender = Gender.valueOf(gender);
        this.role = Role.USER;
        this.status = Status.ACTIVE;
    }
}
