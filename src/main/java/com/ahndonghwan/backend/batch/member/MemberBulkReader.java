package com.ahndonghwan.backend.batch.member;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class MemberBulkReader implements ItemReader<MemberJobParameter> {

    private final int count;
    private boolean read = false;

    // constructor
    public MemberBulkReader(@Value("#{jobParameters['count']}") Integer count) {
        this.count = count != null ? count : 0;
    }

    @Override
    public MemberJobParameter read() {
        if(read) {
            return null;
        }
        read = true;
        return new MemberJobParameter(count);
    }
}
