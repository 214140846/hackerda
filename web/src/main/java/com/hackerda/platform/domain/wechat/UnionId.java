package com.hackerda.platform.domain.wechat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
public class UnionId {

    @Getter
    private final String unionId;

    private final Map<String, WechatUser> wechatUserMap = new HashMap<>(4);

    @EqualsAndHashCode.Exclude
    private Set<WechatUser> originWechatUserSet = Collections.emptySet();

    public UnionId(String unionId) {
        this.unionId = unionId;
    }

    public boolean isEmpty(){
        return StringUtils.isEmpty(unionId);
    }


    public void bindOpenid(WechatUser wechatUser) {
        if (!originWechatUserSet.contains(wechatUser)) {
            wechatUserMap.put(wechatUser.getAppId(), wechatUser);
        }
    }

    public List<WechatUser> getNewBindWechatUser() {
        return wechatUserMap.values().stream()
                .filter(x-> !originWechatUserSet.contains(x))
                .collect(Collectors.toList());
    }

    public String getOpenId(String appId) {
        return this.wechatUserMap.getOrDefault(appId, WechatUser.ofNull()).getOpenId();
    }

    public boolean hasOpenId(WechatUser wechatUser) {
        return wechatUserMap.containsValue(wechatUser);
    }

    public boolean hasApp(String appId) {
        return wechatUserMap.containsKey(appId);
    }

    public void save() {
        originWechatUserSet = new HashSet<>(wechatUserMap.values());
    }

    /**
     * factory method
     */
    public static UnionId ofNull() {
        return new UnionId("");
    }

    public static UnionId ofNew(String unionId) {
        return new UnionId(unionId);
    }

    public static UnionId ofRepo(String unionId, List<WechatUser> wechatUserList) {
        UnionId id = new UnionId(unionId);

        for (WechatUser wechatUser : wechatUserList) {
            id.wechatUserMap.put(wechatUser.getAppId(), wechatUser);
        }

        id.originWechatUserSet = new HashSet<>(wechatUserList);
        return id;
    }



}
