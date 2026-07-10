package com.ahndonghwan.backend.common.security;

import com.ahndonghwan.backend.member.entity.Member;
import com.ahndonghwan.backend.member.enums.Role;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String memberUuid;
    private final String password;
    private final Role role;

    @Builder
    public CustomUserDetails(Member member) {
        this.memberUuid = member.getMemberUuid().toString();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.memberUuid.toString();
    }
}
