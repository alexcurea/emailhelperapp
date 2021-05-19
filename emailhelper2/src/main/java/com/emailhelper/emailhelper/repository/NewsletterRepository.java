package com.emailhelper.emailhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emailhelper.emailhelper.model.FeedbackDto;
import com.emailhelper.emailhelper.model.Newsletter;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
	
	

}
