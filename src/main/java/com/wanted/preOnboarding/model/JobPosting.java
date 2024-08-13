package com.wanted.preOnboarding.model;

import com.wanted.preOnboarding.repository.JobPostingRepository;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "job_posting")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    private String position;

    private Integer reward;

    private String contents;

    private String skill;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private Company company;

    @Transient
    private List<JobPosting> otherJobPostings;

    // 해당 회사가 올린 다른 채용공고 가져오기
    public List<JobPosting> getOtherJobPostings(JobPostingRepository jobPostingRepository) {
        return jobPostingRepository.findByCompany_CompanyId(this.company.getCompanyId());
    }


    public void setOtherJobPostings(List<JobPosting> otherJobPostings) {
        this.otherJobPostings = otherJobPostings;
    }

}
