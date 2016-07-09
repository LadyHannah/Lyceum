package com.lyceum.cms.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyceum.cms.application.ColumnInfoApplication;
import com.lyceum.cms.core.ColumnInfo;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.util.PagingObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ColumInfoController
 * @author Hannah
 *
 */
@Controller
@RequestMapping("cms/column")
public class ColumnInfoController extends FreeMarkerController {

	@Autowired
	private ColumnInfoApplication columnInfoApplication;
	
	/**
	 * Column index page
	 */
	@Override
	@RequestMapping("")
	public String index(HttpServletRequest request, ModelMap map) {
		map.put("parentId", StringUtil.emptyToDefault(request.getParameter("parentId"), ""));
		return getViewName("ftls/column/index");
	}
	
	/**
	 * Find column tree
	 * @param parentId
	 * @param name
	 * @param pagingObject
	 * @return
	 */
	@RequestMapping(value = "find", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject find(String parentId, String name, PagingObject pagingObject) {
		if ("root".equals(parentId)) {
			parentId = "";
		}
		return ColumnInfo.find(parentId, name, pagingObject).toJsonObject();
	}
	
	/**
	 * Find the column tree
	 * @param pagingObject
	 * @return
	 */
	@RequestMapping(value = "tree", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findColumnTree(String id, PagingObject pagingObject) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		
		if (StringUtil.isNullOrEmpty(id)) {
			jsonObj = new JSONObject();
			jsonObj.put("id", "root");
			jsonObj.put("text", "Lyceum");
			jsonObj.put("state", "open");
			
			JSONArray ja = new JSONArray();
			JSONObject jo = null;
			
			for (ColumnInfo po : ColumnInfo.find(null)) {
				jo = new JSONObject();
				jo.put("id", po.getId());
				jo.put("text", po.getName());
				jo.put("state", "closed");
				jo.put("children", "[]");
				ja.add(jo);
			}
			jsonObj.put("children", ja);
			jsonArray.add(jsonObj);
			
		} else {
			for (ColumnInfo po : ColumnInfo.find(id)) {
				jsonObj = new JSONObject();
				jsonObj.put("id", po.getId());
				jsonObj.put("text", po.getName());
				jsonObj.put("state", "closed");
				jsonObj.put("children", "[]");
				jsonArray.add(jsonObj);
			}
		}
		return jsonArray;
	}
	
	/**
	 * Edit columnInfo
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String editColumn(@PathVariable String id, String parentId, ModelMap map) {
		ColumnInfo po = null;
		if ("new".equals(id)) {
			po = new ColumnInfo();
			po.setParent(ColumnInfo.get(parentId));
			map.put("po", po);
			map.put("parentId", parentId);
		} else {
			po = ColumnInfo.get(id);
			po.setKey(po.getShortKey());
			map.put("po", po);
			map.put("parentId", po.getParent() != null ? po.getParent().getId() : "");
		}
		return getViewName("ftls/column/edit");
	}
	
	/**
	 * Save or update
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public boolean editColumn(ColumnInfo po, String parentId) {
		if ("root".equals(parentId)) {
			parentId = "";
		}
		return columnInfoApplication.saveOrUpdate(po, parentId);
	}
	
	/**
	 * Delete Column
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteColumn(String ids) {
		return String.valueOf(columnInfoApplication.delete(ids));
	}
	
}
