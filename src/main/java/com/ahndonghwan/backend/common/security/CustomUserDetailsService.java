package com.ahndonghwan.backend.common.security;

import com.ahndonghwan.backend.common.exception.BaseException;
import com.ahndonghwan.backend.common.exception.ErrorCode;
import com.ahndonghwan.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberUuid) throws UsernameNotFoundException {

        return new CustomUserDetails(
                memberRepository.findByMemberUuid(memberUuid).orElseThrow(
                        () -> new BaseException(ErrorCode.MEMBER_NOT_FOUND)
                )
        );
    }
}
