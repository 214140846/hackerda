package com.hackerda.platform.infrastructure.repository.wechat;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.infrastructure.database.mapper.WechatUnionIdMapper;
import com.hackerda.platform.infrastructure.database.model.AccountWechatUnionId;
import com.hackerda.platform.infrastructure.database.model.WechatUnionId;
import com.hackerda.platform.infrastructure.database.model.WechatUnionIdExample;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UnionIdRepositoryImpl implements UnionIdRepository {

    @Autowired
    private WechatUnionIdMapper wechatUnionIdMapper;

    @Override
    @Transactional
    public void save(UnionId unionId) {

        List<WechatUnionId> unionIdList = unionId.getNewBindWechatUser().stream().map(x -> {

            WechatUnionId wechatUnionId = new WechatUnionId();
            wechatUnionId.setUnionId(unionId.getUnionId());
            wechatUnionId.setAppId(x.getAppId());
            wechatUnionId.setOpenId(x.getOpenId());
            return wechatUnionId;
        }).collect(Collectors.toList());
        for (WechatUnionId wechatUnionId : unionIdList) {
            wechatUnionIdMapper.insertSelective(wechatUnionId);
        }

        unionId.save();

    }

    @Override
    public UnionId find(String unionId) {
        WechatUnionIdExample example = new WechatUnionIdExample();
        example.createCriteria().andUnionIdEqualTo(unionId);

        List<WechatUser> wechatUserList = wechatUnionIdMapper.selectByExample(example).stream()
                .map(x -> new WechatUser(x.getAppId(), x.getOpenId()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(wechatUserList)) {
            return UnionId.ofNull();
        }
        return UnionId.ofRepo(unionId, wechatUserList);
    }

    @Override
    public UnionId find(StudentAccount studentAccount) {
        List<WechatUnionId> unionIdList = wechatUnionIdMapper.selectByAccount(studentAccount.getInt());
        return getUnionIdBO(unionIdList);
    }

    @Override
    public Map<StudentAccount, UnionId> find(List<StudentAccount> studentAccountList) {
        if(CollectionUtils.isEmpty(studentAccountList)) {
            return Collections.emptyMap();
        }

        Map<StudentAccount, List<AccountWechatUnionId>> accountListMap = wechatUnionIdMapper.selectByAccountList(studentAccountList.stream().map(StudentAccount::getInt).collect(Collectors.toList()))
                .stream().collect(Collectors.groupingBy(x -> new StudentAccount(x.getAccount())));

        return accountListMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            List<WechatUser> wechatUserList = entry.getValue().stream()
                    .map(x -> new WechatUser(x.getAppId(), x.getOpenId()))
                    .collect(Collectors.toList());

            return UnionId.ofRepo(entry.getValue().get(0).getUnionId(), wechatUserList);

        }));
    }

    @Override
    public UnionId find(WechatUser wechatUser) {
        WechatUnionIdExample example = new WechatUnionIdExample();
        example.createCriteria()
                .andAppIdEqualTo(wechatUser.getAppId())
                .andOpenIdEqualTo(wechatUser.getOpenId());

        List<WechatUnionId> unionIdList = wechatUnionIdMapper.selectByExample(example);

        WechatUnionId unionId = unionIdList.stream().findFirst().orElse(null);

        if (unionId == null) {
            return UnionId.ofNull();
        }

        return this.find(unionId.getUnionId());
    }

    @Override
    public UnionId findByUserName(String userName) {
        List<WechatUnionId> unionIdList = wechatUnionIdMapper.selectByUserName(userName);
        return getUnionIdBO(unionIdList);
    }

    @NotNull
    private UnionId getUnionIdBO(List<WechatUnionId> unionIdList) {
        if (CollectionUtils.isEmpty(unionIdList)) {
            return UnionId.ofNull();
        }

        List<WechatUser> wechatUserList = unionIdList.stream()
                .map(x -> new WechatUser(x.getAppId(), x.getOpenId()))
                .collect(Collectors.toList());

        return UnionId.ofRepo(unionIdList.get(0).getUnionId(), wechatUserList);
    }
}
