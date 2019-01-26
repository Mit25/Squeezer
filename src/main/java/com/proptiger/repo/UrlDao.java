package com.proptiger.repo;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.model.Url;

public interface UrlDao extends JpaRepository<Url,Integer>{

	public Url findByShortUrl(String shortUrl);
	
	public Url findByLongUrl(String longUrl);
	
	public Url findById(int id);

	@Query(value="select * from Squeezer.Urls where Squeezer.Urls.creation_date between ?1 and ?2", nativeQuery=true)
	public List<Url> getDailyUrlCreated(Timestamp todayDate,Timestamp tomorrowDate);
	
	@Query(value="select count(id) from Squeezer.Urls where Squeezer.Urls.creation_date between ?1 and ?2", nativeQuery=true)
	public BigInteger getDailyUrlCountCreated(Timestamp todayDate,Timestamp tomorrowDate);
	
}
