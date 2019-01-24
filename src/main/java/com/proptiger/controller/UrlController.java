package com.proptiger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.model.Click;
import com.proptiger.model.Url;
import com.proptiger.service.UrlService;

@Controller
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@RequestMapping(value="/ping")
	@ResponseBody
	public String ping() {
		return "pong";
	}
	
	@RequestMapping(value="/url",method=RequestMethod.POST)
	@ResponseBody
	public String insertNewUrl(@RequestBody Url url) {
		return urlService.saveUrl(url);
	}
	
	@RequestMapping(value="/url",method=RequestMethod.GET)
	@ResponseBody
	public String getLongUrl(@RequestParam String shortUrl) {
		String arr[]=shortUrl.split("/");
		int id=urlService.computeId(arr[1].toCharArray(),arr[1].length());
		urlService.recordClick(id);
		return urlService.fetchLongUrl(id);
	}
	
	@RequestMapping(value="/url/report/clicks",method=RequestMethod.GET)
	@ResponseBody
	public List<Click> getDailyReport() {
		return urlService.generateDailyReport();
	}
	
	@RequestMapping(value="/url/report/newUrls",method=RequestMethod.GET)
	@ResponseBody
	public List<Url> getDailyUrlCreated() {
		return urlService.getDailyUrlCreated();
	}

}
