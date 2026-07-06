package com.ahndonghwan.backend.batch.member;

import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;
import com.ahndonghwan.backend.member.utils.MockMemberGenerator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MemberBulkProcessor implements ItemProcessor<MemberJobParameter, List<MemberCreateReqDto>> {

    private final MockMemberGenerator mockMemberGenerator;

    @Override
    public @Nullable List<MemberCreateReqDto> process(MemberJobParameter item) throws Exception {

        return mockMemberGenerator.generateMockMemberList(item.count());
    }
}
