package com.lyceum.cms.application;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lyceum.cms.core.ColumnInfo;
import com.lyceum.cms.core.ColumnInformationLink;
import com.lyceum.cms.core.Information;
import com.zealyo.common.utils.DateUtil;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.hibernate.DAOUtil;

/**
 * 信息管理业务类
 * @author Hannah
 *
 */
@Repository
@Transactional
public class InformationApplication {
	
	private static final Logger log = Logger.getLogger(InformationApplication.class);
	
	/**
	 * 保存更新
	 * @param po
	 * @return
	 */
	public boolean saveOrUpdate(Information po, String columnId, String release, String create) {
		if (null == po) {
			return false;
		}
		if (StringUtil.isNullOrEmpty(po.getId())){
			po.setCreateTime(DateUtil.parseDateTime(create));
			po.setReleaseDate(DateUtil.parseDateTime(release));
		} else {
			po.setCreateTime(DateUtil.nowDate());
		}
		log.info("信息开始保存");
		DAOUtil.saveOrUpdate(po);
		log.info("信息保存成功");
		List<ColumnInformationLink> list = ColumnInformationLink.find(po.getId());
		log.info("查询已有link:" + list.size());
		if (list.size() > 0) {
			for (ColumnInformationLink linkPO : list) {
				if (null != linkPO) {
					linkPO.setTitle(po.getTitle());
					linkPO.setInfoLevel(po.getInfoLevel());
					linkPO.setToped(po.isToped());
					linkPO.setReleaseDate(DateUtil.parseDate(release));
					linkPO.setTimeIntervalDate(po.getCreateTime());
					log.info("更新link");
					linkPO.update();
					log.info("更新link成功");
				}
			}
		} else {
			if (!StringUtil.isNullOrEmpty(columnId)) {  //归属目录不为空
				ColumnInformationLink linkPO = new ColumnInformationLink();
				ColumnInfo column = (ColumnInfo) DAOUtil.get(ColumnInfo.class, columnId);
				if (null != column) {
					linkPO.setColumnInfo(column);
					linkPO.setInformation(po);
					linkPO.setTitle(po.getTitle());
					linkPO.setInfoLevel(po.getInfoLevel());
					linkPO.setToped(po.isToped());
					linkPO.setReleaseDate(DateUtil.parseDateTime(release));
					log.info("新增link");
					linkPO.save();
					log.info("新增link成功");
				}
			} else {    //归属目录为空
				ColumnInformationLink linkPO = new ColumnInformationLink();
				linkPO.setInformation(po);
				linkPO.setTitle(po.getTitle());
				linkPO.setInfoLevel(po.getInfoLevel());
				linkPO.setToped(po.isToped());
				linkPO.setReleaseDate(DateUtil.parseDateTime(release));
				log.info("新增link");
				linkPO.save();
				log.info("新增link成功");
			}
		}
		return true;
	}
	
	/**
	 * 逻辑删除信息
	 * @param ids ids
	 * @return	true or false
	 */
	public boolean delete(String ids) {
		if (StringUtil.isNullOrEmpty(ids)) {
			return false;
		}
		ColumnInformationLink po = null;
		for (String id : StringUtil.split(ids, ",")) {
			po = ColumnInformationLink.get(id);
			if (null != po) {
				po.setColumnInfo(null);
				po.setInformation(null);
				po.delete();
			}
		}
		return true;
	}
}
