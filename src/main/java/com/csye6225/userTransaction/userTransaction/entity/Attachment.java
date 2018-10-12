package com.csye6225.userTransaction.userTransaction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Attachment {
	
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
	private String id;
	
	private String url;
	
	public Attachment(String url) {
		this.url = url;
	}

	public Attachment() {
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Attachment [id=" + id + ", url=" + url + "]";
	}
	
	
	
	

	
	
}
