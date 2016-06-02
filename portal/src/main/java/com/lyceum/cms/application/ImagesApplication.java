package com.lyceum.cms.application;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lyceum.cms.core.Images;
import com.zealyo.common.utils.StringUtil;


@Repository
@Transactional
public class ImagesApplication {
	
	/**
	 * Save or Update
	 * @param po
	 * @return
	 */
	public boolean saveOrUpdate(Images po) {
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
		Images po = null;
		for (String id : StringUtil.split(ids, ",")) {
			po = Images.get(id);
			if (null != po) {
				po.delete();
			}
		}
		return true;
	}
}
