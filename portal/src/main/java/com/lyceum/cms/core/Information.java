package com.lyceum.cms.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
 * Information entity
 * 
 * @author Hannah
 *
 */
@Entity
@Table(name = "CMS_INFORMATION")
public class Information extends PO {

	private static final long serialVersionUID = -380664030568526706L;
	
	@CName("标题")
	@Column(name = "TITLE_", length = 100)
	@JsonProperty private String title;
	
	@CName("副标题")
	@Column(name = "SUB_TITLE", length = 100)
	@JsonProperty private String subTitle;

	@CName("信息摘要")
	@Column(name = "INFO_ABSTRACT", length = 100)
	@JsonProperty private String infoAbstract;
	
	@CName("作者")
	@Column(name = "AUTHOR_", length = 100)
	@JsonProperty private String author;

	@CName("信息内容")
	@Lob
	@Column(name = "CONTENT_")
	@JsonProperty private String content;
	
	@CName("信息文号")
	@Column(name = "FILE_NUMBER", length = 100)
	@JsonProperty private String fileNumber;

	@CName("配图地址")
	@Column(name = "IMG_URL", length = 100)
	@JsonProperty private String imgUrl;
	
	/**
	 * 信息等级对应用户权限不同的等级供不同的用户查看
	 */
	@CName("信息等级")
	@Column(name = "INFO_LEVEl", length = 1)
	@JsonProperty private int infoLevel = 1;
	
	@CName("关键词")
	@Column(name = "KEY_WORD", length = 1000)
	@JsonProperty private String keyWord;

	@CName("内容分类")
	@Column(name = "TYPE_STD", length = 100)
	@JsonProperty private String typeSTD;

	@CName("是否置顶，默认否")
	@Column(name = "IS_TOPED")
	@JsonProperty private boolean toped = false;
	
	@CName("是否可留言")
	@Column(name = "IS_WORDLEAVED")
	@JsonProperty private boolean wordLeaved = false;
	
	/**
	 * 链接路径，可以跳转到其他网站发布信息
	 */
	@CName("链接路径")
	@Column(name = "INFO_URL", length = 100)
	@JsonProperty private String infoUrl;

	@CName("来源名称")
	@Column(name = "SOURCE_NAME", length = 100)
	@JsonProperty private String sourceName;
	
	@CName("最后修改时间")
	@Column(name = "CREATE_TIME")
	@JsonProperty private Date createTime = DateUtil.nowDate();

	@CName("发布时间")
	@Column(name = "RELEASE_DATE")
	@JsonProperty private Date releaseDate = DateUtil.nowDate();
	
	@JsonProperty
	public String getCreate() {
		return DateUtil.formatDateTime(createTime);
	}
	
	@JsonProperty
	public String getRelease() {
		return DateUtil.formatDateTime(releaseDate);
	}

	/**
	 * 返回实体
	 * @param id
	 * @return
	 */
	public static Information get(String id) {
		return DAOUtil.get(Information.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public static GridResult<Information> find(String title, PagingObject paging) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder(" from ").append(Information.class.getName()).append(" po where 1=1 ");
		if (!StringUtil.isNullOrEmpty(title)) {
			query.append(" and po.title like :title");
			params.put("title", "%" + title + "%");
		}
		query.append(" order by po.releaseDate desc");
		return new GridResult<Information>((List<Information>) DAOUtil.findWithPaging(paging, query.toString(), params), paging);
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
	 * @return the {@link #subTitle}
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * @param subTitle the {@link #subTitle} to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
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
	 * @return the {@link #author}
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the {@link #author} to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the {@link #content}
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the {@link #content} to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the {@link #fileNumber}
	 */
	public String getFileNumber() {
		return fileNumber;
	}

	/**
	 * @param fileNumber the {@link #fileNumber} to set
	 */
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	/**
	 * @return the {@link #imgUrl}
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl the {@link #imgUrl} to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	 * @return the {@link #keyWord}
	 */
	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * @param keyWord the {@link #keyWord} to set
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	/**
	 * @return the {@link #typeSTD}
	 */
	public String getTypeSTD() {
		return typeSTD;
	}

	/**
	 * @param typeSTD the {@link #typeSTD} to set
	 */
	public void setTypeSTD(String typeSTD) {
		this.typeSTD = typeSTD;
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
	 * @return the {@link #sourceName}
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName the {@link #sourceName} to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
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
	 * @return the {@link #createTime}
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the {@link #createTime} to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

}
