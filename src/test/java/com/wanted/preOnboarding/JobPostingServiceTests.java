package com.wanted.preOnboarding;

import com.wanted.preOnboarding.model.Application;
import com.wanted.preOnboarding.model.Company;
import com.wanted.preOnboarding.model.JobPosting;
import com.wanted.preOnboarding.model.Member;
import com.wanted.preOnboarding.repository.ApplicationRepository;
import com.wanted.preOnboarding.repository.CompanyRepository;
import com.wanted.preOnboarding.repository.JobPostingRepository;
import com.wanted.preOnboarding.repository.MemberRepository;
import com.wanted.preOnboarding.service.JobPostingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringJUnitConfig
public class JobPostingServiceTests {
    @Mock
    private JobPostingRepository jobPostingRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private JobPostingService jobPostingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    1. 채용공고 등록 테스트
    @Test
    public void testCreateJobPosting(){
        Company company = new Company();
        company.setCompanyId(1L);

        JobPosting jobPosting = new JobPosting();
        jobPosting.setPosition("백엔드 주니어 개발자");
        jobPosting.setReward(1000000);
        jobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 채용합니다.");
        jobPosting.setSkill("Python");
        jobPosting.setCompany(company);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(jobPostingRepository.save(jobPosting)).thenReturn(jobPosting);

        JobPosting createdJobPosting = jobPostingService.createJobPosting(1L, jobPosting);

        assertThat(createdJobPosting.getPosition()).isEqualTo("백엔드 주니어 개발자");
        assertThat(createdJobPosting.getReward()).isEqualTo(1000000);
    }

//    2. 채용공고 수정 테스트
    @Test
    public void testUpdateJobPosting() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setPost_id(1L);
        jobPosting.setReward(1000000);
        jobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 채용합니다.");
        jobPosting.setSkill("Python");

        JobPosting updatedJobPosting = new JobPosting();
        updatedJobPosting.setReward(1500000);
        updatedJobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다.");

        when(jobPostingRepository.findById(1L)).thenReturn(Optional.of(jobPosting));
        when(jobPostingRepository.save(jobPosting)).thenReturn(jobPosting);

        JobPosting result = jobPostingService.updateJobPosting(1L, updatedJobPosting);

        assertThat(result.getReward()).isEqualTo(1500000);
        assertThat(result.getContents()).contains("적극");
    }

//    3. 채용공고 삭제 테스트
    @Test
    public void testDeleteJobPosting() {
        when(jobPostingRepository.existsById(1L)).thenReturn(true);

        jobPostingService.deleteJobPosting(1L);

        verify(jobPostingRepository, times(1)).deleteById(1L);
    }

//    4-1. 채용공고 목록 가져오기 테스트
    @Test
    public void testGetAllJobPostings() {
        JobPosting jobPosting1 = new JobPosting();
        jobPosting1.setPosition("백엔드 주니어 개발자");
        jobPosting1.setSkill("Python");

        JobPosting jobPosting2 = new JobPosting();
        jobPosting2.setPosition("Django 백엔드 개발자");
        jobPosting2.setSkill("Django");

        when(jobPostingRepository.findAll()).thenReturn(Arrays.asList(jobPosting1, jobPosting2));

        List<JobPosting> jobPostings = jobPostingService.getAllJobPostings();

        assertThat(jobPostings).hasSize(2);
    }

//    5. 채용 상세페이지 테스트
    @Test
    public void testGetJobPostingDetails() {
        JobPosting jobPosting = new JobPosting();
        Company company = new Company();
        company.setCompanyId(1L);
        jobPosting.setPost_id(1L);
        jobPosting.setPosition("백엔드 주니어 개발자");
        jobPosting.setReward(1500000);
        jobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..");
        jobPosting.setSkill("Python");
        jobPosting.setCompany(company);

        JobPosting otherJobPosting = new JobPosting();
        otherJobPosting.setPost_id(2L);
        otherJobPosting.setPosition("프론트엔드 주니어 개발자");
        otherJobPosting.setReward(700000);
        otherJobPosting.setContents("원티드랩에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..");
        otherJobPosting.setSkill("JavaScript");

        List<JobPosting> otherJobPostings = List.of(otherJobPosting);

        when(jobPostingRepository.findById(1L)).thenReturn(Optional.of(jobPosting));
        when(jobPostingRepository.findByCompany_CompanyId(1L)).thenReturn(otherJobPostings);

        JobPosting result = jobPostingService.getJobPostingDetails(1L);

        assertThat(result.getPost_id()).isEqualTo(1L);
        assertThat(result.getPosition()).isEqualTo("백엔드 주니어 개발자");
        assertThat(result.getReward()).isEqualTo(1500000);
        assertThat(result.getSkill()).isEqualTo("Python");
        assertThat(result.getOtherJobPostings().size()).isEqualTo(1);
        assertThat(result.getOtherJobPostings().get(0).getPosition()).isEqualTo("프론트엔드 주니어 개발자");

        verify(jobPostingRepository).findById(1L);
        verify(jobPostingRepository).findByCompany_CompanyId(1L);
    }

//    6. 채용공고 지원 테스트
    @Test
    public void testApplyForJob() {
        Member member = new Member();
        member.setMember_id(1L);

        JobPosting jobPosting = new JobPosting();
        jobPosting.setPost_id(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(jobPostingRepository.findById(1L)).thenReturn(Optional.of(jobPosting));
        when(applicationRepository.existsByMemberAndJobPosting(any(Member.class), any(JobPosting.class))).thenReturn(false);

        jobPostingService.applyForJob(1L, 1L);

        verify(applicationRepository, times(1)).save(any(Application.class));
    }
}
