package com.wanted.preOnboarding.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String company_name;

    private String country;

    private String region;


    @OneToMany(mappedBy = "company")
    private List<JobPosting> jobPostings;
}
