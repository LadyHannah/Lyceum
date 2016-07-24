package com.lyceum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyceum.cms.core.ColumnInfo;
import com.lyceum.cms.core.ColumnInformationLink;
import com.lyceum.cms.core.Images;
import com.lyceum.cms.core.ImagesColumn;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.hibernate.DAOUtil;
import com.zealyo.jdbc.util.PagingObject;

@Controller
public class IndexController extends FreeMarkerController{

	protected static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Value("#{settings['filePath']}")
	private  String filePath;
	
	@RequestMapping("")
	public String index(HttpServletRequest request, ModelMap params) {
		log.info("Enter Home Page");
		//Paging
		PagingObject paging = new PagingObject();
		int curPage = StringUtil.parseInt(request.getParameter("currentPage"), 1);
		if (curPage < 1) { curPage = 1; }
		params.put("curPage", curPage);
		paging.setCurPage(curPage);
		paging.setPerPageRow(8);
		
		ColumnInfo gjyy = DAOUtil.getByKey(ColumnInfo.class, "sy/gjyy");
		params.put("gjyy", gjyy); //国际英语
		ColumnInfo xyz = DAOUtil.getByKey(ColumnInfo.class, "sy/xyz");
		params.put("xyz", xyz); //小语种
		ColumnInfo tdjs = DAOUtil.getByKey(ColumnInfo.class, "sy/tdjs");
		params.put("tdjs", tdjs); //团队介绍
		ColumnInfo zsal = DAOUtil.getByKey(ColumnInfo.class, "sy/zsal");
		params.put("zsal", zsal); //真实案例
		ColumnInfo yyst = DAOUtil.getByKey(ColumnInfo.class, "sy/yyst");
		params.put("yyst", yyst); //预约试听
		
		paging.setPerPageRow(2);
		List<ColumnInformationLink> article = ColumnInformationLink.find(gjyy.getId(), paging);
		params.put("article", article);
		
		ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "sy");
		params.put("imagesColumnId", imagesColumnPO.getId());
		
		return getViewName("index");
	}
	
	/**
	 * Seconde page
	 * @param key
	 * @param request
	 * @param paging
	 * @param map
	 * @return
	 */
	@RequestMapping("col/{key}")
	public String col(@PathVariable String key, HttpServletRequest request, PagingObject paging, ModelMap map) {
		map.put("colKey", key.trim());
		key = key.trim().replaceAll("-", "/");
		map.put("columnKey", key);
		//paging
		int curPage = StringUtil.parseInt(request.getParameter("currentPage"), 1);
		if (curPage < 1) { curPage = 1; }
		map.put("curPage", curPage);
		paging.setCurPage(curPage);
		paging.setPerPageRow(8);
		
		if (key.startsWith("gjyy")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "gjyy");
			map.put("imagesColumnId", imagesColumnPO.getId());
			ColumnInfo gjyy = DAOUtil.getByKey(ColumnInfo.class, "gjyy");
			List<ColumnInformationLink> gjyyList = ColumnInformationLink.find(gjyy.getId(), paging);
			map.put("gjyyList", gjyyList);
			return getViewName("info/english");
		}
		if (key.equals("xyz")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "xyz");
			map.put("imagesColumnId", imagesColumnPO.getId());
			ColumnInfo xyz = DAOUtil.getByKey(ColumnInfo.class, "xyz");
			List<ColumnInformationLink> xyzList = ColumnInformationLink.find(xyz.getId(), paging);
			map.put("xyzList", xyzList);
			return getViewName("info/minority");
		}
		if (key.startsWith("tdjs")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "tdjs");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/team");
		}
		if (key.startsWith("xyz/japanese")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "japanese");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/japanese");
		}
		if (key.startsWith("xyz/korea")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "korea");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/korea");
		}
		if (key.startsWith("xyz/germany")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "germany");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/germany");
		}
		if (key.startsWith("xyz/italy")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "italy");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/italy");
		}
		if (key.startsWith("xyz/spanish")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "spanish");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/spanish");
		}
		if (key.startsWith("xyz/french")) {
			ImagesColumn imagesColumnPO = DAOUtil.getByKey(ImagesColumn.class, "french");
			map.put("imagesColumnId", imagesColumnPO.getId());
			return getViewName("info/french");
		}
		return getViewName("info/english");
	}
	
	/**
	 * 读取图片
	 * @param request
	 * @param response
	 * @param imgUrl  图片路径(a.jpg)
	 */
	@RequestMapping("/showImage")
    @ResponseBody
    public void showImage(HttpServletRequest request, HttpServletResponse response, 
    						String imagesColumnId, String imgNumber, String imgUrl){
		List<Images> imagesList = Images.getByImgNumber(imgNumber, imagesColumnId);
		for (Images images : imagesList) {
			//response.setContentType("text/html; charset=UTF-8");
			response.setContentType("image/*");
			FileInputStream fis = null; 
			OutputStream os = null; 
			//获取当前服务器地址
			//String envVar = this.getClass().getResource("/common").getPath();   //Local environment
			//String envVar = System.getenv("OPENSHIFT_DATA_DIR") + "/images/";  //server environment
			try {
				fis = new FileInputStream(filePath + images.getImgUrl());
				os = response.getOutputStream();
				int count = 0;
				int i = fis.available();
				byte[] buffer = new byte[i];
				while ( (count = fis.read(buffer)) != -1 ){
					os.write(buffer, 0, count);
					os.flush();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				try {
					fis.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
    }
	
}
