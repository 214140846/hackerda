package com.hackerda.platform.domain.student;

import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@ToString(callSuper = true)
public class WechatStudentUserBO extends StudentUserBO{

    private Map<String, WechatUser> wechatUserMap = new HashMap<>(0);

    @Getter
    private UnionId unionId;

    @Getter
    private UnionId newBindUnionId = UnionId.ofNull();

    @Getter
    private UnionId revokeUnionId = UnionId.ofNull();

    @EqualsAndHashCode.Exclude
    private Set<WechatUser> originWechatUserSet = Collections.emptySet();

    /**
     * 区分该对象是否使用unionId
     */
    @Getter
    @Setter
    private boolean useUnionId = true;

    public boolean hasBindApp() {
        return getAppOpenid() != null;
    }


    public void setBindWechatUser(List<WechatUser> wechatUserList) {
        originWechatUserSet = new HashSet<>(wechatUserList);

        this.wechatUserMap = wechatUserList.stream().collect(Collectors.toMap(WechatUser::getAppId,
                wechatUser -> wechatUser));
    }

    public void setUnionId(UnionId unionId) {
        checkUseUnionId();
        this.unionId = unionId;
    }

    public void bindUnionId(UnionId unionId) {
        checkUseUnionId();

        if (!unionId.equals(this.unionId)) {
            this.unionId = unionId;
            this.newBindUnionId = unionId;
        }
    }

    public void revokeUnionId() {
        checkUseUnionId();
        if (!hasBindUnionId()) {
            throw new UnsupportedOperationException(this.toString() + ": this user haven`t bind unionId");
        }

        if (newBindUnionId.isEmpty()) {
            this.revokeUnionId = this.unionId;
        } else {
            this.newBindUnionId = UnionId.ofNull();
        }

        this.unionId = UnionId.ofNull();

    }

    private void checkUseUnionId() {
        if (!useUnionId) {
            throw new UnsupportedOperationException(this.toString() + ": this user cant`t use unionId");
        }

    }

    public boolean hasBindUnionId() {
        return !(this.unionId == null || unionId.isEmpty());
    }


    public String getAppOpenid(){
        return "";
    }


    public String getOpenid(String appId) {
        if(useUnionId) {
            return unionId.getOpenId(appId);
        }

        return wechatUserMap.getOrDefault(appId, WechatUser.ofNull()).getOpenId();
    }




    public boolean hasBindApp(String appId) {
        if(useUnionId) {
            return unionId.hasApp(appId);
        }

        return wechatUserMap.containsKey(appId);
    }

    public boolean hasBindWechatUser(WechatUser wechatUser) {
        if (useUnionId) {
            return !unionId.isEmpty() && unionId.hasOpenId(wechatUser);
        }

        return originWechatUserSet.contains(wechatUser) || wechatUserMap.containsValue(wechatUser);
    }

    public boolean hasBindWechatUser() {
        return !originWechatUserSet.isEmpty() || !wechatUserMap.isEmpty();
    }

    public void bindWechatUser(WechatUser wechatUser) {
        if(wechatUserMap.containsKey(wechatUser.getAppId())) {
            throw new BusinessException(ErrorCode.ACCOUNT_HAS_BIND, "微信"+ wechatUser.toString()+ "无法绑定此学号"+super.getAccount());
        }
        wechatUserMap.put(wechatUser.getAppId(), wechatUser);
    }

    public WechatUser revokeWechatUser(String appId) {
        WechatUser wechatUser = wechatUserMap.remove(appId);
        if(wechatUser == null) {
            log.error("{} have`t bin appId {}", this, appId);
        }
        return wechatUser;
    }

    public List<WechatUser> getNewBindWechatUser() {
        return wechatUserMap.values().stream()
                .filter(x-> !originWechatUserSet.contains(x))
                .collect(Collectors.toList());
    }

    public List<WechatUser> getRevokeWechatUser() {
        return originWechatUserSet.stream()
                .filter(x-> !wechatUserMap.containsValue(x))
                .collect(Collectors.toList());
    }


    public void save() {

        if (useUnionId) {
            if (!newBindUnionId.isEmpty() && !revokeUnionId.isEmpty()) {
                throw new IllegalStateException("unionId state error: 不能同时有新绑定的id和移除绑定绑定的id。" );
            }

            if (!newBindUnionId.isEmpty()) {
                this.unionId = newBindUnionId;
                this.newBindUnionId = UnionId.ofNull();
            }

            if (!revokeUnionId.isEmpty()) {
                this.revokeUnionId = UnionId.ofNull();
            }

        }

    }

}
