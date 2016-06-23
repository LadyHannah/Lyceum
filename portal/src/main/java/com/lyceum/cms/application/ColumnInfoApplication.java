package com.lyceum.cms.application;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lyceum.cms.core.ColumnInfo;
import com.lyceum.cms.core.ColumnInformationLink;
import com.lyceum.cms.core.Information;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.util.PagingObject;

/**
 * 目录业务管理
 * @author Hannah
 *
 */
@Repository
@Transactional
public class ColumnInfoApplication {
	
	/**
	 * 保存更新
	 * @param po
	 * @return
	 */
	public boolean saveOrUpdate(ColumnInfo po, String parentId) {
		if (null == po) {
			return false;
		}
		po.saveOrUpdate(parentId);
		return true;
	}
	
	/**
	 * 逻辑删除目录信息
	 * @param ids ids
	 * @return	true or false
	 */
	public boolean delete(String ids) {
		if (StringUtil.isNullOrEmpty(ids)) {
			return false;
		}
		ColumnInfo po = null;
		for (String id : StringUtil.split(ids, ",")) {
			po = ColumnInfo.get(id);
			List<ColumnInformationLink> linkPO = ColumnInformationLink.find(id, new PagingObject());
			for (ColumnInformationLink link :linkPO) {
				link.setColumnInfo(null);
				Information info = Information.get(link.getInformationId());
				info.delete();
				link.delete();
			}
			if (null != po) {
				po.delete();
			}
		}
		return true;
	}
}

