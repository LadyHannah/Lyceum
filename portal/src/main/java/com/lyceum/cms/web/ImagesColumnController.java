package com.lyceum.cms.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyceum.cms.application.ImagesColumnApplication;
import com.lyceum.cms.core.ImagesColumn;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.util.PagingObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ImagesColumInfoController
 * @author Hannah
 *
 */
@Controller
@RequestMapping("cms/imagesColumn")
public class ImagesColumnController extends FreeMarkerController {

	@Autowired
	private ImagesColumnApplication imagesColumnApplication;
	
	/**
	 * Images Column index page
	 */
	@Override
	@RequestMapping("")
	public String index(HttpServletRequest request, ModelMap map) {
		return getViewName("ftls/imagesColumn/index");
	}
	
	/**
	 * Find images column
	 * @param parentId
	 * @param name
	 * @param pagingObject
	 * @return
	 */
	@RequestMapping(value = "find", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject find(String name, PagingObject pagingObject) {
		return ImagesColumn.find(name, pagingObject).toJsonObject();
	}
	
	/**
	 * Find the column tree
	 * @param pagingObject
	 * @return
	 */
	@RequestMapping(value = "tree", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findImagesColumnTree(String id, PagingObject pagingObject) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		
		if (StringUtil.isNullOrEmpty(id)) {
			jsonObj = new JSONObject();
			jsonObj.put("id", "root");
			jsonObj.put("text", "Lyceum");
			jsonObj.put("state", "open");
			
			JSONArray ja = new JSONArray();
			JSONObject jo = null;
			
			for (ImagesColumn po : ImagesColumn.findList(null, new PagingObject())) {
				jo = new JSONObject();
				jo.put("id", po.getId());
				jo.put("text", po.getName());
				jo.put("state", "closed");
				jo.put("children", "[]");
				ja.add(jo);
			}
			jsonObj.put("children", ja);
			jsonArray.add(jsonObj);
			
		} 
		return jsonArray;
	}
	
	/**
	 * Edit Images Column
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String editImagesColumn(@PathVariable String id, ModelMap map) {
		ImagesColumn po = null;
		if ("new".equals(id)) {
			po = new ImagesColumn();
			map.put("po", po);
		} else {
			po = ImagesColumn.get(id);
			map.put("po", po);
		}
		return getViewName("ftls/imagesColumn/edit");
	}
	
	/**
	 * Save or update
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public boolean editImagesColumn(ImagesColumn po) {
		return imagesColumnApplication.saveOrUpdate(po);
	}
	
	/**
	 * Delete Images Column
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteColumn(String ids) {
		return String.valueOf(imagesColumnApplication.delete(ids));
	}
	
}
