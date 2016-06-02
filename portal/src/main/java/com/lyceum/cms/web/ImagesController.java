package com.lyceum.cms.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lyceum.cms.application.ImagesApplication;
import com.lyceum.cms.core.Images;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.jdbc.util.PagingObject;

import net.sf.json.JSONObject;

/**
 * Images controller
 * @author Hannah
 *
 */
@Controller
@RequestMapping("cms/images")
public class ImagesController extends FreeMarkerController {

	@Value("#{settings['filePath']}")
	private String filePath;
	@Autowired
	private ImagesApplication imagesApplication;
	
	@Override
	@RequestMapping("")
	public String index(HttpServletRequest request, ModelMap map) {
		List<Images> parentList = Images.findListByName(null, null, new PagingObject());
		map.put("parentList", parentList);
		return getViewName("ftls/images/index");
	}
	
	/**
	 * 查找子栏目
	 * @param parentTitle
	 * @param pagingObject
	 * @return
	 */
	@RequestMapping(value = "find", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject find(String parentTitle,PagingObject pagingObject) {
		return Images.findGridResultByName(parentTitle, null, pagingObject).toJsonObject();
	}
	
	/**
	 * 新增、编辑信息
	 * @param id
	 * @param columnId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable String id, String parentTitle, ModelMap map) {
		map.put("parentTitle", parentTitle);
		Images po = null;
		if ("new".equals(id)) {
			po = new Images();
			po.setParentTitle(parentTitle);
			map.put("po", po);
		} else {
			po = Images.get(id);
			map.put("po", po);
		}
		return getViewName("ftls/images/edit");
	}
	
	/**
	 * 查看
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, ModelMap map) {
		Images po = Images.get(id);
		map.put("po", po);
		return getViewName("ftls/images/view");
	}
	
	/**
	 * Save or Update Images Info
	 * @param po
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public boolean edit(Images po ,@RequestParam(value="file",required=false) MultipartFile file,
            HttpServletRequest request) throws Exception{
        String path="";
        if(!file.isEmpty()){
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType=file.getContentType();
            //获得文件后缀名称
            String extensionName=contentType.substring(contentType.indexOf("/")+1);
            path=uuid+"."+extensionName;
            
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(filePath+path)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
            	e.getMessage();
                return false;
            }
        }
        po.setImgUrl(path);
		return imagesApplication.saveOrUpdate(po);
	}
	
	/**
	 * 删除信息
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteInfo(String ids) {
		return String.valueOf(imagesApplication.delete(ids));
	}
	
	/**
	 * 读取图片
	 * @param request
	 * @param response
	 * @param imgUrl  图片路径(a.jpg)
	 */
	@RequestMapping("/show")
    @ResponseBody
    public void showImage(HttpServletRequest request, HttpServletResponse response, String imgUrl){
    	//response.setContentType("text/html; charset=UTF-8");
        response.setContentType("image/*");
        FileInputStream fis = null; 
        OutputStream os = null; 
        try {
        	fis = new FileInputStream(filePath+imgUrl);
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
