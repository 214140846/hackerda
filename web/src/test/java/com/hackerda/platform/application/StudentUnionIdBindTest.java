package com.hackerda.platform.application;


import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatActionRecordRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentUnionIdBindTest {

    @Autowired
    private StudentBindApp studentBindApp;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WechatActionRecordRepository wechatActionRecordRepository;
    @Autowired
    private UnionIdRepository unionIdRepository;

    private UnionId unionId;
    private WechatUser wechatUser = new WechatUser("test_appId", "test_openid");

    private UnionId unionId2 = UnionId.ofNew("test_unionId2");
    private WechatUser wechatUser2 = new WechatUser("test_appId", "test_openid2");

    @Before
    public void init() {
        wechatUser = new WechatUser("test_appId", "test_openid");
        unionId = UnionId.ofNew("test_unionId");
        unionId.bindOpenid(wechatUser);

        unionIdRepository.save(unionId);

        unionId2.bindOpenid(wechatUser2);
        unionIdRepository.save(unionId2);
    }


    @Test
    public void bindByUnionId() {

        StudentAccount studentAccount = new StudentAccount("2017025820");
        WechatStudentUserBO wechatStudentUserBO = studentBindApp.bindByUnionId(studentAccount, "1", unionId,
                wechatUser);


        assertThat(studentRepository.findWetChatUser(studentAccount)).isEqualTo(wechatStudentUserBO);

        // test unbind
        studentBindApp.unbindUnionId(wechatStudentUserBO);
        assertThat(wechatStudentUserBO.hasBindUnionId()).isFalse();
        assertThat(studentRepository.findWetChatUser(studentAccount)).isEqualTo(wechatStudentUserBO);

        assertThat(wechatStudentUserBO.isMsgHasCheck()).isTrue();

        // rebind
        studentBindApp.bindByUnionId(studentAccount, "1", unionId2, wechatUser);

        assertThat(studentRepository.findWetChatUser(studentAccount).hasBindUnionId()).isTrue();
        assertThat(wechatActionRecordRepository.find(wechatUser).size()).isEqualTo(2);

    }

    @Test
    public void testDuplicateBindWithDiffUnionId() {

        StudentAccount studentAccount = new StudentAccount("2017025820");
        studentBindApp.bindByUnionId(studentAccount, "1", unionId, wechatUser);

        UnionId unionId = UnionId.ofNew("test_unionId2");

        assertThatThrownBy(() -> studentBindApp.bindByUnionId(studentAccount, "1", unionId, wechatUser))
                .isInstanceOf(BusinessException.class)
                .hasMessageEndingWith("该学号已经被绑定");


        assertThat(wechatActionRecordRepository.find(wechatUser).size()).isEqualTo(2);

    }


    @Test
    public void testDuplicateBindWithSameUnionId() {

        StudentAccount studentAccount = new StudentAccount("2017025820");
        studentBindApp.bindByUnionId(studentAccount, "1", unionId, wechatUser);
        WechatStudentUserBO userBO = studentBindApp.bindByUnionId(studentAccount, "1", unionId, wechatUser);

        WechatStudentUserBO db = studentRepository.findWetChatUser(studentAccount);

        assertThat(db).isEqualTo(userBO);
        assertThat(wechatActionRecordRepository.find(wechatUser).size()).isEqualTo(2);

    }

    @Test
    public void testSameUnionIdBindDiffAccount() {

        studentBindApp.bindByUnionId(new StudentAccount("2017025820"), "1", unionId, wechatUser);
        WechatStudentUserBO userBO = studentBindApp.bindByUnionId(new StudentAccount("2014025838"), "1", unionId,
                wechatUser);

        WechatStudentUserBO db = studentRepository.findWetChatUser(new StudentAccount("2014025838"));

        assertThat(db).isEqualTo(userBO);
        assertThat(wechatActionRecordRepository.find(wechatUser).size()).isEqualTo(2);

    }

    @Test
    public void testDuplicateBindInWhiteList() {

        StudentAccount studentAccount = new StudentAccount("2014025838");
        studentBindApp.bindByUnionId(studentAccount, "1", unionId, wechatUser);


        studentBindApp.bindByUnionId(studentAccount, "1", unionId2, wechatUser);


        assertThat(studentRepository.findWetChatUser(unionId2).getUnionId()).isEqualTo(unionId2);

        assertThat(wechatActionRecordRepository.find(wechatUser).size()).isEqualTo(2);

    }

}
