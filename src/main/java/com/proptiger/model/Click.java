package com.proptiger.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@IdClass(ClickPK.class)
@Table(name="Clicks",schema="Squeezer")
public class Click {
	
	@Id
	@Column(name="short_url")
	private String shortUrl;

	@Id
	@Column(name="click_date")
	private Date clickDate;

	@Column(name="count")
	private int count;	
	
	@PrePersist
	public void beforeCreation() {
		count=1;
	}
	
	public Click() {
		
	}
	
	public Click(String shortUrl, Date currDate) {
		this.shortUrl=shortUrl;
		this.clickDate=currDate;
	}
	
	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}
	
	public Date getClickDate() {
		return clickDate;
	}

	public void setClickDate(Date clickDate) {
		this.clickDate = clickDate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
