package com.hackerda.platform.domain.wechat;

import com.google.common.collect.Sets;
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


    @EqualsAndHashCode.Exclude
    private final Set<WechatUser> modifyWechatUserSet = Sets.newHashSet();

    public UnionId(String unionId) {
        this.unionId = unionId;
    }

    public boolean isEmpty(){
        return StringUtils.isEmpty(unionId);
    }

    public void bindOpenid(WechatUser wechatUser) {
        wechatUserMap.put(wechatUser.getAppId(), wechatUser);
        if (originWechatUserSet.contains(wechatUser)) {
            modifyWechatUserSet.add(wechatUser);
        }
    }

    public void subscribe(String appId) {
        WechatUser wechatUser = wechatUserMap.get(appId);
        if(wechatUser == null) {
            throw new IllegalArgumentException("unionId " + this.unionId + " haven`t bind appId "+ appId);
        }

        wechatUser.setSubscribe(true);

        if(originWechatUserSet.contains(wechatUser)) {
            modifyWechatUserSet.add(wechatUser);
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
    public WechatUser getWechatUser(String appId) {
        return wechatUserMap.getOrDefault(appId, WechatUser.ofNull());
    }

    public void unSubscribe(String appId) {
        WechatUser wechatUser = wechatUserMap.get(appId);
        if(wechatUser == null) {
            throw new IllegalArgumentException("unionId " + this.unionId + " haven`t bind appId "+ appId);
        }

        wechatUser.setSubscribe(false);

        if(originWechatUserSet.contains(wechatUser)) {
            modifyWechatUserSet.add(wechatUser);
        }
    }


    public boolean hasOpenId(WechatUser wechatUser) {
        return wechatUserMap.containsValue(wechatUser);
    }

    public boolean hasApp(String appId) {
        return wechatUserMap.containsKey(appId);
    }

    public Set<WechatUser> getModifyWechatUserSet() {
        return Collections.unmodifiableSet(modifyWechatUserSet);
    }


    public void save() {
        originWechatUserSet = new HashSet<>(wechatUserMap.values());
        modifyWechatUserSet.clear();
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
