package com.ahndonghwan.backend.batch.member.job;

import com.ahndonghwan.backend.batch.member.MemberBulkProcessor;
import com.ahndonghwan.backend.batch.member.MemberBulkReader;
import com.ahndonghwan.backend.batch.member.MemberBulkWriter;
import com.ahndonghwan.backend.batch.member.MemberJobParameter;
import com.ahndonghwan.backend.batch.member.properties.MemberBatchProperties;
import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MemberJobConfig {

    // 생성자 주입
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final MemberBulkReader reader;
    private final MemberBulkProcessor processor;
    private final MemberBulkWriter writer;
    private final MemberBatchProperties memberBatchProperties;

    // 수동 빈 등록
    @Bean
    public Job memberBulkInsertJob() {
        return new JobBuilder("memberBulkInsertJob", jobRepository)
                .start(memberBulkInsertStep())
                .build();
    }

    @Bean
    public Step memberBulkInsertStep() {
        return new StepBuilder("memberBulkInsertStep", jobRepository)
                .<MemberJobParameter, List<MemberCreateReqDto>> chunk(memberBatchProperties.chunkSize())
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
