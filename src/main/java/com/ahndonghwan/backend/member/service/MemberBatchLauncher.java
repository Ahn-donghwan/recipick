package com.ahndonghwan.backend.member.service;

import com.ahndonghwan.backend.member.dto.request.BulkMemberCreateReqDto;

public interface MemberBatchLauncher {

    void createMockMembers(BulkMemberCreateReqDto dto);
}
