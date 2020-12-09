package com.hackerda.platform.infrastructure.repository.wechat;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.infrastructure.database.mapper.ext.StudentUserExtMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UnionIdRepositoryImplTest {

    @Autowired
    private UnionIdRepositoryImpl unionIdRepository;
    @Autowired
    private StudentUserExtMapper studentUserExtMapper;

    @Test
    public void save() {

        UnionId testUnionId = UnionId.ofNew("test_unionId");
        testUnionId.bindOpenid(new WechatUser("appId", "openId1"));
        unionIdRepository.save(testUnionId);

        assertThat(unionIdRepository.find("test_unionId")).isEqualTo(testUnionId);


        testUnionId.bindOpenid(new WechatUser("appId", "openId2"));
        unionIdRepository.save(testUnionId);

        assertThat(unionIdRepository.find("test_unionId")).isEqualTo(testUnionId);

    }

    @Test
    public void find() {
        UnionId unionId = unionIdRepository.find("test_unionId");
        assertThat(unionId.isEmpty()).isTrue();
    }

    @Test
    public void findByAccount() {
        UnionId testUnionId = UnionId.ofNew("test_unionId");
        testUnionId.bindOpenid(new WechatUser("appId", "openId1"));
        unionIdRepository.save(testUnionId);

        studentUserExtMapper.insertUnionIdRelative(2014025838, "test_unionId");

        UnionId unionId = unionIdRepository.find(new StudentAccount(2014025838));

        assertThat(unionId.isEmpty()).isFalse();
    }

}