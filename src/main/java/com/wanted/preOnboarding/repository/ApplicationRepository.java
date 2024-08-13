package com.wanted.preOnboarding.repository;

import com.wanted.preOnboarding.model.Application;
import com.wanted.preOnboarding.model.JobPosting;
import com.wanted.preOnboarding.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByMemberAndJobPosting(Member member, JobPosting jobPosting);
}
