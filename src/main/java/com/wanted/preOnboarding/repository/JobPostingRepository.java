package com.wanted.preOnboarding.repository;

import com.wanted.preOnboarding.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
     List<JobPosting> findByCompany_CompanyId(Long id);

}
