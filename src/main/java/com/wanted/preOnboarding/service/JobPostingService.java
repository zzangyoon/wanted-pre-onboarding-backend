package com.wanted.preOnboarding.service;

import com.wanted.preOnboarding.model.Application;
import com.wanted.preOnboarding.model.Company;
import com.wanted.preOnboarding.model.JobPosting;
import com.wanted.preOnboarding.model.Member;
import com.wanted.preOnboarding.repository.ApplicationRepository;
import com.wanted.preOnboarding.repository.CompanyRepository;
import com.wanted.preOnboarding.repository.JobPostingRepository;
import com.wanted.preOnboarding.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostingService {
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private MemberRepository memberRepository;

//    1. 채용공고 등록
    public JobPosting createJobPosting(Long company_id, JobPosting jobPosting) {
        Company company = companyRepository.findById(company_id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        jobPosting.setCompany(company);
        return jobPostingRepository.save(jobPosting);
    }

//    2. 채용공고 수정
    public JobPosting updateJobPosting(Long post_id, JobPosting updatedJobPosting) {
        JobPosting jobPosting = jobPostingRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("JobPosting not found"));

        if (updatedJobPosting.getPosition() != null) {
            jobPosting.setPosition(updatedJobPosting.getPosition());
        }
        if (updatedJobPosting.getReward() != null) {
            jobPosting.setReward(updatedJobPosting.getReward());
        }
        if (updatedJobPosting.getContents() != null) {
            jobPosting.setContents(updatedJobPosting.getContents());
        }
        if (updatedJobPosting.getSkill() != null) {
            jobPosting.setSkill(updatedJobPosting.getSkill());
        }

        return jobPostingRepository.save(jobPosting);
    }

//    3. 채용공고 삭제
    public void deleteJobPosting(Long post_id) {
        jobPostingRepository.deleteById(post_id);
    }

//    4-1. 채용공고 목록 가져오기
    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

//    5. 채용 상세페이지
    public JobPosting getJobPostingDetails(Long post_id) {
        JobPosting jobPosting = jobPostingRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("JobPosting not found"));

        // 해당 회사가 올린 다른 채용공고 가져오기
        List<JobPosting> otherJobPostings = jobPosting.getOtherJobPostings(jobPostingRepository);
        jobPosting.setOtherJobPostings(otherJobPostings);

        return jobPosting;
    }

//    6. 채용공고 지원
    public void applyForJob(Long post_id, Long member_id){
        Member member = memberRepository.findById(member_id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        JobPosting jobPosting = jobPostingRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("JobPosting not found"));

        if (applicationRepository.existsByMemberAndJobPosting(member, jobPosting)) {
            throw new RuntimeException("already applied");
        }

        Application application = new Application();
        application.setMember(member);
        application.setJobPosting(jobPosting);
        applicationRepository.save(application);
    }
}
