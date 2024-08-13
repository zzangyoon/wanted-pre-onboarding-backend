package com.wanted.preOnboarding.repository;

import com.wanted.preOnboarding.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
