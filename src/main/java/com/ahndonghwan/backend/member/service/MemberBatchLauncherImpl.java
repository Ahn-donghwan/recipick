package com.ahndonghwan.backend.member.service;

import com.ahndonghwan.backend.batch.member.properties.MemberBatchProperties;
import com.ahndonghwan.backend.member.dto.request.BulkMemberCreateReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberBatchLauncherImpl implements MemberBatchLauncher {

    private final JobOperator jobOperator;
    private final Job memberBulkInsertJob;

    private final MemberBatchProperties memberBatchProperties;

    @Override
    public void createMockMembers(BulkMemberCreateReqDto dto) {

        long timestamp = System.currentTimeMillis();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("count", dto.count())
                .addLong("timestamp", timestamp)
                .toJobParameters();

        try {

            JobExecution execution = jobOperator.start(memberBulkInsertJob, jobParameters);
            log.info("memberBulkInsertJob finished. job execution id: {}, status: {}, count: {}, timestamp: {}",
                    execution.getId(), execution.getStatus(), dto.count(), timestamp);

            int chunkSize = memberBatchProperties.chunkSize();
            log.info(">>> chunkSize = {}", chunkSize);  // 값 확인용

        } catch (Exception e) {
            log.error("memberBulkInsertJob failed. count: {}, timestamp: {}",
                    dto.count(), timestamp, e);
        }
    }
}
