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
 * Images Column Entity
 * @author Hannah
 *
 */
@Entity
@Table(name = "CMS_IMAGES_COLUMN")
public class ImagesColumn extends PO {

	private static final long serialVersionUID = 1199674727463649850L;
	
	@CName("Name")
	@Column(name = "NAME_", length = 20)
	@JsonProperty private String name;
	
	@CName("Column key")
	@Column(name = "COLUMN_KEY", length = 100)
	@JsonProperty private String key;
	
	@CName("OrderIndex")
	@Column(name = "ORDER_INDEX", length = 6)
	@JsonProperty private float orderIndex = 1000f;
	
	public static ImagesColumn get(String id) {
		return DAOUtil.get(ImagesColumn.class, id);
	}

	/**
	 * Find GridResult<ImagesColumn>
	 * @param name
	 * @param pagingObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GridResult<ImagesColumn> find(String name, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(ImagesColumn.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(name)) {
			query.append(" and po.name like :name");
			params.put("name", "%" + name + "%");
		}
		query.append(" order by po.orderIndex");
		List<ImagesColumn> list = (List<ImagesColumn>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return new GridResult<ImagesColumn>(list, pagingObject);
	}
	
	/**
	 * Find List<ImagesColumn>
	 * @param name
	 * @param pagingObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ImagesColumn> findList(String name, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(ImagesColumn.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(name)) {
			query.append(" and po.name like :name");
			params.put("name", "%" + name + "%");
		}
		query.append(" order by po.orderIndex asc");
		return (List<ImagesColumn>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
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
