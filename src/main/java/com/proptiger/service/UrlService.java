package com.proptiger.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Autowired
	private ApplicationContext appCtx;
	
	/* 
	 * This function receives the long-url and checks if it is already exist in database. 
	 * If exist than return corresponding short-url.
	 * Otherwise it will generate a new short-url.
	 * Then save the url's record in database and return short-url.
	 */
	@Transactional
	public String saveUrl(Url url) {
		UrlService tmpService=appCtx.getBean(UrlService.class);
		String shortUrl=tmpService.fetchShortUrl(url.getLongUrl());
		if(shortUrl!=null)
			return shortUrl;
		if(url.getDomain()==null) {
			url.setDomain(Constants.myDomain);
		}
		Url tmpUrl=urlDao.save(url);
		tmpUrl.setShortUrl(tmpUrl.getDomain()+"/"+generateShortUrl(tmpUrl.getId()));
		return urlDao.save(tmpUrl).getShortUrl();
	}

	/*
	 * Check database if long-url exist or not.
	 * If exist than return the corresponding short-url.
	 */
	@Cacheable(value="LongToShortCache",unless="#result==null")
	public String fetchShortUrl(String longUrl) {
		if(urlDao==null)
			System.out.println("Why Again?");
		Url tmpUrl=urlDao.findByLongUrl(longUrl);
		if(tmpUrl==null)
			return null;
		return tmpUrl.getShortUrl();
	}
	
	/* 
	 * This method generates short-url using auto incremented id.
	 * Simple base conversion of id from base 10 to base 62.
	 */
	private String generateShortUrl(int id) {
		String map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
		String shortUrl=""; 
 		while (id>0) { 
			shortUrl=map.charAt(id%62)+shortUrl; 
			id=id/62; 
		} 
 		return shortUrl;
	}
	
	/*
	 * This method fetch long-url and update count for short-url clicks for today.
	 */
	public String getLongUrl(String shortUrl) {
		UrlService tmpService=appCtx.getBean(UrlService.class);
		String arr[]=shortUrl.split("/");										//The short-url format is <domain-name>/<unique-path>
		int id=tmpService.computeId(arr[1].toCharArray(),arr[1].length());		// after split arr[0] is domain name and arr[1] is unique-path
		tmpService.recordClick(shortUrl);
		return tmpService.fetchLongUrl(id);
		
	}

	/* 
	 * This method fetch long-url for short-url.
	 * Id was extracted from short-url in UrlController and passed into this method. 
	 */
	@Cacheable("ShortToLongCache")
	public String fetchLongUrl(int id) {
		if(urlDao==null)
			System.out.println("Why Again?");
		return urlDao.findById(id).getLongUrl();
	}
	
	/* 
	 * Stores how much time a short-url was clicked.
	 * It checks database for record of current date for given short-url.
	 * If the record exist than click count will increased by 1.
	 * Or a new record is stored with click count 1.
	 */
	@Async
	public void recordClick(String shortUrl) {
		Date currDate=new Date(Calendar.getInstance().getTimeInMillis());
		Click tmpClick=clickDao.findByShortUrlAndClickDate(shortUrl, currDate);
		if(tmpClick != null) {
			tmpClick.setCount(tmpClick.getCount()+1);
			clickDao.save(tmpClick);
		}
		else {
			tmpClick=new Click(shortUrl, currDate);
			clickDao.save(tmpClick);
		}
	}
	
	/* 
	 * Compute id from short-url.
	 * Simple base conversion logic for base 62 to base 10.
	 */
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

	/* 
	 * Returns the list of ids of urls which were clicked today.
	 */
	public List<Click> generateDailyReport() {
		Date currDate=new Date(Calendar.getInstance().getTimeInMillis());
		return clickDao.generateReport(currDate);
	}
	
	public List<Click> generateFullReport(String shortUrl) {
		return clickDao.generateFullReport(shortUrl);
	}
	
	/* 
	 * Returns the list of urls who are newly created today.
	 * Creation date in database is stored as timestamp instead of date.
	 * So we query where timestamp between today 12:00AM and tomorrow 12:00AM.
	 */
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
	
	public String getDailyUrlCountCreated() {
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
		return urlDao.getDailyUrlCountCreated(todayDate,tomorrowDate).toString();
	}
	
}
