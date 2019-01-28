package com.proptiger.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.model.Click;

public interface ClickDao extends JpaRepository<Click,Integer>{

	public Click findByShortUrlAndClickDate(String shortUrl, Date clickDate);

	@Query(value="select * from Squeezer.Clicks where Squeezer.Clicks.click_date=?1",nativeQuery=true)
	public List<Click> generateReport(Date currDate);

	@Query(value="select * from Squeezer.Clicks where Squeezer.Clicks.short_url=?1",nativeQuery=true)
	public List<Click> generateFullReport(String shortUrl); 
	
}
