package com.hackerda.platform.domain.user;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class RoleScript {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void createRole() {


//        roleRepository.store(new PermissionBO("推荐", "recommend", 1));
//        roleRepository.store(new PermissionBO("置顶", "top", 1));
        roleRepository.store(new PermissionBO("删除", PermissionBO.DELETE, 1));
//        RoleBO roleBO = new RoleBO("管理员", "admin", 1);
        RoleBO roleBO = roleRepository.findByCode("admin");
//        roleBO.grantPermission(roleRepository.findPermissionByCode(PermissionBO.RECOMMEND));
//        roleBO.grantPermission(roleRepository.findPermissionByCode(PermissionBO.TOP));
        roleBO.grantPermission(roleRepository.findPermissionByCode(PermissionBO.DELETE));
        roleRepository.store(roleBO);

        RoleBO superAdmin = roleRepository.findByCode("admin");

        System.out.println(superAdmin);

//        assertThat(superAdmin).isEqualTo(roleBO);
//
//
//        RoleBO roleBO2 = new RoleBO("黑名单用户", "black_list", 2);
//        roleBO2.grantPermission(roleRepository.findPermissionByCode(PermissionBO.VIEW));
//        roleRepository.store(roleBO2);
//
//        assertThat(roleRepository.findByCode("black_list")).isEqualTo(roleBO2);
    }
}
