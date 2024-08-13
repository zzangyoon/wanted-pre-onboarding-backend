package com.wanted.preOnboarding.repository;

import com.wanted.preOnboarding.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
