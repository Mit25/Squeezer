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
	@Column(name="id")
	private int id;

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
	
	public Click(int id, Date currDate) {
		this.id=id;
		this.clickDate=currDate;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
