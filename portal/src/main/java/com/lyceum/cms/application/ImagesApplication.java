package com.lyceum.cms.application;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lyceum.cms.core.Images;
import com.zealyo.common.utils.StringUtil;


@Repository
@Transactional
public class ImagesApplication {
	
	@Value("#{settings['filePath']}")
	private  String filePath;
	
	/**
	 * Save or Update
	 * @param po
	 * @return
	 */
	public boolean saveOrUpdate(Images po) {
		if (null == po) {
			return false;
		}
	//	po.setImagesColumn(ImagesColumn.get(imagesColumnId));
		po.saveOrUpdate();
		return true;
	}
	
	/**
	 * Mass Delete
	 * @param ids ids
	 * @return	true or false
	 */
	public boolean delete(String ids) {
		if (StringUtil.isNullOrEmpty(ids)) {
			return false;
		}
		Images po = null;
		for (String id : StringUtil.split(ids, ",")) {
			po = Images.get(id);
			if (null != po) {
		        //String envVar = this.getClass().getResource("/common").getPath();   //Local environment
		    	//String envVar = System.getenv("OPENSHIFT_DATA_DIR") + "/images/";  //server environment
				//构建文件目录 
		        File fileDir = new File(filePath+ po.getImgUrl());
		        if (fileDir.exists()) {
		            //把之前的图片删除 以免太多没用的图片占据空间
		            fileDir.delete();
		        }
				po.delete();
			}
		}
		return true;
	}
	
	/**
	 * Name image name as date+5randoms
	 * 图片以“年月日时分+5位随机数”命名
	 * @return
	 */
    public String getRandomFileName() {  
  
        SimpleDateFormat simpleDateFormat;  
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");  
        Date date = new Date();  
        String str = simpleDateFormat.format(date); 
        
        Random random = new Random();  
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数  
        return str + rannum;// 当前时间  
    }
    
}
