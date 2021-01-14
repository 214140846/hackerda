package com.hackerda.platform.infrastructure.repository.wechat;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.infrastructure.database.mapper.ext.StudentUserExtMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

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
        testUnionId.bindOpenid(new WechatUser("appId1", "openId1"));
        unionIdRepository.save(testUnionId);

        assertThat(unionIdRepository.find("test_unionId")).isEqualTo(testUnionId);

        testUnionId.bindOpenid(new WechatUser("appId2", "openId2", false));
        unionIdRepository.save(testUnionId);

        UnionId saveId = unionIdRepository.find("test_unionId");

        assertThat(saveId).isEqualTo(testUnionId);
        assertThat(saveId.getOpenId("appId2")).isEqualTo("openId2");
        assertThat(saveId.getWechatUser("appId2").isSubscribe()).isEqualTo(false);
    }

    @Test
    public void subscribe() {
        // test subscribe and unSubscribe
        UnionId testUnionId = UnionId.ofNew("test_unionId");
        testUnionId.bindOpenid(new WechatUser("appId1", "openId1"));

        testUnionId.unSubscribe("appId1");
        unionIdRepository.save(testUnionId);

        assertThat(testUnionId.getWechatUser("appId1").isSubscribe()).isFalse();
        assertThat(unionIdRepository.find("test_unionId").getWechatUser("appId1").isSubscribe()).isFalse();

        testUnionId.subscribe("appId1");
        unionIdRepository.save(testUnionId);

        assertThat(testUnionId.getWechatUser("appId1").isSubscribe()).isTrue();
        assertThat(unionIdRepository.find("test_unionId").getWechatUser("appId1").isSubscribe()).isTrue();

        // test bind
        testUnionId.bindOpenid(new WechatUser("appId1", "openId1", false));
        unionIdRepository.save(testUnionId);

        assertThat(testUnionId.getWechatUser("appId1").isSubscribe()).isFalse();
        assertThat(unionIdRepository.find("test_unionId").getWechatUser("appId1").isSubscribe()).isFalse();
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

        Map<StudentAccount, UnionId> map = unionIdRepository.find(Lists.newArrayList(new StudentAccount(2014025838)));

        assertThat(map.get(new StudentAccount(2014025838))).isEqualTo(unionId);

        assertThat(map.get(new StudentAccount(2014025839))).isNull();

        Map<StudentAccount, UnionId> map2 = unionIdRepository.find(Lists.newArrayList(new StudentAccount(2014025839)));

        assertThat(map2.get(new StudentAccount(2014025839))).isNull();
    }

}