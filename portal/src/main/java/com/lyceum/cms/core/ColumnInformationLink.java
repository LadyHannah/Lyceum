package com.lyceum.cms.core;

import java.util.Date;
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

import org.springframework.transaction.annotation.Transactional;

import com.zealyo.common.annotation.CName;
import com.zealyo.common.annotation.JsonProperty;
import com.zealyo.common.utils.DateUtil;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.PO;
import com.zealyo.jdbc.hibernate.DAOUtil;
import com.zealyo.jdbc.util.GridResult;
import com.zealyo.jdbc.util.PagingObject;

/**
 * Column & Information Link
 * 
 * @author Hannah
 *
 */
@Entity
@Table(name = "CMS_COLUMN_INFORMATION_LINK")
@Transactional
public class ColumnInformationLink extends PO {

	private static final long serialVersionUID = -2147867133464988658L;

	@CName("所属目录")
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "COLUMN_ID")
	@JsonProperty(type = "vo") private ColumnInfo columnInfo;
	
	@CName("信息主体ID")
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "INFO_ID")  
	@JsonProperty private Information information;

	@CName("信息标题")
	@Column(name = "TITLE_", length = 100)
	@JsonProperty private String title;
	
	@CName("信息摘要")
	@Column(name = "INFO_ABSTRACT", length = 5000)
	@JsonProperty private String infoAbstract;
	
	@CName("信息访问等级1.2.3.4.5")
	@Column(name = "INFO_LEVEL", length = 1)
	@JsonProperty private int infoLevel = 1;
	
	@CName("信息状态：0未发布 1发布（默认）2草稿箱 3失效 4待审核")
	@Column(name = "INFO_STATE", length = 1)
	@JsonProperty private int infoState = 1;
	
	@CName("链接路径，可以跳转到其他网站发布信息")
	@Column(name = "INFO_URL", length = 100)
	@JsonProperty private String infoUrl;
	
	@CName("发布时间")
	@Column(name = "RELEASE_DATE")
	@JsonProperty private Date releaseDate = DateUtil.nowDate();

	@CName("是否可留言")
	@Column(name = "IS_WORDLEAVED")
	@JsonProperty private boolean wordLeaved = false;
	
	@CName("是否置顶")
	@Column(name = "IS_TOPED")
	@JsonProperty private boolean toped = false;

	@CName("是否精华")
	@Column(name = "IS_MARROWED")
	@JsonProperty private boolean marrowed = false;
	
	@CName("是否热门")
	@Column(name = "IS_HOTED")
	@JsonProperty private boolean hoted = false;
	
	@CName("发布状态，默认不发布")
	@Column(name = "PUBLISHED")
	@JsonProperty private boolean published = false;

	@CName("是否可被共享")
	@Column(name = "IS_SHARE_STATE")
	@JsonProperty private boolean shareState = false;
	
	@CName("自动发布时间")
	@Column(name = "TIME_INTERVAL_DATE")
	@JsonProperty private Date timeIntervalDate;
	
	@CName("逻辑删除，用于被删除后还要恢复")
	@Column(name = "IS_DELETED")
	private boolean deleted = false;
	
	
	public static ColumnInformationLink get(String id) {
		return DAOUtil.get(ColumnInformationLink.class, id);
	}
	
	/**
	 * 查询信息
	 * @param title title
	 * @param columnId columnId
	 * @param pagingObject pagingObject
	 * @return public List<ColumnInformationLink>
	 */
	@SuppressWarnings("unchecked")
	public static GridResult<ColumnInformationLink> find(String title, String columnId, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(ColumnInformationLink.class.getName()).append(" po where po.deleted =false");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(title)) {
			query.append(" and po.title like :title");
			params.put("title", "%" + title + "%");
		}
		if (StringUtil.isNullOrEmpty(columnId)) {
			query.append(" and po.columnInfo.id = null");
		} else {
			query.append(" and po.columnInfo.id = :columnId");
			params.put("columnId", columnId);
		}
		query.append(" order by po.releaseDate desc");
		List<ColumnInformationLink> list = (List<ColumnInformationLink>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return new GridResult<ColumnInformationLink>(list, pagingObject);
	}
	
	/**
	 * 查询信息
	 * @param columnId columnId
	 * @param pagingObject pagingObject
	 * @return public List<ColumnInformationLink>
	 */
	@SuppressWarnings("unchecked")
	public static List<ColumnInformationLink> find(String columnId, PagingObject pagingObject) {
		StringBuilder query = new StringBuilder(" from ").append(ColumnInformationLink.class.getName()).append(" po where po.deleted =false");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(columnId)) {
			query.append(" and po.columnInfo.id = null");
		} else {
			query.append(" and po.columnInfo.id = :columnId");
			params.put("columnId", columnId);
		}
		query.append(" order by po.releaseDate desc");
		List<ColumnInformationLink> list = (List<ColumnInformationLink>) DAOUtil.findWithPaging(pagingObject, query.toString(), params);
		return list;
	}
	
	/**
	 * 根据信息id查询
	 * @param infoId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ColumnInformationLink> find(String infoId) {
		StringBuilder query = new StringBuilder(" from ").append(ColumnInformationLink.class.getName()).append(" po where 1 = 1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(infoId)) {
			query.append(" and po.information.id = :infoId");
			params.put("infoId", infoId);
		}
		return (List<ColumnInformationLink>) DAOUtil.findWithoutPaging(query.toString(), params);
	}
	
	@JsonProperty
	public String getReleaseDateStr() {
		return DateUtil.formatDateTime(this.releaseDate);
	}
	
	/**
	 * 获取目录的ID
	 * @return
	 */
	public String getColumnId() {
		return this.columnInfo == null ? "" : this.columnInfo.getId();
	}
	
	/**
	 * 获取目录的name
	 * @return
	 */
	public String getColumnName() {
		return this.columnInfo == null ? "" : this.columnInfo.getName();
	}
	
	/**
	 * 获取目录的key
	 * @return
	 */
	public String getColumnKey() {
		return this.columnInfo == null ? "" : this.columnInfo.getKey();
	}
	
	/**
	 * 获取信息的author
	 * @return
	 */
	public String getAuthor() {
		return this.information == null ? "" : this.information.getAuthor();
	}
	
	/**
	 * 获取信息的content
	 * @return
	 */
	public String getContent() {
		return this.information == null ? "" : this.information.getContent();
	}
	
	/**
	 * 获取信息的imgUrl
	 * @return
	 */
	public String getImgUrls() {
		return this.information == null ? "" : this.information.getImgUrl();
	}
	
	/**
	 * 获取信息的id
	 * @return
	 */
	public String getInformationId() {
		return this.information == null ? "" : this.information.getId();
	}
	/**
	 * 获取信息的keyword
	 * @return
	 */
	public String getInfoKeywords() {
		return this.information == null ? "" : this.information.getKeyWord();
	}
	
	/**
	 * 获取信息的type
	 * @return
	 */
	public String getType() {
		return this.information == null ? "" : this.information.getTypeSTD();
	}

	
	/**
	 * 获取信息的sourceName
	 * @return
	 */
	public String getSource() {
		return this.information == null ? "" : this.information.getSourceName();
	}
	
	/**
	 * 获取信息的subTitle
	 * @return
	 */
	public String getSubTitle() {
		return this.information == null ? "" : this.information.getSubTitle();
	}
	
	/**
	 * 获取信息的infoUrl
	 * @return
	 */
	public String getUrl() {
		return this.information == null ? "" : this.information.getInfoUrl();
	}
	
	/**
	 * 获取信息的release
	 * @return
	 */
	public String getReleaseStr() {
		return this.information == null ? "" : this.information.getRelease();
	}
	
	/**
	 * 获取信息的abstracts
	 * @return
	 */
	public String getAbstracts() {
		return this.information == null ? "" : this.information.getInfoAbstract();
	}
	
	/**
	 * @return the {@link #columnInfo}
	 */
	public ColumnInfo getColumnInfo() {
		return columnInfo;
	}
	
	/**
	 * @param columnInfo the {@link #columnInfo} to set
	 */
	public void setColumnInfo(ColumnInfo columnInfo) {
		this.columnInfo = columnInfo;
	}

	/**
	 * @return the {@link #information}
	 */
	public Information getInformation() {
		return information;
	}

	/**
	 * @param information the {@link #information} to set
	 */
	public void setInformation(Information information) {
		this.information = information;
	}

	/**
	 * @return the {@link #title}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the {@link #title} to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the {@link #infoAbstract}
	 */
	public String getInfoAbstract() {
		return infoAbstract;
	}

	/**
	 * @param infoAbstract the {@link #infoAbstract} to set
	 */
	public void setInfoAbstract(String infoAbstract) {
		this.infoAbstract = infoAbstract;
	}

	/**
	 * @return the {@link #infoLevel}
	 */
	public int getInfoLevel() {
		return infoLevel;
	}

	/**
	 * @param infoLevel the {@link #infoLevel} to set
	 */
	public void setInfoLevel(int infoLevel) {
		this.infoLevel = infoLevel;
	}

	/**
	 * @return the {@link #infoState}
	 */
	public int getInfoState() {
		return infoState;
	}

	/**
	 * @param infoState the {@link #infoState} to set
	 */
	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}

	/**
	 * @return the {@link #infoUrl}
	 */
	public String getInfoUrl() {
		return infoUrl;
	}

	/**
	 * @param infoUrl the {@link #infoUrl} to set
	 */
	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	/**
	 * @return the {@link #releaseDate}
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate the {@link #releaseDate} to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the {@link #wordLeaved}
	 */
	public boolean isWordLeaved() {
		return wordLeaved;
	}

	/**
	 * @param wordLeaved the {@link #wordLeaved} to set
	 */
	public void setWordLeaved(boolean wordLeaved) {
		this.wordLeaved = wordLeaved;
	}

	/**
	 * @return the {@link #toped}
	 */
	public boolean isToped() {
		return toped;
	}

	/**
	 * @param toped the {@link #toped} to set
	 */
	public void setToped(boolean toped) {
		this.toped = toped;
	}

	/**
	 * @return the {@link #marrowed}
	 */
	public boolean isMarrowed() {
		return marrowed;
	}

	/**
	 * @param marrowed the {@link #marrowed} to set
	 */
	public void setMarrowed(boolean marrowed) {
		this.marrowed = marrowed;
	}

	/**
	 * @return the {@link #hoted}
	 */
	public boolean isHoted() {
		return hoted;
	}

	/**
	 * @param hoted the {@link #hoted} to set
	 */
	public void setHoted(boolean hoted) {
		this.hoted = hoted;
	}

	/**
	 * @return the {@link #published}
	 */
	public boolean isPublished() {
		return published;
	}

	/**
	 * @param published the {@link #published} to set
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}

	/**
	 * @return the {@link #shareState}
	 */
	public boolean isShareState() {
		return shareState;
	}

	/**
	 * @param shareState the {@link #shareState} to set
	 */
	public void setShareState(boolean shareState) {
		this.shareState = shareState;
	}

	/**
	 * @return the {@link #timeIntervalDate}
	 */
	public Date getTimeIntervalDate() {
		return timeIntervalDate;
	}

	/**
	 * @param timeIntervalDate the {@link #timeIntervalDate} to set
	 */
	public void setTimeIntervalDate(Date timeIntervalDate) {
		this.timeIntervalDate = timeIntervalDate;
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
