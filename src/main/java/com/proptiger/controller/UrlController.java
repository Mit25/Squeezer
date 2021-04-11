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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value="url/",description="URL Shortener Service")
@Controller
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@ApiOperation(value="Testing Method",notes="It response with pong.",response=String.class)
	@RequestMapping(value="/ping",method=RequestMethod.GET)
	@ResponseBody
	public String ping() {
		return "pong";
	}
	
	@ApiOperation(value="Convert any long-url into short-url",notes="It performs decimal to base 62 conversion on id to generate a unique short url path.",response=String.class)
	@RequestMapping(value="/url",method=RequestMethod.POST)
	@ResponseBody
	public String insertNewUrl(@ApiParam(value="long-url(required) and domain name(optional)",required=true) @RequestBody Url url) {
		return urlService.saveUrl(url);
	}
	
	@ApiOperation(value="Return original long-url",notes="It performs reverse base converion to retrive id and use that id to find long-url in database",response=String.class)
	@RequestMapping(value="/url",method=RequestMethod.GET)
	@ResponseBody
	public String getLongUrl(@ApiParam(value="Short-url which you want to open",required=true) @RequestParam String shortUrl) {
		return urlService.getLongUrl(shortUrl);
	}
	
	@ApiOperation(value="Respond with total clicks of short-urls",notes="Looks up data of short to long url requests and extract today's clicks",response=Click.class,responseContainer="List")
	@RequestMapping(value="/url/report/clicks",method=RequestMethod.GET)
	@ResponseBody
	public List<Click> getDailyReport() {
		return urlService.generateDailyReport();
	}
	
	@ApiOperation(value="Respond with total clicks of short-url",notes="Looks up data of short to long url requests and extract today's clicks",response=Click.class,responseContainer="List")
	@RequestMapping(value="/url/report/clickOn",method=RequestMethod.GET)
	@ResponseBody
	public List<Click> getFullReport(@ApiParam(value="Short-url for which you want report",required=true) @RequestParam String shortUrl) {
		return urlService.generateFullReport(shortUrl);
	}
	
	@ApiOperation(value="Respond with total short url created today",notes="Queries database and return the result set.",response=Url.class,responseContainer="List")
	@RequestMapping(value="/url/report/newUrls",method=RequestMethod.GET)
	@ResponseBody
	public List<Url> getDailyUrlCreated() {
		return urlService.getDailyUrlCreated();
	}
	
	@ApiOperation(value="Respond with total number short url created today",notes="Queries database and return the result set.",response=Integer.class)
	@RequestMapping(value="/url/report/newUrlCount",method=RequestMethod.GET)
	@ResponseBody
	public String getDailyUrlCountCreated() {
		return urlService.getDailyUrlCountCreated();
	}

}
