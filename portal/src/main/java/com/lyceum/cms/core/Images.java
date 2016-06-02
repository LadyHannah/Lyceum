package com.lyceum.cms.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zealyo.common.annotation.CName;
import com.zealyo.common.annotation.JsonProperty;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.PO;
import com.zealyo.jdbc.hibernate.DAOUtil;
import com.zealyo.jdbc.util.GridResult;
import com.zealyo.jdbc.util.PagingObject;
/**
 * Images List
 * 
 * @author Hannah
 *
 */
@Entity
@Table(name = "CMS_IMAGES")
public class Images extends PO {

	private static final long serialVersionUID = -7926015978443790870L;

	@CName("Parent Title")
	@Column(name = "PARENT_TITLE", length = 20)
	@JsonProperty private String parentTitle;
	
	@CName("Title")
	@Column(name = "TITLE_", length = 20)
	@JsonProperty private String title;
	
	@CName("Image URL")
	@Column(name = "IMG_URL")
	@JsonProperty private String imgUrl;
	
	
	@Column(name = "TITLE_KEY", length = 100)
	@JsonProperty private String titleKey;
	
	@CName("orderIndex")
	@Column(name = "ORDER_INDEX", length = 6)
	@JsonProperty private float orderIndex = 1000f;
	
	public static Images get(String id) {
		return DAOUtil.get(Images.class, id);
	}

	/**
	 * Find GridResult<Images>
	 * @param parentTitle
	 * @param titleKey
	 * @param pagingObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GridResult<Images> findGridResultByName(String parentTitle, String titleKey, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(Images.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(parentTitle)) {
			query.append(" and po.parentTitle like :parentTitle");
			params.put("parentTitle", "%" + parentTitle + "%");
		} else {
			query.append(" and ( po.parentTitle is null or po.parentTitle = '')");
		}
		if (!StringUtil.isNullOrEmpty(titleKey)) {
			query.append(" and po.titleKey = :titleKey");
			params.put("titleKey", titleKey);
		}
		query.append(" order by po.orderIndex asc");
		List<Images> list = (List<Images>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return new GridResult<Images>(list, pagingObject);
	}
	
	/**
	 * Find List<Images>
	 * @param parentTitle
	 * @param titleKey
	 * @param pagingObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Images> findListByName(String parentTitle, String titleKey, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(Images.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(parentTitle)) {
			query.append(" and po.parentTitle like :parentTitle");
			params.put("parentTitle", "%" + parentTitle + "%");
		} else {
			query.append(" and ( po.parentTitle is null or po.parentTitle = '')");
		}
		if (!StringUtil.isNullOrEmpty(titleKey)) {
			query.append(" and po.titleKey = :titleKey");
			params.put("titleKey", titleKey);
		}
		query.append(" order by po.orderIndex asc");
		return (List<Images>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
	}

	/**
	 * @return the parentTitle
	 */
	public String getParentTitle() {
		return parentTitle;
	}

	/**
	 * @param parentTitle the parentTitle to set
	 */
	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @return the titleKey
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * @param titleKey the titleKey to set
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/**
	 * @return the orderIndex
	 */
	public float getOrderIndex() {
		return orderIndex;
	}

	/**
	 * @param orderIndex the orderIndex to set
	 */
	public void setOrderIndex(float orderIndex) {
		this.orderIndex = orderIndex;
	}
	
}
