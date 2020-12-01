package com.hackerda.platform.application;

import com.hackerda.platform.domain.user.*;
import com.hackerda.platform.domain.user.action.ActionTarget;
import com.hackerda.platform.domain.user.action.UserAction;
import com.hackerda.platform.domain.user.action.UserActionRecordBO;
import com.hackerda.platform.domain.user.action.UserActionRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserRoleApp {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserActionRecordRepository userActionRecordRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void grantRole(AppStudentUserBO operator, AppStudentUserBO appUserBO, RoleBO roleBO) {

        if (!appUserBO.hasRole(roleBO)) {
            appUserBO.grantRole(roleBO);
            userRepository.update(appUserBO);

            UserActionRecordBO record = new UserActionRecordBO(operator.getUserName(), UserAction.GRANT_ROLE, ActionTarget.USER,
                    appUserBO.getUserName(), roleBO.getId().toString(), new Date());

            userActionRecordRepository.store(record);
        }

    }



    @Transactional
    public void createRole(AppStudentUserBO operator, String roleName, String roleCode, List<PermissionBO> permissionBOList) {
        RoleBO roleBO = new RoleBO(roleName, roleCode, operator.getRolePriority());
        roleBO.grantPermission(permissionBOList);

        roleRepository.store(roleBO);

        UserActionRecordBO record = new UserActionRecordBO(operator.getUserName(), UserAction.CREATE_ROLE, ActionTarget.ROLE,
                roleBO.getId().toString(), roleBO.getId().toString(), new Date());

        userActionRecordRepository.store(record);
    }
}
