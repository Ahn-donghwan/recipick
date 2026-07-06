package com.ahndonghwan.backend.member.utils;

import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;
import com.ahndonghwan.backend.member.enums.Gender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
public class MockMemberGeneratorImpl implements MockMemberGenerator {

    @Override
    public MemberCreateReqDto generateMockMember() {

        String uuid = UUID.randomUUID().toString().replace("-", "");

        return MemberCreateReqDto.builder()
                .email("test" + uuid + "@gmail.com")
                .password("Test1234!")
                .phone("010" + "-" + randomFourDigitNumber() + "-" + randomFourDigitNumber())
                .nickname("user" + uuid)
                .name("user" + uuid)
                .birth(randomBirth())
                .gender(randomGender())
                .build();
    }

    @Override
    public List<MemberCreateReqDto> generateMockMemberList(int count) {

        return IntStream.range(0, count)
                .mapToObj(i -> generateMockMember())
                .toList();
    }

    public static int randomFourDigitNumber() {
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    public static LocalDate randomBirth() {
        return LocalDate.ofEpochDay(
                ThreadLocalRandom.current().nextLong(
                        LocalDate.of(1950, 1, 1).toEpochDay(),
                        LocalDate.of(2010, 12, 12).toEpochDay()
                )
        );
    }

    public static Gender randomGender() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Gender.MALE
                : Gender.FEMALE;
    }
}
