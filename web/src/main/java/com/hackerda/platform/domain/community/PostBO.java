package com.hackerda.platform.domain.community;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 帖子
 */

@Getter
@ToString
@EqualsAndHashCode
public class PostBO {

    @Setter
    private Long id;
    /**
     * 用户id
     */
    private final String userName;

    /** 话题内容 **/
    private final String content;

    /** 发表时间 **/
    private final Date postTime;

    /** 图片文件信息集合 **/
    private final List<ImageInfo> imageInfoList;

    /** 是否允许评论 **/
    private final boolean allowComment = true;

    /** 发布身份 **/
    private final IdentityCategory identityCategory;

    /** 状态 **/
    @Setter
    private RecordStatus status;

    private final String equipment;

    private Date lastReplyTime;

    private final boolean show;


    public PostBO(String userName, String content, List<ImageInfo> imageInfoList, IdentityCategory identityCategory, String equipment) {
        this(userName, content, imageInfoList, identityCategory, new Date(), equipment, true);
    }

    public PostBO(String userName, String content, List<ImageInfo> imageInfoList, IdentityCategory identityCategory,
                  Date postTime, String equipment, boolean show) {
        this.userName = userName;
        this.content = content;
        this.imageInfoList = imageInfoList;
        this.identityCategory = identityCategory;
        this.postTime = postTime;
        this.equipment = equipment;
        this.show = show;
    }

    public boolean isRelease() {
        return status == RecordStatus.Release;
    }

    public boolean isDelete() {
        return status == RecordStatus.Delete;
    }

    public void delete(){
        this.status = RecordStatus.Delete;
    }

    public boolean hasImage(){
        return !CollectionUtils.isEmpty(imageInfoList);
    }

    public String getUnReleaseReason() {
        if(status != RecordStatus.Release) {
            return "包含违规内容，校验不通过";
        }else {
            return "";
        }
    }

    public boolean hasContent() {
        return !StringUtils.isEmpty(content);
    }

    public boolean canDeleteByUser(String userName) {
        return this.getUserName().equals(userName);
    }

    public boolean isAnonymous() {
        return identityCategory.isAnonymous();
    }

    public boolean isTop() {
        return status == RecordStatus.TOP;
    }

    public void statusToRelease() {
        this.status = RecordStatus.Release;
    }


    /**
     * 置顶
     */
    public void top() {
        if (this.status == RecordStatus.Release) {
            this.status = RecordStatus.TOP;
        } else {
            throw new IllegalStateException("id:" + this.id + "帖子状态为"+this.status.name()+" 无法被置顶");
        }
    }

    /**
     * 置顶
     */
    public void feature() {
        if (this.status == RecordStatus.Release) {
            this.status = RecordStatus.Featured;
        } else {
            throw new IllegalStateException("id:" + this.id + "帖子状态为"+this.status.name()+" 无法被精选");
        }
    }
}
