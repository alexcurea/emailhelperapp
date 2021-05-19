package com.emailhelper.emailhelper.feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailhelper.emailhelper.model.Candidate;
import com.emailhelper.emailhelper.model.FeedbackDto;
import com.emailhelper.emailhelper.model.Newsletter;
import com.emailhelper.emailhelper.repository.CandidateRepository;
import com.emailhelper.emailhelper.repository.FeedbackDtoRepository;
import com.emailhelper.emailhelper.repository.NewsletterRepository;

import java.io.File;
import java.util.List;

import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
	
	private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
	
	@Autowired
	private FeedbackDtoRepository feedbackDtoRepository;
	
	@Autowired
	private NewsletterRepository newsletterRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
    private JavaMailSender mailSender;

    private EmailCfg emailCfg;

    public FeedbackController(EmailCfg emailCfg) {
        this.emailCfg = emailCfg;
    }

    
    @PostMapping(value="/send", consumes=MediaType.APPLICATION_JSON_VALUE)
    public void sendFeedback(@RequestBody FeedbackDto feedback,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("Feedback is not valid");
        }

        // Create a mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());

        // Create an email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("rc@feedback.com");
        mailMessage.setTo(feedback.getAdress());
        mailMessage.setSubject(feedback.getSubject());
        mailMessage.setText(feedback.getContent());

        // Send mail
        mailSender.send(mailMessage);
        
        this.feedbackDtoRepository.save(feedback);
    }
    
    @PostMapping(value="/contract/send", consumes=MediaType.APPLICATION_JSON_VALUE)
    public void sendContractEmail(@RequestBody FeedbackDto feedback,
                             BindingResult bindingResult) throws MessagingException{
        if(bindingResult.hasErrors()){
            throw new ValidationException("Feedback is not valid");
        }
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom("admitere@unitbv.ro");
        helper.setTo(feedback.getAdress());
        helper.setSubject(feedback.getSubject());
        helper.setText(feedback.getContent(), true);
        
        FileSystemResource file = new FileSystemResource(new File("C:\\Users\\Curea\\git\\emailhelperapp\\emailhelper2\\src\\main\\resources\\pdf\\Metodologia_de_admitere.pdf"));
        helper.addAttachment("Metodologia_de_admitere.pdf", file);
        
        FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\Curea\\git\\emailhelperapp\\emailhelper2\\src\\main\\resources\\img\\ut.png"));
        helper.addInline("image001", resource);
        
        mailSender.send(message);
        this.feedbackDtoRepository.save(feedback);
    }
    
    @PostMapping(value="/newsletter/send", consumes=MediaType.APPLICATION_JSON_VALUE)
    public void sendNewsletter(@RequestBody Newsletter newsletter,
                             BindingResult bindingResult) throws MessagingException{
        if(bindingResult.hasErrors()){
            throw new ValidationException("Newsletter is not valid");
        }
        
        List<Candidate> subscribedCandidates = this.candidateRepository.findByIsSubscribed(true);
        String joinedAdresses = subscribedCandidates.stream()
        		.map(Candidate::getEmail)
        		.collect(Collectors.joining(", "));
        
        logger.info("Adresses:",  joinedAdresses);
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        
        
        helper.setFrom("admitere@unitbv.ro");
        helper.setTo(InternetAddress.parse(joinedAdresses));
        helper.setSubject(newsletter.getTitle());
        helper.setText(newsletter.getContent(), true);
        
        //FileSystemResource file = new FileSystemResource(new File("C:\\Users\\Curea\\git\\emailhelperapp\\emailhelper2\\src\\main\\resources\\pdf\\Metodologia_de_admitere.pdf"));
        //helper.addAttachment("Metodologia_de_admitere.pdf", file);
        
        FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\Curea\\git\\emailhelperapp\\emailhelper2\\src\\main\\resources\\img\\ut.png"));
        helper.addInline("image001", resource);
        
        mailSender.send(message);
        this.newsletterRepository.save(newsletter);
    }
        
}
