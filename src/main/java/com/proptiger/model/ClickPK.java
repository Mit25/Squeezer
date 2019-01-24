package com.proptiger.model;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class ClickPK implements Serializable {
	
	public int id;
	public Date clickDate;
	
	public ClickPK() {
		
	}
	
	public ClickPK(int id, Date currDate) {
		this.id=id;
		this.clickDate=currDate;
	}

}
