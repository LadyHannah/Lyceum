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
		//��ҳ
		PagingObject paging = new PagingObject();
		int curPage = StringUtil.parseInt(request.getParameter("currentPage"), 1);
		if (curPage < 1) { curPage = 1; }
		params.put("curPage", curPage);
		paging.setCurPage(curPage);
		paging.setPerPageRow(8);
		
		//��Ŀ˵��
		ColumnInfo gjyy = DAOUtil.getByKey(ColumnInfo.class, "gjyy");
		params.put("gjyy_sub", gjyy.getDescription()); //����Ӣ��
		ColumnInfo xyz = DAOUtil.getByKey(ColumnInfo.class, "xyz");
		params.put("xyz_sub", xyz.getDescription()); //С����
		ColumnInfo tdjs = DAOUtil.getByKey(ColumnInfo.class, "tdjs");
		params.put("tdjs_sub", tdjs.getDescription()); //�Ŷӽ���
		ColumnInfo zsal = DAOUtil.getByKey(ColumnInfo.class, "zsal");
		params.put("zsal_sub", zsal.getDescription()); //��ʵ����
		
		//����Ӣ����Сģ��
		
		paging.setPerPageRow(2);
		ColumnInfo sy = DAOUtil.getByKey(ColumnInfo.class, "sy");
		List<ColumnInformationLink> article = ColumnInformationLink.find(sy.getId(), paging);
		params.put("article", article);
		
		//��ҳͼƬ
		paging.setPerPageRow(5);
		List<Images> imagesList = Images.findListByName("��ҳ", null, paging);
		params.put("imagesList", imagesList);
		
		return getViewName("index");
	}
	
	/**
	 * ��ת����ҳ��
	 * @param key
	 * @param request
	 * @param paging
	 * @param map
	 * @return
	 */
	@RequestMapping("col/{key}")
	public String col(@PathVariable String key, HttpServletRequest request, PagingObject paging, ModelMap map) {
		//��ת������ҳ��
		map.put("colKey", key.trim());
		//�����ݹ�����keyת��Ϊ���Բ�ѯ��key
		key = key.trim().replaceAll("-", "/");
		//�жϵ�������ʽ,Ϊ��Ŀ��ǩ��key��ֵ
		map.put("columnKey", key);
		//��ҳ
		int curPage = StringUtil.parseInt(request.getParameter("currentPage"), 1);
		if (curPage < 1) { curPage = 1; }
		map.put("curPage", curPage);
		paging.setCurPage(curPage);
		paging.setPerPageRow(8);
		
		//����Ӣ��
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
