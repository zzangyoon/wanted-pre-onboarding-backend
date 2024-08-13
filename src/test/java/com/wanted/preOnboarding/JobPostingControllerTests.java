package com.wanted.preOnboarding;

import com.wanted.preOnboarding.controller.JobPostingController;
import com.wanted.preOnboarding.model.JobPosting;
import com.wanted.preOnboarding.service.JobPostingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
public class JobPostingControllerTests {

    Long jobPostingId = 1L;

    private MockMvc mockMvc;

    @Mock
    private JobPostingService jobPostingService;

    @InjectMocks
    private JobPostingController jobPostingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobPostingController).build();
    }

//    채용공고 수정 테스트
    @Test
    public void testUpdateJobPostings() {

        JobPosting existingJobPosting = new JobPosting();
        existingJobPosting.setPost_id(jobPostingId);
        existingJobPosting.setReward(1000000);
        existingJobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 채용합니다.");
        existingJobPosting.setSkill("Java");

        JobPosting updatedJobPosting = new JobPosting();
        updatedJobPosting.setReward(1200000);
        updatedJobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다.");
        updatedJobPosting.setSkill("Python");

        when(jobPostingService.updateJobPosting(eq(jobPostingId), any(JobPosting.class)))
                .thenReturn(updatedJobPosting);

        ResponseEntity<JobPosting> response = jobPostingController.updateJobPosting(jobPostingId, updatedJobPosting);
        JobPosting result = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getContents()).isEqualTo("원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다.");
        assertThat(result.getReward()).isEqualTo(1200000);
        assertThat(result.getSkill()).isEqualTo("Python");

        verify(jobPostingService).updateJobPosting(eq(jobPostingId), any(JobPosting.class));
    }

//    채용공고 삭제 테스트1
    @Test
    public void testDeleteJobPosting() throws Exception {
        mockMvc.perform(delete("/job_posting/1"))
                .andExpect(status().isNoContent());
    }

//    채용공고 삭제 테스트2
    @Test
    public void testDeleteJobPosting2() throws Exception {

        ResponseEntity<Void> response = jobPostingController.deleteJobPosting(jobPostingId);

        System.out.println("=======");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(jobPostingService).deleteJobPosting(jobPostingId);
    }

//    채용공고 목록 가져오기 테스트
    @Test
    public void testGetAllJobPostings() {
        JobPosting jobPosting1 = new JobPosting();
        jobPosting1.setPost_id(jobPostingId);
        jobPosting1.setPosition("백엔드 주니어 개발자");

        JobPosting jobPosting2 = new JobPosting();
        jobPosting2.setPost_id(2L);
        jobPosting2.setPosition("Django 백엔드 개발자");

        List<JobPosting> jobPostings = List.of(jobPosting1, jobPosting2);

        when(jobPostingService.getAllJobPostings()).thenReturn(jobPostings);

        ResponseEntity<List<JobPosting>> response = jobPostingController.getAllJobPostings();

        List<JobPosting> result = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getPosition()).isEqualTo("백엔드 주니어 개발자");
        assertThat(result.get(1).getPosition()).isEqualTo("Django 백엔드 개발자");

        verify(jobPostingService).getAllJobPostings();
    }

//    채용 상세페이지 테스트
    @Test
    public void testGetJobPostingDetails() {

        JobPosting jobPosting = new JobPosting();
        jobPosting.setPost_id(jobPostingId);
        jobPosting.setPosition("백엔드 주니어 개발자");
        jobPosting.setReward(1500000);
        jobPosting.setContents("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..");
        jobPosting.setSkill("Java");

        JobPosting otherJobPosting = new JobPosting();
        otherJobPosting.setPost_id(2L);
        otherJobPosting.setPosition("프론트엔드 주니어 개발자");
        otherJobPosting.setReward(700000);
        otherJobPosting.setContents("원티드랩에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..");
        otherJobPosting.setSkill("JavaScript");

        List<JobPosting> otherJobPostings = List.of(otherJobPosting);
        jobPosting.setOtherJobPostings(otherJobPostings);

        when(jobPostingService.getJobPostingDetails(jobPostingId)).thenReturn(jobPosting);

        ResponseEntity<JobPosting> response = jobPostingController.getJobPostingDetails(jobPostingId);

        JobPosting result = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getPost_id()).isEqualTo(jobPostingId);
        assertThat(result.getPosition()).isEqualTo("백엔드 주니어 개발자");
        assertThat(result.getReward()).isEqualTo(1500000);
        assertThat(result.getSkill()).isEqualTo("Java");
        assertThat(result.getOtherJobPostings().size()).isEqualTo(1);
        assertThat(result.getOtherJobPostings().get(0).getPosition()).isEqualTo("프론트엔드 주니어 개발자");

        verify(jobPostingService).getJobPostingDetails(jobPostingId);
    }
}
