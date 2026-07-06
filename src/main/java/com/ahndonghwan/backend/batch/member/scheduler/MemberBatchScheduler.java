package com.ahndonghwan.backend.batch.member.scheduler;

import com.ahndonghwan.backend.member.dto.request.BulkMemberCreateReqDto;
import com.ahndonghwan.backend.member.service.MemberBatchLauncher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberBatchScheduler {

    private final MemberBatchLauncher memberBatchLauncher;

    // 10분마다
    @Scheduled(cron = "0 */10 * * * *")
    public void createMockMembers() {

        log.info("memberBulkInsertJob scheduler started.");
        BulkMemberCreateReqDto dto = new BulkMemberCreateReqDto(5000L);
        memberBatchLauncher.createMockMembers(dto);
    }
}
