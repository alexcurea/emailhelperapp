package com.emailhelper.emailhelper.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.emailhelper.emailhelper.model.enums.ERole;

@Entity
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;

	public Role() {

	}
	
	public Role(ERole name) {
		this.setName(name);
	}
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}

    
	
}
