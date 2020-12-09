package com.hackerda.platform.application;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnionIdApp {

    @Autowired
    private UnionIdRepository unionIdRepository;


    public UnionId getUnionId (String unionId, WechatUser wechatUser) {

        UnionId savedUnionId = unionIdRepository.find(unionId);

        if (savedUnionId.isEmpty()) {
            UnionId ofNew = UnionId.ofNew(unionId);
            ofNew.bindOpenid(wechatUser);

            unionIdRepository.save(ofNew);

            return ofNew;
        }

        if (!savedUnionId.hasOpenId(wechatUser)) {
            savedUnionId.bindOpenid(wechatUser);
            unionIdRepository.save(savedUnionId);
        }

        return savedUnionId;

    }


}
