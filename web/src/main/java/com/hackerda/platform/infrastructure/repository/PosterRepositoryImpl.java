package com.hackerda.platform.infrastructure.repository;

import com.github.pagehelper.PageHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hackerda.platform.domain.community.*;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.user.Gender;
import com.hackerda.platform.infrastructure.database.dao.ImageDao;
import com.hackerda.platform.infrastructure.database.dao.user.UserDao;
import com.hackerda.platform.infrastructure.database.mapper.ext.PostExtMapper;
import com.hackerda.platform.infrastructure.database.model.ImageInfoDO;
import com.hackerda.platform.infrastructure.database.model.Post;
import com.hackerda.platform.infrastructure.database.model.PostExample;
import com.hackerda.platform.infrastructure.database.model.StudentPosterDO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpCookie;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class PosterRepositoryImpl implements PosterRepository {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private PostExtMapper postExtMapper;

    private final LoadingCache<Long, List<ImageInfo>> imageCache = CacheBuilder.newBuilder()

            .maximumSize(1000)
            .build(new CacheLoader<Long, List<ImageInfo>>() {
                @Override
                public List<ImageInfo> load(@NotNull Long key) {
                    return imageDao.selectByPostId(key).stream()
                            .map(imageInfoDO -> new ImageInfo(imageInfoDO.getUrl(), imageInfoDO.getFileId(),
                                    RecordStatus.getByCode(imageInfoDO.getRecordStatus()))).collect(Collectors.toList());
                }
            });

    @Override
    public StudentPoster findByStudentAccount(StudentAccount studentAccount) {

        StudentPosterDO studentPosterDO = userDao.selectByStudentPoster(studentAccount.getAccount());

        return getStudentPoster(studentPosterDO, studentAccount);
    }

    @Override
    public StudentPoster findStudentPosterByUserName(String userName) {


        return findStudentPosterByUserName(Collections.singletonList(userName)).stream().findFirst().orElse(null);
    }

    @Override
    public List<StudentPoster> findStudentPosterByUserName(Collection<String> userName) {

        return userDao.selectStudentPosterByUserName(userName).stream()
                .map(x-> getStudentPoster(x, new StudentAccount(x.getAccount())))
                .collect(Collectors.toList());
    }

    private StudentPoster getStudentPoster(StudentPosterDO studentPosterDO, StudentAccount studentAccount) {
        StudentPoster.StudentPosterBuilder builder = StudentPoster.builder(studentPosterDO.getUserName(),
                studentPosterDO.getNickName(), studentPosterDO.getAvatarUrl(), Gender.formCode(studentPosterDO.getGender()));

        builder.academyName(studentPosterDO.getAcademyName());
        builder.className(studentPosterDO.getClassName());
        builder.name(studentPosterDO.getName());
        builder.sex(studentPosterDO.getSex());
        builder.subjectName(studentPosterDO.getSubjectName());
        builder.studentAccount(studentAccount);
        builder.urpClassNum(studentPosterDO.getUrpClassNum());

        return builder.build();
    }

    @Override
    @Transactional
    public void save(PostBO postBO) {

        Post post = adapter(postBO);
        postExtMapper.insertSelective(post);
        postBO.setId(post.getId());

        List<ImageInfoDO> imageInfoDOList = postBO.getImageInfoList().stream().map(x -> {
            ImageInfoDO imageInfoDO = new ImageInfoDO();
            imageInfoDO.setFileId(x.getFileId());
            imageInfoDO.setUrl(x.getPath());
            imageInfoDO.setRecordStatus(x.getRecordStatus().getCode());
            return imageInfoDO;
        }).collect(Collectors.toList());

        imageDao.insertBatch(imageInfoDOList);

        imageDao.insertPostImageRelative(post.getId(), imageInfoDOList.stream()
                .map(ImageInfoDO::getId).collect(Collectors.toList()));


    }

    @Override
    public void update(PostBO postBO) {
        Post post = adapter(postBO);

        post.setId(postBO.getId());

        postExtMapper.updateByPrimaryKeySelective(post);
    }

    private Post adapter(PostBO postBO) {
        Post post = new Post();

        post.setAllowComment(postBO.isAllowComment() ? (byte) 1 : (byte) 0);
        post.setContent(postBO.getContent());
        post.setIdentityCode(postBO.getIdentityCategory().getCode());
        post.setRecordStatus(postBO.getStatus().getCode());
        post.setUserName(postBO.getUserName());
        post.setPostTime(postBO.getPostTime());
        post.setEquipment(postBO.getEquipment());
        return post;
    }

    @Override
    public PostDetailBO findByPostById(long id) {
        Post post = postExtMapper.selectByPrimaryKey(id);

        StudentPoster poster = findStudentPosterByUserName(post.getUserName());
        return getPostDetailBO(post, Collections.singletonList(poster));
    }

    @Override
    public List<PostDetailBO> findByIdList(List<Long> idList) {

        if(idList.isEmpty()) {
            return Collections.emptyList();
        }
        PostExample example = new PostExample();

        example.createCriteria().andIdIn(idList);
        return getPostDetailList(example);
    }


    @Override
    public List<PostDetailBO> findShowPost(Integer startId, int limit) {

        PostExample example = new PostExample();
        example.setOrderByClause("id desc");
        PostExample.Criteria criteria = example.createCriteria();
        if(startId != null) {
            criteria.andIdLessThan(startId.longValue());
        }
        criteria.andRecordStatusEqualTo(RecordStatus.Release.getCode())
                .andShowEqualTo(true);
        PageHelper.startPage(0, limit);
        return getPostDetailList(example);

    }

    @Override
    public List<PostDetailBO> findShowPostByLastReply(Date lastReplyTime, int count, List<RecordStatus> identityCategoryList) {
        PostExample example = new PostExample();
        example.setOrderByClause("last_reply_time desc");
        PostExample.Criteria criteria = example.createCriteria();
        if(lastReplyTime != null) {
            criteria.andLastReplyTimeLessThan(lastReplyTime);
        }
        criteria.andRecordStatusIn(identityCategoryList.stream().map(RecordStatus::getCode).collect(Collectors.toList()))
                .andShowEqualTo(true);
        PageHelper.startPage(0, count);
        return getPostDetailList(example);
    }

    @Override
    public List<PostDetailBO> findPostByUser(String userName, Integer startId, int count, List<RecordStatus> identityCategoryList) {
        PostExample example = new PostExample();
        example.setOrderByClause("id desc");
        PostExample.Criteria criteria = example.createCriteria();

        if(startId != null) {
            criteria.andIdLessThan(startId.longValue());
        }
        criteria.andRecordStatusIn(identityCategoryList.stream().map(RecordStatus::getCode).collect(Collectors.toList()))
                .andShowEqualTo(true);
        criteria.andUserNameEqualTo(userName);
        criteria.andIdentityCodeEqualTo(IdentityCategory.Community.getCode());
        PageHelper.startPage(0, count);
        return getPostDetailList(example);
    }

    @Override
    public List<PostDetailBO> findByStatus(List<RecordStatus> identityCategoryList) {
        PostExample example = new PostExample();
        example.createCriteria()
                .andRecordStatusIn(identityCategoryList.stream().map(RecordStatus::getCode)
                        .collect(Collectors.toList()));

        return getPostDetailList(example);
    }

    @NotNull
    private List<PostDetailBO> getPostDetailList(PostExample example) {
        List<Post> postList = postExtMapper.selectByExample(example);
        List<String> userNameList = postList.stream().map(Post::getUserName).collect(Collectors.toList());
        List<StudentPoster> studentPosterList = findStudentPosterByUserName(userNameList);

        return postList.stream().map(x-> this.getPostDetailBO(x, studentPosterList)).collect(Collectors.toList());
    }

    @Override
    public long countShowPost(String userName, List<IdentityCategory> identityCategoryList) {
        PostExample example = new PostExample();
        example.createCriteria().andUserNameEqualTo(userName)
                .andIdentityCodeIn(identityCategoryList.stream().map(IdentityCategory::getCode).collect(Collectors.toList()))
                .andRecordStatusEqualTo(RecordStatus.Release.getCode());
        return postExtMapper.countByExample(example);
    }

    @Override
    public long findMaxReleasePostId() {
        PostExample example = new PostExample();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andRecordStatusEqualTo(RecordStatus.Release.getCode());

        PageHelper.startPage(0, 1);

        return postExtMapper.selectByExample(example).stream().findFirst().get().getId();
    }

    @Override
    public void updateLastReplyTime(long id, Date lastReplyTime) {
        Post post = new Post();
        post.setId(id);
        post.setLastReplyTime(lastReplyTime);
        postExtMapper.updateByPrimaryKeySelective(post);
    }


    private PostDetailBO getPostDetailBO(Post post, List<StudentPoster> studentPosterList) {
        List<ImageInfo> imageInfoList;
        try {
            imageInfoList = imageCache.get(post.getId());
        } catch (ExecutionException e) {
            imageInfoList = Collections.emptyList();
        }

        PostDetailBO postDetailBO = new PostDetailBO(post.getId(), post.getUserName(), post.getContent(), imageInfoList,
                IdentityCategory.getByCode(post.getIdentityCode()), post.getPostTime(), post.getEquipment(), post.getShow());
        Map<String, StudentPoster> studentPosterMap = studentPosterList.stream().collect(Collectors.toMap(Poster::getUserName,
                x -> x));

        postDetailBO.setCommentCount(post.getCommentCount());
        postDetailBO.setViewCount(post.getViewCount());
        postDetailBO.setLikeCount(post.getLikeCount());
        postDetailBO.setPostUser(studentPosterMap.get(post.getUserName()));
        postDetailBO.setStatus(RecordStatus.getByCode(post.getRecordStatus()));
        postDetailBO.setLastReplyTime(post.getLastReplyTime());
        return postDetailBO;
    }
}
