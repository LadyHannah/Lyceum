package com.lyceum.cms.core;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.zealyo.common.annotation.JsonProperty;
import com.zealyo.common.utils.DateUtil;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.PO;
import com.zealyo.jdbc.hibernate.DAOUtil;
import com.zealyo.jdbc.util.GridResult;
import com.zealyo.jdbc.util.PagingObject;
/**
 * 目录
 * 
 * @author Hannah
 *
 */
@Entity
@Table(name = "CMS_COLUMN_INFO")
public class ColumnInfo extends PO {

	private static final long serialVersionUID = -6420650306966284110L;

	/**
	 * 目录名称
	 */
	@Column(name = "NAME_", length = 20)
	@JsonProperty private String name;
	
	/**
	 * 目录编号
	 */
	@Column(name = "COLUMN_KEY", length = 100)
	@JsonProperty private String key;
	
	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION", length = 400)
	@JsonProperty private String description;
	
	/**
	 * 配图地址
	 */
	@Column(name = "IMAGE_URL", length = 500)
	@JsonProperty private String imageUrl;
	
	/**
	 * 目录映射地址
	 */
	@Column(name = "LINK_URLS", length = 500)
	@JsonProperty private String linkUrl;
	
	/**
	 * 排序
	 */
	@Column(name = "ORDER_INDEX", length = 6)
	@JsonProperty private float orderIndex = 1000f;
	
	/**
	 * 归属站点Id
	 */
	@Column(name = "SITE_ID", length = 32)
	@JsonProperty private String siteId;
	
	/**
	 * 目录属性 
	 * 0 多篇信息（默认）
	 * 1 单篇信息
	 * 2 不可发布信息
	 * 
	 */
	@Column(name = "TYPE_")
	@JsonProperty private int type = 0;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	@JsonProperty private Date createDate = DateUtil.nowDate();

	/**
	 * 父目录
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PARENT_ID")
	@JsonProperty(type = "vo") private ColumnInfo parent;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy(value = "orderIndex ASC")
	private Set<ColumnInfo> childs = new HashSet<ColumnInfo>();

	/**
	 * 逻辑删除
	 */
	@Column(name = "IS_DELETED")
	private boolean deleted = false;
	
	public static ColumnInfo get(String id) {
		return DAOUtil.get(ColumnInfo.class, id);
	}
	
	/**
	 * 更新目录
	 * @param parentId
	 */
	public void saveOrUpdate(String parentId) {
		if (!StringUtil.isNullOrEmpty(parentId)) {
			this.setParent(ColumnInfo.get(parentId));
		}
		//更新目录key
		if (getParent() != null) {
			setKey(getParent().getKey() + "/" + getShortKey());
		}
		saveOrUpdate();
	}

	/**
	 * 查找目录列表
	 * @param parentId parentId
	 * @param name name
	 * @param pagingObject pagingObject
	 * @return GridResult<ColumnInfo>
	 */
	@SuppressWarnings("unchecked")
	public static  GridResult<ColumnInfo> find(String parentId, String name, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(ColumnInfo.class.getName()).append(" po where po.deleted = false");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(parentId)) {
			query.append(" and po.parent.id = :parent");
			params.put("parent", parentId);
		} else {
			query.append(" and ( po.parent is null or po.parent.id = '')");
		}
		if (!StringUtil.isNullOrEmpty(name)) {
			query.append(" and po.name like :name");
			params.put("name", "%" + name + "%");
		}
		query.append(" order by po.orderIndex");
		List<ColumnInfo> list = (List<ColumnInfo>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return new GridResult<ColumnInfo>(list, pagingObject);
	}
	
	/**
	 * 查找目录列表
	 * @param parentId parentId
	 * @return List<ColumnInfo>
	 */
	@SuppressWarnings("unchecked")
	public static List<ColumnInfo> find(String parentId) {
		StringBuilder query = new StringBuilder(" from ").append(ColumnInfo.class.getName()).append(" po where po.deleted = false ");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(parentId)) {
			query.append(" and po.parent.id = :parentId");
			params.put("parentId", parentId);
		} else {
			query.append(" and ( po.parent is null or po.parent.id = '')");
		}
		query.append(" order by po.orderIndex");
		return (List<ColumnInfo>) DAOUtil.findWithoutPaging(query.toString(), params);
	}

	/**
	 * 根据站点id，查找根目录
	 * @param siteId siteId
	 * @return List<ColumnInfo>
	 */
	@SuppressWarnings("unchecked")
	public static List<ColumnInfo> findRootBySiteId(String siteId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder(" from ").append(ColumnInfo.class.getName())
					.append(" po where po.deleted = false and (po.parent is null or po.parent.id = '') ")
					.append(" and po.siteId = :siteId");
		params.put("siteId", siteId);
		query.append(" order by po.orderIndex");
		return (List<ColumnInfo>) DAOUtil.findWithoutPaging(query.toString(), params);
	}
	
	/**
	 * 根据站点id，查找根目录
	 * @param name name
	 * @param siteId siteId
	 * @param pagingObject {@link PagingObject}
	 * @return List<ColumnInfo>
	 */
	@SuppressWarnings("unchecked")
	public static List<ColumnInfo> findRootBySiteId(String name, String siteId, PagingObject pagingObject) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder(" from ").append(ColumnInfo.class.getName())
				.append(" po where po.deleted = false and (po.parent is null or po.parent.id = '') ")
				.append(" and po.siteId = :siteId");
		params.put("siteId", siteId);
		
		if (!StringUtil.isNullOrEmpty(name)) {
			query.append(" and po.name like :name");
			params.put("name", "%" + name + "%");
		}
		query.append(" order by po.orderIndex");
		return (List<ColumnInfo>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
	}

	/**
	 * 判断目录是否已经存在
	 * @param key key
	 * @param parentId parentId
	 * @return true or false
	 */
	public static boolean isExit(String key, String parentId) {
		StringBuilder query = new StringBuilder("select count(*) from ").append(ColumnInfo.class.getName()).append(" po where po.deleted = false");
		Map<String, Object> params = new HashMap<String, Object>();
		query.append(" and po.columnKey = :columnKey");
		params.put("columnKey", key);
		if (!StringUtil.isNullOrEmpty(parentId)) {
			query.append(" and po.parent.id = :parentId");
			params.put("parentId", parentId);
		}
		return new Long(DAOUtil.count(query.toString(), params)).intValue() > 0;
	}
	
	/**
	 * 获取父目录的key
	 * @return
	 */
	@JsonProperty
	public String getParentId() {
		return this.parent == null ? "" : this.parent.getId();
	}
	
	/**
	 * 获取父目录的key
	 * @return
	 */
	public String getParentKey() {
		return this.parent == null ? "" : this.parent.getKey();
	}
	
	/**
	 * 目录属性 
	 * 0 多篇信息（默认）
	 * 1 单篇信息
	 * 2 不可发布信息
	 * 
	 */
	@JsonProperty
	public String getTypeStr() {
		switch (this.type) {
		case 0:
			return "多篇信息";
		case 1:
			return "单篇信息";
		case 2:
			return "不可发布信息";
		default:
			break;
		}
		return "";
	}
	
	/**
	 * 返回去除父目录key的key
	 * @return
	 */
	@JsonProperty
	public String getShortKey() {
		if (this.key.indexOf("/") >= 0) {
			return this.key.substring(this.key.lastIndexOf("/") + 1);
		} else {
			return this.key;
		}
	}

	/**
	 * @return the {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the {@link #name} to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the {@link #key}
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the {@link #key} to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the {@link #description}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the {@link #description} to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the {@link #imageUrl}
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the {@link #imageUrl} to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the {@link #linkUrl}
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * @param linkUrl the {@link #linkUrl} to set
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 * @return the {@link #orderIndex}
	 */
	public float getOrderIndex() {
		return orderIndex;
	}

	/**
	 * @param orderIndex the {@link #orderIndex} to set
	 */
	public void setOrderIndex(float orderIndex) {
		this.orderIndex = orderIndex;
	}

	/**
	 * @return the {@link #siteId}
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the {@link #siteId} to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the {@link #type}
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the {@link #type} to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the {@link #createDate}
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the {@link #createDate} to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the {@link #parent}
	 */
	public ColumnInfo getParent() {
		return parent;
	}

	/**
	 * @param parent the {@link #parent} to set
	 */
	public void setParent(ColumnInfo parent) {
		this.parent = parent;
	}

	/**
	 * @return the {@link #childs}
	 */
	public Set<ColumnInfo> getChilds() {
		return childs;
	}

	/**
	 * @param childs the {@link #childs} to set
	 */
	public void setChilds(Set<ColumnInfo> childs) {
		this.childs = childs;
	}

	/**
	 * @return the {@link #deleted}
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the {@link #deleted} to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
