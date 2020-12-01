package com.hackerda.platform.application;

import com.hackerda.platform.domain.community.IdentityCategory;
import com.hackerda.platform.domain.community.PostBO;
import com.hackerda.platform.domain.community.RecordStatus;
import com.hackerda.platform.infrastructure.community.RecommendPostRedisRecorder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityPostAppTest {

    @Autowired
    private CommunityPostApp communityPostApp;
    @Autowired
    private RecommendPostRedisRecorder recommendPostRedisRecorder;

    @Test
    public void deletePost() {
    }

    @Test
    public void testPostTopAndRevoke() {
        PostBO postBO = new PostBO("test", "测试内容", Collections.emptyList(), IdentityCategory.Community,
                "IPhone X");
        postBO.setStatus(RecordStatus.Release);

        communityPostApp.createPost(postBO);

        assertThat(communityPostApp.topPost("test", postBO, false).isSuccess()).isTrue();

        assertThat(postBO.getStatus()).isEqualTo(RecordStatus.TOP);

        assertThat(communityPostApp.topPost("test", postBO, true).isSuccess()).isTrue();

        assertThat(postBO.getStatus()).isEqualTo(RecordStatus.Release);

    }

    @Test
    public void testReTop() {
        PostBO postBO1 = new PostBO("test", "测试内容", Collections.emptyList(), IdentityCategory.Community,
                "IPhone X");
        postBO1.setStatus(RecordStatus.Release);

        communityPostApp.createPost(postBO1);

        assertThat(communityPostApp.topPost("test", postBO1, false).isSuccess()).isTrue();

        PostBO postBO2 = new PostBO("test", "测试内容", Collections.emptyList(), IdentityCategory.Community,
                "IPhone X");
        postBO2.setStatus(RecordStatus.Release);

        communityPostApp.createPost(postBO2);

        assertThat(communityPostApp.topPost("test", postBO2, false).isSuccess()).isTrue();

    }

    @Test
    public void testPostRecommendAndRevoke() {
        PostBO postBO = new PostBO("test", "测试内容", Collections.emptyList(), IdentityCategory.Community,
                "IPhone X");
        postBO.setStatus(RecordStatus.Release);

        communityPostApp.createPost(postBO);

        assertThat(communityPostApp.recommendPost("test", postBO, false).isSuccess()).isTrue();

        assertThat(recommendPostRedisRecorder.getPostIdList(new Date()).size()).isEqualTo(1);

        assertThat(communityPostApp.recommendPost("test", postBO, true).isSuccess()).isTrue();

        assertThat(recommendPostRedisRecorder.getPostIdList(new Date()).size()).isEqualTo(0);

    }
}