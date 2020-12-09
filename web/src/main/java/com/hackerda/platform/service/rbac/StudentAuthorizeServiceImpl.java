package com.hackerda.platform.service.rbac;

import com.hackerda.platform.application.StudentBindApp;
import com.hackerda.platform.application.UnionIdApp;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.user.PhoneNumber;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.exception.BusinessException;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.controller.vo.StudentUserDetailVO;
import com.hackerda.platform.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

/**
 * @author JR Chan
 */
@Service
public class StudentAuthorizeServiceImpl implements UserAuthorizeService{

    @Autowired
    private StudentBindApp studentBindApp;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UnionIdApp unionIdApp;


    @Override
    public StudentUserDetailVO studentAuthorize(@Nonnull String account, @Nonnull String password,
                                                @Nonnull String appId, @Nonnull String openid, @Nonnull String unionId) {
        StudentAccount studentAccount = new StudentAccount(account);
        WechatUser wechatUser = new WechatUser(appId, openid);

        UnionId wechatUnionId = unionIdApp.getUnionId(unionId, wechatUser);
        WechatStudentUserBO studentUser = studentBindApp.bindByUnionId(studentAccount, password, wechatUnionId, wechatUser);

        return getVO(studentUser, appId);
    }

    @Override
    public StudentUserDetailVO appStudentAuthorize(@Nonnull String account, @Nonnull String password, @Nonnull String appId, @Nonnull String code) {

        StudentAccount studentAccount = new StudentAccount(account);
        WechatStudentUserBO studentUser = studentBindApp.bindByCode(studentAccount, password, appId, code);

        return getVO(studentUser, appId);
    }

    @Override
    public StudentUserDetailVO bindCommonWechatUser(@Nonnull String account, @Nonnull String phoneNumber,
                                                    @Nonnull String appId, @Nonnull String openId) {

        StudentAccount studentAccount = new StudentAccount(account);
        PhoneNumber number = new PhoneNumber(phoneNumber);
        WechatUser wechatUser = new WechatUser(appId, openId);
        WechatStudentUserBO studentUser = studentBindApp.bindCommonWechatUser(studentAccount, number, wechatUser);

        return getVO(studentUser, appId);
    }

    @Override
    public void appStudentRevokeAuthorize(@Nonnull String account, @Nonnull String appId) {
        WechatStudentUserBO wechatStudentUserBO;

        if(StringUtils.isNotEmpty(account)) {
            wechatStudentUserBO = studentRepository.findWetChatUser(new StudentAccount(account));

        }else {
            StudentUserBO studentUserBO = (StudentUserBO) SecurityUtils.getSubject().getPrincipal();
            wechatStudentUserBO = studentRepository.findWetChatUser(studentUserBO.getAccount());
        }

        if(wechatStudentUserBO == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_MISS, account+"信息不存在");
        }


        studentBindApp.unbindUnionId(wechatStudentUserBO);

    }

    private StudentUserDetailVO getVO(WechatStudentUserBO studentUser, String appId){
        String account = studentUser.getAccount().toString();
        String token = JwtUtils.signForWechatStudent(account, appId, studentUser.getOpenid(appId));

        StudentUserDetailVO vo = new StudentUserDetailVO();

        vo.setAccount(studentUser.getAccount().getInt());
        vo.setName(studentUser.getName());
        vo.setSex(studentUser.getSex());
        vo.setEthnic(studentUser.getEthnic());
        vo.setAcademyName(studentUser.getAcademyName());
        vo.setSubjectName(studentUser.getSubjectName());
        vo.setClassName(studentUser.getClassName());
        vo.setToken(token);
        vo.setUseDefaultPassword(studentUser.isUsingDefaultPassword());

        return vo;
    }

}
