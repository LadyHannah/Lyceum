package com.lyceum.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyceum.cms.application.InformationApplication;
import com.lyceum.cms.core.ColumnInfo;
import com.lyceum.cms.core.ColumnInformationLink;
import com.lyceum.cms.core.Information;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.common.utils.DateUtil;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.util.PagingObject;

import net.sf.json.JSONObject;

/**
 * Information controller
 * @author Hannah
 *
 */
@Controller
@RequestMapping("cms/info")
public class InformationController extends FreeMarkerController {

	@Autowired
	private InformationApplication informationApplication;
	
	@Override
	@RequestMapping("")
	public String index(HttpServletRequest arg0, ModelMap arg1) {
		return getViewName("ftls/info/index");
	}

	/**
	 * 查找信息数据
	 * @param title			信息标题
	 * @param columnId		栏目编号
	 * @param pagingObject	分页对象
	 * @return JSONObject
	 */
	@RequestMapping(value = "find", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject find(String title, String columnId, PagingObject pagingObject) {
		if ("root".equals(columnId)) {
			columnId = "";
		}
		return ColumnInformationLink.find(title, columnId, pagingObject).toJsonObject();
	}
	
	/**
	 * 保存信息
	 * @param po
	 * @return
	 */
	@RequestMapping(value = "{columnId}", method = RequestMethod.POST)
	@ResponseBody
	public boolean type(@PathVariable String columnId) {
		if ("root".equals(columnId)) {
			return false;
		} else if (!StringUtil.isNullOrEmpty(columnId)) {
			ColumnInfo po = ColumnInfo.get(columnId);
			if (po.getType()==0) {  //多篇信息
				return true;
			} else if (po.getType() == 1) {  //单篇信息
				 List<ColumnInformationLink> linkList = ColumnInformationLink.find(columnId, null);
				 if (linkList.size() > 0) {
					return false;
				 }
			} else if (po.getType() == 2) {  // 不可发布信息
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 新增、编辑信息
	 * @param id
	 * @param columnId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable String id, String columnId, String linkId, ModelMap map) {
		if (!StringUtil.isNullOrEmpty(linkId)) {
			ColumnInformationLink linkPO = ColumnInformationLink.get(linkId);
			id = linkPO.getInformation().getId();
			columnId = linkPO.getColumnInfo().getId();
		}
		Information po = null;
		if ("new".equals(id)) {
			po = new Information();
			map.put("po", po);
		} else {
			po = Information.get(id);
			map.put("po", po == null ? new Information() : po);
		}
		
		if ("root".equals(columnId)) {
			columnId = "";
		}
		ColumnInfo colPO = null;
		if (StringUtil.isNullOrEmpty(columnId)) {
			colPO = new ColumnInfo();
			map.put("colPO", colPO);
		} else {
			colPO = ColumnInfo.get(columnId);
			map.put("colPO", colPO);
		}
		return getViewName("ftls/info/edit");
	}
	
	/**
	 * 保存信息
	 * @param po
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public boolean edit(Information po, String columnId, String release, String create) {
		return informationApplication.saveOrUpdate(po, columnId, release, create);
	}
	
	/**
	 * 删除信息
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteInfo(String ids) {
		return String.valueOf(informationApplication.delete(ids));
	}
	
	/**
	 * 精华、置顶、热门、发布
	 * @param linkId
	 * @param name
	 * @param property
	 * @return
	 */
	@RequestMapping(value = "operate", method = RequestMethod.POST)
	@ResponseBody
	public boolean operate(String linkId, String name, boolean property) {
		ColumnInformationLink linkPO = ColumnInformationLink.get(linkId);
		if (property) {
			property = false;
		} else {
			property = true;
		}
		if ("marrowed".equals(name)) {
			linkPO.setMarrowed(property);
		}
		if ("toped".equals(name)) {
			linkPO.setToped(property);
		}
		if ("hoted".equals(name)) {
			linkPO.setHoted(property);
		}
		if ("published".equals(name)) {
			linkPO.setPublished(property);
			if (property) {
				linkPO.setReleaseDate(DateUtil.nowDate());
				Information info = linkPO.getInformation();
				info.setReleaseDate(DateUtil.nowDate());
				info.saveOrUpdate();
			}
		}
		linkPO.saveOrUpdate();
		//更新索引
		return true ;
	}
	
}
