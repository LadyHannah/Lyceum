package com.lyceum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lyceum.cms.core.ColumnInfo;
import com.lyceum.cms.core.ColumnInformationLink;
import com.lyceum.cms.core.Images;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.hibernate.DAOUtil;
import com.zealyo.jdbc.util.PagingObject;

@Controller
public class IndexController extends FreeMarkerController{

	protected static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Value("#{settings['filePath']}")
	private String filePath;
	@RequestMapping("")
	public String index(HttpServletRequest request, ModelMap params) {
		log.info("Enter Home Page");
		//分页
		PagingObject paging = new PagingObject();
		int curPage = StringUtil.parseInt(request.getParameter("currentPage"), 1);
		if (curPage < 1) { curPage = 1; }
		params.put("curPage", curPage);
		paging.setCurPage(curPage);
		paging.setPerPageRow(8);
		
		//栏目说明
		ColumnInfo gjyy = DAOUtil.getByKey(ColumnInfo.class, "gjyy");
		params.put("gjyy_sub", gjyy.getDescription()); //国际英语
		ColumnInfo xyz = DAOUtil.getByKey(ColumnInfo.class, "xyz");
		params.put("xyz_sub", xyz.getDescription()); //小语种
		ColumnInfo tdjs = DAOUtil.getByKey(ColumnInfo.class, "tdjs");
		params.put("tdjs_sub", tdjs.getDescription()); //团队介绍
		ColumnInfo zsal = DAOUtil.getByKey(ColumnInfo.class, "zsal");
		params.put("zsal_sub", zsal.getDescription()); //真实案例
		
		//国际英语两小模块
		
		paging.setPerPageRow(2);
		ColumnInfo sy = DAOUtil.getByKey(ColumnInfo.class, "sy");
		List<ColumnInformationLink> article = ColumnInformationLink.find(sy.getId(), paging);
		params.put("article", article);
		
		//首页图片
		paging.setPerPageRow(5);
		List<Images> imagesList = Images.findListByName("首页", null, paging);
		params.put("imagesList", imagesList);
		
		return getViewName("index");
	}
	
	/**
	 * 跳转二级页面
	 * @param key
	 * @param request
	 * @param paging
	 * @param map
	 * @return
	 */
	@RequestMapping("col/{key}")
	public String col(@PathVariable String key, HttpServletRequest request, PagingObject paging, ModelMap map) {
		//跳转到二级页面
		map.put("colKey", key.trim());
		//将传递过来的key转换为可以查询的key
		key = key.trim().replaceAll("-", "/");
		//判断导航的样式,为栏目标签的key赋值
		map.put("columnKey", key);
		//分页
		int curPage = StringUtil.parseInt(request.getParameter("currentPage"), 1);
		if (curPage < 1) { curPage = 1; }
		map.put("curPage", curPage);
		paging.setCurPage(curPage);
		paging.setPerPageRow(8);
		
		//国际英语
		if (key.startsWith("gjyy")) {
			return getViewName("info/english");
		}
		if (key.equals("xyz")) {
			return getViewName("info/minority");
		}
		if (key.startsWith("tdjs")) {
			return getViewName("info/team");
		}
		if (key.startsWith("xyz/japanese")) {
			return getViewName("info/japanese");
		}
		if (key.startsWith("xyz/korea")) {
			return getViewName("info/korea");
		}
		if (key.startsWith("xyz/germany")) {
			return getViewName("info/germany");
		}
		if (key.startsWith("xyz/italy")) {
			return getViewName("info/italy");
		}
		if (key.startsWith("xyz/spanish")) {
			return getViewName("info/spanish");
		}
		if (key.startsWith("xyz/franch")) {
			return getViewName("info/franch");
		}
		return getViewName("info/english");
	}
	
}
