package com.lyceum.cms.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zealyo.common.mvc.FreeMarkerController;

@Controller("cmsIndexController")
@RequestMapping("cms")
public class IndexController extends FreeMarkerController{
	
	protected static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@RequestMapping("")
	public String index(HttpServletRequest request, ModelMap map) {
		log.info("Enter CMS Home Page");
		return getViewName("ftls/index");
	}
}
