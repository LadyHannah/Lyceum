package com.lyceum.cms.application;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lyceum.cms.core.Images;
import com.lyceum.cms.core.ImagesColumn;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.util.PagingObject;


@Repository
@Transactional
public class ImagesColumnApplication {
	
	/**
	 * Save or Update
	 * @param po
	 * @return
	 */
	public boolean saveOrUpdate(ImagesColumn po) {
		if (null == po) {
			return false;
		}
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
		ImagesColumn columnPO = null;
		for (String id : StringUtil.split(ids, ",")) {
			List<Images> imagesList = Images.findList(null, id, new PagingObject());  // find list by images column id
			for (Images imagesPO : imagesList) {
				if (null != imagesPO) {
					imagesPO.delete();    //delete images belongs to the column
				}
			}
			
			columnPO = ImagesColumn.get(id);
			if (null != columnPO) {
				columnPO.delete();  // delete images column
			}
		}
		return true;
	}
    
}
