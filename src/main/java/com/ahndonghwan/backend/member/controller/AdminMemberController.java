package com.ahndonghwan.backend.member.controller;

import com.ahndonghwan.backend.common.response.ApiResponse;
import com.ahndonghwan.backend.common.response.ResponseMessage;
import com.ahndonghwan.backend.member.dto.request.BulkMemberCreateReqDto;
import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;
import com.ahndonghwan.backend.member.service.MemberBatchLauncher;
import com.ahndonghwan.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members")
@Tag(name = "member")
public class AdminMemberController {

    private final MemberService memberService;
    private final MemberBatchLauncher memberBatchLauncher;

    @Operation(
            summary = "회원 생성",
            description = "[테스트용] 관리자가 테스트를 목적으로 회원 1명을 임의로 생성하는 API"
    )
    @PostMapping
    public ApiResponse<Void> createMember(@Valid @RequestBody MemberCreateReqDto dto) {
        memberService.createMember(dto);
        return new ApiResponse<>(ResponseMessage.SUCCESS_TO_CREATE_MEMBER);
    }

    @Operation(
            summary = "회원 생성 (벌크)",
            description = "[테스트용] 관리자가 테스트를 목적으로 Mock 회원을 임의로 대량 삽입하는 API"
    )
    @PostMapping("/bulk")
    public ApiResponse<Void> createMember(@Valid @RequestBody BulkMemberCreateReqDto dto) {
        memberBatchLauncher.createMockMembers(dto);
        return new ApiResponse<>(ResponseMessage.SUCCESS_TO_CREATE_MEMBER);
    }
}
