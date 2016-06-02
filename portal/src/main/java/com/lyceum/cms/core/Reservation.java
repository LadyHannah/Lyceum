package com.lyceum.cms.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zealyo.common.annotation.CName;
import com.zealyo.common.annotation.JsonProperty;
import com.zealyo.common.utils.DateUtil;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.PO;
import com.zealyo.jdbc.hibernate.DAOUtil;
import com.zealyo.jdbc.util.GridResult;
import com.zealyo.jdbc.util.PagingObject;
/**
 * Reservation List
 * 
 * @author Hannah
 *
 */
@Entity
@Table(name = "CMS_RESERVATION")
public class Reservation extends PO {
	
	private static final long serialVersionUID = 3721962709161520812L;

	@CName("Name")
	@Column(name = "NAME_", length = 20)
	@JsonProperty private String name;
	
	@CName("Language")
	@Column(name = "LANGUAGE")
	@JsonProperty private int language = 0;
	
	@CName("Email")
	@Column(name = "EMAIL", length = 50)
	@JsonProperty private String email;
	
	@CName("Phone")
	@Column(name = "PHONE", length = 50)
	@JsonProperty private String phone;
	
	@CName("Message")
	@Column(name = "MESSAGE", length = 3000)
	@JsonProperty private String message;
	
	@CName("Create_date")
	@Column(name = "CREATE_DATE")
	@JsonProperty private Date createDate = DateUtil.nowDate();


	@CName("Is_deleted")
	@Column(name = "IS_DELETED")
	private boolean deleted = false;
	
	public static Reservation get(String id) {
		return DAOUtil.get(Reservation.class, id);
	}

	/**
	 * Find GridResult<Reservation> by Name
	 * @param name name
	 * @param pagingObject pagingObject
	 * @return GridResult<Reservation>
	 */
	@SuppressWarnings("unchecked")
	public static GridResult<Reservation> findGridResultByName(String name, int language, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(Reservation.class.getName()).append(" po where po.deleted = false");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(name)) {
			query.append(" and po.name like :name");
			params.put("name", "%" + name + "%");
		}
		if (language > 0) {
			query.append(" and po.language = :language");
			params.put("language", language);
		}
		query.append(" order by po.createDate desc");
		List<Reservation> list = (List<Reservation>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return new GridResult<Reservation>(list, pagingObject);
	}
	
	/**
	 * Find List<Reservation> by Name
	 * @param name name
	 * @param pagingObject pagingObject
	 * @return GridResult<Reservation>
	 */
	@SuppressWarnings("unchecked")
	public static List<Reservation> findListbyName(String name, int language, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(Reservation.class.getName()).append(" po where po.deleted = false");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(name)) {
			query.append(" and po.name like :name");
			params.put("name", "%" + name + "%");
		}
		if (language > 0) {
			query.append(" and po.language = :language");
			params.put("language", language);
		}
		query.append(" order by po.createDate desc");
		return (List<Reservation>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
	}
	
	@JsonProperty
	public String getLanguageStr() {
		switch (this.language) {
		case 1:
			return "国际英语 ";
		case 2:
			return "日语";
		case 3:
			return "韩语";
		case 4:
			return "德语";
		case 5:
			return "法语";
		case 6:
			return "意大利语";
		case 7:
			return "西班牙语";
		default:
			break;
		}
		return "";
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
	 * @return the language
	 */
	public int getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(int language) {
		this.language = language;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
