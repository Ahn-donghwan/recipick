package com.ahndonghwan.backend.member.repository;

import com.ahndonghwan.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
