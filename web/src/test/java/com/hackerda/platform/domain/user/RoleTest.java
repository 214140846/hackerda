package com.hackerda.platform.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class RoleTest {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void createRole() {

        PermissionBO permissionBO = new PermissionBO("推荐", "recommend", 0);

        roleRepository.store(permissionBO);

        RoleBO roleBO = new RoleBO("超级管理员", "super_admin", 0);
        roleBO.grantPermission(permissionBO);

        roleRepository.store(roleBO);

        RoleBO superAdmin = roleRepository.findByCode("super_admin");

        assertThat(superAdmin).isEqualTo(roleBO);


        RoleBO roleBO2 = new RoleBO("黑名单用户", "black_list", 2);
        roleBO2.grantPermission(roleRepository.findPermissionByCode(PermissionBO.VIEW));
        roleRepository.store(roleBO2);

        assertThat(roleRepository.findByCode("black_list")).isEqualTo(roleBO2);


    }

    @Test
    public void createFail() {
        PermissionBO permissionBO = new PermissionBO("推荐", "recommend", 0);

        RoleBO roleBO = new RoleBO("超级管理员", "super_admin", 0);

        assertThatThrownBy(() -> roleBO.grantPermission(permissionBO))
                .isInstanceOf(IllegalArgumentException.class);

        RoleBO roleBO2 = new RoleBO("黑名单用户", "black_list", 2);
        roleRepository.store(permissionBO);

        assertThatThrownBy(() -> roleBO2.grantPermission(permissionBO))
                .isInstanceOf(IllegalArgumentException.class);

    }
}