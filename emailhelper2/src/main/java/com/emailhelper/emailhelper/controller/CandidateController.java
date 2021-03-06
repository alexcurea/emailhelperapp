package com.emailhelper.emailhelper.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emailhelper.emailhelper.model.Candidate;
import com.emailhelper.emailhelper.repository.CandidateRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CandidateController {
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@GetMapping(value= "/candidate", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Candidate> getCandidates(){
		return this.candidateRepository.findAll();
	}
	
	@GetMapping(value = "/candidate/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Candidate getCandidate(@PathVariable("email") String email) {
		Optional<Candidate> candidateOpt = this.candidateRepository.findByEmail(email);
		return candidateOpt.orElse(null);
	}
	
	@PostMapping(value = "/candidate/create", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public Candidate createCandidate(@RequestBody Candidate candidate) {
		return this.candidateRepository.save(candidate);
	}
	
	@PutMapping(value = "/candidate/update", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Candidate updateCandidate(@RequestBody Candidate candidate) {
		return this.candidateRepository.save(candidate);
	}
	
	@DeleteMapping(value = "/candidate/delete")
	public void deleteCandidate(@RequestParam ("id") Long id) {
		if(candidateRepository.existsById(id)) {
			this.candidateRepository.deleteById(id);
		}
	}
	
	@DeleteMapping(value = "/candidate/subscribe")
	public void subscribe(@RequestParam ("email") String email, @RequestParam ("subscribtion") String subscribtion) {
		if(candidateRepository.existsByEmail(email)) {
			Candidate candidate = candidateRepository.findByEmail(email).get();
			boolean subscriptionBool = Boolean.parseBoolean(subscribtion);
			candidate.setIsSubscribed(subscriptionBool);
			candidateRepository.save(candidate);
		}
	}
	

}
