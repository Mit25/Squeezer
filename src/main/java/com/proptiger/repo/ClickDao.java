package com.proptiger.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.model.Click;

public interface ClickDao extends JpaRepository<Click,Integer>{

	public Click findByIdAndClickDate(int id, Date clickDate);

	@Query(value="select * from Squeezer.Clicks where Squeezer.Clicks.click_date=?1",nativeQuery=true)
	public List<Click> generateReport(Date currDate); 
	
}
