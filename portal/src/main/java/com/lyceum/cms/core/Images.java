package com.lyceum.cms.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	private static final long serialVersionUID = 1294141367901708057L;

	@CName("Images Column")
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "IMAGES_COLUMN_ID")
	@JsonProperty(type = "vo") private ImagesColumn imagesColumn;

	@CName("Title")
	@Column(name = "TITLE_", length = 20)
	@JsonProperty private String title;
	
	@CName("Image URL")
	@Column(name = "IMG_URL")
	@JsonProperty private String imgUrl;
	
	@CName("Image Number")
	@Column(name = "IMG_NUMBER", length = 10)
	@JsonProperty private String imgNumber;
	
	public static Images get(String id) {
		return DAOUtil.get(Images.class, id);
	}
	
	/**
	 * Find List<Images>
	 * @param imgNumber
	 * @param imagesColumnId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Images> getByImgNumber(String imgNumber, String imagesColumnId) {
		StringBuilder query = new StringBuilder(" from ").append(Images.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(imgNumber)) {
			query.append(" and po.imgNumber = :imgNumber");
			params.put("imgNumber", imgNumber);
		}
		if (StringUtil.isNullOrEmpty(imagesColumnId)) {
			query.append(" and po.imagesColumn.id = null");
		} else {
			query.append(" and po.imagesColumn.id = :imagesColumn");
			params.put("imagesColumn", imagesColumnId);
		}
		query.append(" order by po.imgNumber asc");
		return (List<Images>) DAOUtil.findWithoutPaging(query.toString(), params);
	}

	/**
	 * Find GridResult<Images>
	 * @param title
	 * @param imagesColumnId
	 * @param pagingObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GridResult<Images> findGridResultByName(String title, String imagesColumnId, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(Images.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(title)) {
			query.append(" and po.title like :title");
			params.put("title", "%" + title + "%");
		} 
		if (StringUtil.isNullOrEmpty(imagesColumnId)) {
			query.append(" and po.imagesColumn.id = null");
		} else {
			query.append(" and po.imagesColumn.id = :imagesColumn");
			params.put("imagesColumn", imagesColumnId);
		}
		query.append(" order by po.imgNumber asc");
		List<Images> list = (List<Images>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return new GridResult<Images>(list, pagingObject);
	}
	
	/**
	 * Find List<Images>
	 * @param title
	 * @param imagesColumnId
	 * @param pagingObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Images> findList(String title, String imagesColumnId, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(Images.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(title)) {
			query.append(" and po.title like :title");
			params.put("title", "%" + title + "%");
		}
		if (StringUtil.isNullOrEmpty(imagesColumnId)) {
			query.append(" and po.imagesColumn.id = null");
		} else {
			query.append(" and po.imagesColumn.id = :imagesColumn");
			params.put("imagesColumn", imagesColumnId);
		}
		query.append(" order by po.imgNumber asc");
		return (List<Images>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
	}

	/**
	 * @return the imagesColumn
	 */
	public ImagesColumn getImagesColumn() {
		return imagesColumn;
	}

	/**
	 * @param imagesColumn the imagesColumn to set
	 */
	public void setImagesColumn(ImagesColumn imagesColumn) {
		this.imagesColumn = imagesColumn;
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
	 * @return the imgNumber
	 */
	public String getImgNumber() {
		return imgNumber;
	}

	/**
	 * @param imgNumber the imgNumber to set
	 */
	public void setImgNumber(String imgNumber) {
		this.imgNumber = imgNumber;
	}

	

}
