package com.ahndonghwan.backend.member.dto.request;

import com.ahndonghwan.backend.member.entity.Member;
import com.ahndonghwan.backend.member.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "회원 생성 요청 DTO")
public record CreateMemberReqDto(

        @Schema(description = "이메일", example = "test@test.com")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        String email,

        @Schema(description = "비밀번호", example = "test1234!")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자리 이상 20자리 이하로 입력해주세요.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).*$",
                message = "대문자를 포함한 영문, 숫자, 특수문자를 최소 1글자 이상 포함하여 작성해주세요."
        )
        String password,

        @Schema(description = "휴대폰 번호", example = "010-1234-1234")
        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        String phone,

        @Schema(description = "닉네임")
        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickname,

        @Schema(description = "회원명")
        @NotBlank(message = "회원명을 입력해주세요.")
        String name,

        @Schema(description = "생년월일", example = "2000-01-01")
        @NotNull(message = "생년월일을 입력해주세요.")
        LocalDate birth,

        @Schema(description = "성별", example = "MALE")
        @NotBlank(message = "성별을 입력해주세요.")
        Gender gender
) {

    public Member toEntity() {

        return Member.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .nickname(nickname)
                .name(name)
                .birth(birth)
                .gender(gender)
                .build();
    }
}
