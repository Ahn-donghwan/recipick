package com.ahndonghwan.backend.member.controller;

import com.ahndonghwan.backend.common.response.ApiResponse;
import com.ahndonghwan.backend.common.response.ResponseMessage;
import com.ahndonghwan.backend.member.dto.request.CreateMemberReqDto;
import com.ahndonghwan.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "member")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원 생성",
            description = """
                    [테스트용]
                    관리자가 테스트를 목적으로 회원 1명을 임의로 생성하는 API 입니다.
                    
                    [요청 바디]
                    - email : (필수 입력, 중복 불가) 이메일
                    - password : (필수 입력, 8~20자리, 영문, 숫자, 특수문자를  최소 1글자 이상씩 포함) 비밀번호
                    - phone : 휴대폰 번호
                    - nickname : 닉네임
                    - name : 회원명
                    - birth : 생년월일
                    - gender : 성별 (MALE/FEMALE)
                    
                    [예외 상황]
                    - MEMBER_CREATE_FAILED : 회원가입 중 알 수 없는 오류 발생
                    """
    )
    @PostMapping
    public ApiResponse<Void> createMember(
            @RequestBody CreateMemberReqDto dto
    ) {
        memberService.createMember(dto);
        return new ApiResponse<>(ResponseMessage.SUCCESS_TO_CREATE_MEMBER);
    }
}
