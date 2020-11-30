package com.hackerda.platform.infrastructure.database.dao.rbac;

import com.hackerda.platform.infrastructure.database.mapper.PermissionMapper;
import com.hackerda.platform.infrastructure.database.model.Permission;
import com.hackerda.platform.infrastructure.database.model.PermissionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author JR Chan
 */
@Repository
public class PermissionDao {

    @Autowired
    private PermissionMapper permissionMapper;

    public void insertSelective(Permission permission){
        permissionMapper.insertSelective(permission);
    }

    public Permission selectByCode(String code){
        PermissionExample example = new PermissionExample();
        example.createCriteria().andPermissionCodeEqualTo(code);
        return permissionMapper.selectByExample(example).stream().findFirst().orElse(null);
    }

}
