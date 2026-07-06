package com.ahndonghwan.backend.batch.member;

import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;
import com.ahndonghwan.backend.member.entity.Member;
import com.ahndonghwan.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberBulkWriter implements ItemWriter<List<MemberCreateReqDto>> {

    private final MemberRepository memberRepository;

    @Override
    public void write(Chunk<? extends List<MemberCreateReqDto>> chunk) throws Exception {

        List<Member> members = chunk.getItems().stream()
                .flatMap(List::stream)
                .map(MemberCreateReqDto::toEntity)
                .toList();

        memberRepository.saveAll(members);
    }
}
