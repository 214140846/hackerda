package com.hackerda.platform.controller.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hackerda.platform.domain.user.AppUserBO;
import com.hackerda.platform.domain.user.PermissionBO;
import com.hackerda.platform.domain.user.RoleBO;
import com.hackerda.platform.domain.user.UserRepository;
import com.hackerda.platform.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.stream.Collectors;

@Slf4j
public class UserJWTRealm extends AuthorizingRealm {

    private final UserRepository userRepository;

    public UserJWTRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        AppUserBO userBO = (AppUserBO) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        if (!userBO.isGuest()) {
            simpleAuthorizationInfo.setRoles(userBO.getRoleList().stream().map(RoleBO::getCode).collect(Collectors.toSet()));

            simpleAuthorizationInfo.addStringPermissions(userBO.getRoleList().stream().flatMap(x -> x.getPermissionList().stream())
                    .map(PermissionBO::getCode).collect(Collectors.toSet()));
        }

        return simpleAuthorizationInfo;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        if(StringUtils.isEmpty(token)){
            return null;
        }

        if(token.equals("guest")) {
            return new SimpleAuthenticationInfo(AppUserBO.createGuest(), "guest", "userJWTRealm");
        }

        String username = JwtUtils.getClaim(token, JwtUtils.USERNAME_KEY);

        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        try {
            AppUserBO userBO = userRepository.findByUserName(username);
            if(userBO == null) {
                return null;
            }
            JwtUtils.verify(token, username, userBO.getSalt());
            return new SimpleAuthenticationInfo(userBO, token, "userJWTRealm");
        }catch (JWTVerificationException e){
            return null;
        }catch (Exception e){
            log.error("verify token error", e);
            throw new AuthenticationException(e);
        }
    }
}
