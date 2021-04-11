package com.proptiger.model;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class ClickPK implements Serializable {
	
	public String shortUrl;
	public Date clickDate;
	
	public ClickPK() {
		
	}
	
	public ClickPK(String shortUrl, Date currDate) {
		this.shortUrl=shortUrl;
		this.clickDate=currDate;
	}

}
