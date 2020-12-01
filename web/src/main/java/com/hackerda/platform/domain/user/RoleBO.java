package com.hackerda.platform.domain.user;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class RoleBO {

    public static String USER = "user";

    private final String name;

    private final String code;

    @Setter
    private Integer id;

    private final int priority;

    private final List<PermissionBO> permissionList;

    @EqualsAndHashCode.Exclude
    private final List<PermissionBO> newGrantPermissionList = Lists.newArrayListWithCapacity(0);


    public RoleBO(String name, String code, int priority) {
        this(null, name, code, priority, new ArrayList<>(0));
    }

    public RoleBO(Integer id, String name, String code, int priority, List<PermissionBO> permissionList) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.priority = priority;
        this.permissionList = permissionList;
    }

    public void grantPermission(PermissionBO permissionBO) {
        if (permissionBO.getPriority() < priority) {
            throw new IllegalArgumentException(permissionBO.getName()+" priority is greater than "+ this.name);
        }

        if (permissionBO.isNew()) {
            throw new IllegalArgumentException(permissionBO.getName()+" haven`t store");
        }

        permissionList.add(permissionBO);
        newGrantPermissionList.add(permissionBO);
    }

    public void grantPermission(List<PermissionBO> grantPermissionList) {
        for (PermissionBO permissionBO : grantPermissionList) {
            grantPermission(permissionBO);
        }
    }

    public List<PermissionBO> getPermissionList() {
        return Collections.unmodifiableList(permissionList);
    }

    public List<PermissionBO> getNewGrantPermissionList() {
        return Collections.unmodifiableList(newGrantPermissionList);
    }

    /**
     * 该角色是否是新角色，未保存在数据库的角色都是新角色
     * @return 是新角色则返回ture
     */
    public boolean isNewRole() {
        return this.id == null;
    }

}
