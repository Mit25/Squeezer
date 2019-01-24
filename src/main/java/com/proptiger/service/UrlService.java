package com.proptiger.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.proptiger.model.Click;
import com.proptiger.model.Url;
import com.proptiger.repo.ClickDao;
import com.proptiger.repo.UrlDao;

@Service
public class UrlService {

	@Autowired
	private UrlDao urlDao;
	
	@Autowired
	private ClickDao clickDao;
	
	//@CachePut(value="LongUrlCache",key="#result")
	public String saveUrl(Url url) {
		Url tmpUrl;
		if( (tmpUrl = isLongUrlExist( url.getLongUrl() ) ) != null) {
			return tmpUrl.getShortUrl();
		}
		if(url.getDomain()==null) {
			url.setDomain(Constants.myDomain);
		}
		tmpUrl=urlDao.save(url);
		tmpUrl.setShortUrl(tmpUrl.getDomain()+"/"+generateShortUrl(tmpUrl.getId()));
		return urlDao.save(tmpUrl).getShortUrl();
	}

	@Cacheable("LongUrlCache")
	public String fetchLongUrl(int id) {
		return urlDao.findById(id).getLongUrl();
	}
	
	@Async
	public void recordClick(int id) {
		Date currDate=new Date(Calendar.getInstance().getTimeInMillis());
		Click tmpClick=clickDao.findByIdAndClickDate(id, currDate);
		if(tmpClick != null) {
			tmpClick.setCount(tmpClick.getCount()+1);
			clickDao.save(tmpClick);
		}
		else {
			tmpClick=new Click(id, currDate);
			clickDao.save(tmpClick);
		}
	}
	
	public int computeId(char[] shortUrl,int n) {
		int id=0;
	    for (int i=0; i < n; i++) { 
	        if ('a' <= shortUrl[i] && shortUrl[i] <= 'z') 
	          id = id*62 + shortUrl[i] - 'a'; 
	        if ('A' <= shortUrl[i] && shortUrl[i] <= 'Z') 
	          id = id*62 + shortUrl[i] - 'A' + 26; 
	        if ('0' <= shortUrl[i] && shortUrl[i] <= '9') 
	        	id = id*62 + shortUrl[i] - '0' + 52; 
	    } 
	    return id; 
	}
	
	private Url isLongUrlExist(String longUrl) {
		return urlDao.findByLongUrl(longUrl);
	}
	
	private String generateShortUrl(int id) {
		String map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
		String shortUrl=""; 
 		while (id>0) { 
			shortUrl=map.charAt(id%62)+shortUrl; 
			id=id/62; 
		} 
 		return shortUrl;
	}

	public List<Click> generateDailyReport() {
		Date currDate=new Date(Calendar.getInstance().getTimeInMillis());
		return clickDao.generateReport(currDate);
	}

	public List<Url> getDailyUrlCreated() {
		Calendar today = new GregorianCalendar();
		Calendar tomorrow = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		tomorrow.set(Calendar.HOUR_OF_DAY, 0);
		tomorrow.set(Calendar.MINUTE, 0);
		tomorrow.set(Calendar.SECOND, 0);
		tomorrow.set(Calendar.MILLISECOND, 0);	
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp todayDate=new Timestamp(today.getTimeInMillis());
		Timestamp tomorrowDate=new Timestamp(tomorrow.getTimeInMillis());
		return urlDao.getDailyUrlCreated(todayDate,tomorrowDate);
	}
	
}
