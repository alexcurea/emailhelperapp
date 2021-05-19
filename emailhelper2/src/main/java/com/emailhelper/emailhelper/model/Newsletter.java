package com.emailhelper.emailhelper.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "newsletters")
public class Newsletter {
	
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Id
	@Column(name = "newsletter_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "publisher", nullable = false)
	private String publisher;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "content", nullable = false, length = 10000)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp", nullable = false)
	private Date timestamp;
	
	public Newsletter() {
		
	}
	
	public Newsletter(Newsletter newsletter) {
		
		this.id = newsletter.getId();
		this.publisher = newsletter.getPublisher();
		this.title =  newsletter.getTitle();
		this.content = newsletter.getContent();
		this.timestamp = newsletter.getTimestamp();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
