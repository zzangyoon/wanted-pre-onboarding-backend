package com.wanted.preOnboarding.controller;

import com.wanted.preOnboarding.model.JobPosting;
import com.wanted.preOnboarding.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job_posting")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

//    1. 채용공고 등록
    @PostMapping
    public ResponseEntity<JobPosting> createJobPosting(@RequestParam Long company_id, @RequestBody JobPosting jobPosting) {
        return ResponseEntity.ok(jobPostingService.createJobPosting(company_id, jobPosting));
    }

//    2. 채용공고 수정
    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJobPosting(@PathVariable Long id, @RequestBody JobPosting jobPosting) {
        return ResponseEntity.ok(jobPostingService.updateJobPosting(id, jobPosting));
    }

//    3. 채용공고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.noContent().build();
    }

//    4-1. 채용공고 목록 가져오기
    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobPostings() {
        return ResponseEntity.ok(jobPostingService.getAllJobPostings());
    }

//    5. 채용 상세페이지
    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingDetails(@PathVariable Long id) {
        JobPosting jobPosting = jobPostingService.getJobPostingDetails(id);
        return ResponseEntity.ok(jobPosting);
    }

//    6. 채용공고 지원
    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> applyForJob(@PathVariable Long post_id, @RequestParam Long member_id) {
        jobPostingService.applyForJob(post_id, member_id);
        return ResponseEntity.noContent().build();
    }
}
