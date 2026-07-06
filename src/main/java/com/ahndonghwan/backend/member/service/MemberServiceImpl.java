package com.ahndonghwan.backend.member.service;

import com.ahndonghwan.backend.common.exception.BaseException;
import com.ahndonghwan.backend.common.exception.ErrorCode;
import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;
import com.ahndonghwan.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void createMember(MemberCreateReqDto dto) {
        try {
            memberRepository.save(dto.toEntity());
            log.info("member created");
        } catch (Exception e) {
            log.error("member create failed");
            throw new BaseException(ErrorCode.MEMBER_CREATE_FAILED);
        }
    }
}
