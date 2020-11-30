package com.hackerda.platform.domain.user;

import java.util.List;

public interface RoleRepository {

    void store(RoleBO roleBO);

    void store(PermissionBO permissionBO);

    RoleBO findByCode(String code);

    PermissionBO findPermissionByCode(String code);

    List<RoleBO> findByUserName(String userName);
}
