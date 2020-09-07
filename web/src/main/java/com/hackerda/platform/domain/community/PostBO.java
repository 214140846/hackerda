package com.hackerda.platform.domain.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 帖子
 */

@Getter
@ToString
public class PostBO {

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


    public PostBO(String userName, String content, List<ImageInfo> imageInfoList, IdentityCategory identityCategory, String equipment) {
        this(userName, content, imageInfoList, identityCategory, new Date(), equipment);
    }

    public PostBO(String userName, String content, List<ImageInfo> imageInfoList, IdentityCategory identityCategory,
                  Date postTime, String equipment) {
        this.userName = userName;
        this.content = content;
        this.imageInfoList = imageInfoList;
        this.identityCategory = identityCategory;
        this.postTime = postTime;
        this.equipment = equipment;
    }

    public boolean isRelease() {
        return status == RecordStatus.Release;
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
}
