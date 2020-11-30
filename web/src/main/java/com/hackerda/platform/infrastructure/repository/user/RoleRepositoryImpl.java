package com.hackerda.platform.infrastructure.repository.user;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.user.PermissionBO;
import com.hackerda.platform.domain.user.RoleBO;
import com.hackerda.platform.domain.user.RoleRepository;
import com.hackerda.platform.infrastructure.database.dao.rbac.PermissionDao;
import com.hackerda.platform.infrastructure.database.dao.rbac.RoleDao;
import com.hackerda.platform.infrastructure.database.dao.rbac.RolePermissionDao;
import com.hackerda.platform.infrastructure.database.model.Permission;
import com.hackerda.platform.infrastructure.database.model.Role;
import com.hackerda.platform.infrastructure.database.model.RoleDetailDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Transactional
    @Override
    public void store(RoleBO roleBO) {
        Role role = new Role().setName(roleBO.getName())
                .setCode(roleBO.getCode())
                .setId(roleBO.getId())
                .setPriority(roleBO.getPriority());
        if (roleBO.isNewRole()) {
            roleDao.insertSelective(role);
            roleBO.setId(role.getId());
        }

        List<Permission> permissionList = roleBO.getNewGrantPermissionList().stream()
                .map(x -> new Permission()
                        .setId(x.getId())
                        .setPermissionName(x.getName())
                        .setPermissionCode(x.getCode())
                        .setPriority(x.getPriority()))
                .collect(Collectors.toList());

        rolePermissionDao.insertBatch(Lists.newArrayList(role.getId()), permissionList.stream().map(Permission::getId).collect(Collectors.toList()));

    }

    @Override
    public void store(PermissionBO permissionBO) {
        Permission permission = new Permission()
                .setId(permissionBO.getId())
                .setPermissionName(permissionBO.getName())
                .setPermissionCode(permissionBO.getCode())
                .setPriority(permissionBO.getPriority());

        permissionDao.insertSelective(permission);
        permissionBO.setId(permission.getId());
    }

    @Override
    public RoleBO findByCode(String code) {
        List<RoleDetailDO> roleDetailList = roleDao.selectRoleDetailByCode(code);

        return transfer(roleDetailList).stream().findFirst().orElse(null);
    }

    @Override
    public PermissionBO findPermissionByCode(String code) {
        Permission x = permissionDao.selectByCode(code);

        return new PermissionBO(x.getId(), x.getPermissionName(),
                x.getPermissionCode(), x.getPriority());

    }

    @Override
    public List<RoleBO> findByUserName(String userName) {

        List<RoleDetailDO> roleDetailList = roleDao.selectRoleDetailByUserName(userName);

        return transfer(roleDetailList);
    }


    private List<RoleBO> transfer(List<RoleDetailDO> roleDetailList) {

        if(CollectionUtils.isEmpty(roleDetailList)) {
            return Collections.emptyList();
        }

        Map<RoleBO, List<PermissionBO>> collect = roleDetailList.stream()
                .collect(Collectors.groupingBy(x -> new RoleBO(x.getRoleId(), x.getRoleName(), x.getRoleCode(),
                                x.getRolePriority(), Collections.emptyList()),
                        Collectors.mapping(x -> new PermissionBO(x.getPermissionId(), x.getPermissionName(),
                                        x.getPermissionCode(),
                                        x.getPermissionPriority()),
                                Collectors.toList())));

        return collect.keySet().stream()
                .map(x -> new RoleBO(x.getId(), x.getName(), x.getCode(), x.getPriority(), collect.get(x)))
                .collect(Collectors.toList());
    }
}
