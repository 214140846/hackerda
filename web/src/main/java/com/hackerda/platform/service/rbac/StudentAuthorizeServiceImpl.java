package com.hackerda.platform.service.rbac;

import com.hackerda.platform.application.StudentBindApp;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.StudentUserRepository;
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
    private StudentUserRepository studentUserRepository;


    @Override
    public StudentUserDetailVO studentAuthorize(@Nonnull String account, @Nonnull String password, @Nonnull String appId, @Nonnull String openid) {
        StudentUserBO studentUser = studentBindApp.bindByOpenId(account, password, appId, openid);

        return getVO(studentUser);
    }

    @Override
    public StudentUserDetailVO appStudentAuthorize(@Nonnull String account, @Nonnull String password, @Nonnull String appId, @Nonnull String code) {
        StudentUserBO studentUser = studentBindApp.bindByCode(account, password, appId, code);

        return getVO(studentUser);
    }

    @Override
    public void appStudentRevokeAuthorize(@Nonnull String account, @Nonnull String appId) {
        StudentUserBO studentUserBO = (StudentUserBO) SecurityUtils.getSubject().getPrincipal();

        if(studentUserBO == null  && StringUtils.isNotEmpty(account)) {
            studentUserBO = studentUserRepository.getByAccount(Integer.parseInt(account));
        }

        if(studentUserBO == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_MISS, account+"信息不存在");
        }


        studentBindApp.unbindByPlatform(studentUserBO, appId);

    }

    private StudentUserDetailVO getVO(StudentUserBO studentUser){
        String account = studentUser.getAccount().toString();
        String token = JwtUtils.signForUserDetail(account, new String[0], new String[0], account);

        StudentUserDetailVO vo = new StudentUserDetailVO();

        vo.setAccount(studentUser.getAccount());
        vo.setName(studentUser.getName());
        vo.setSex(studentUser.getSex());
        vo.setEthnic(studentUser.getEthnic());
        vo.setAcademyName(studentUser.getAcademyName());
        vo.setSubjectName(studentUser.getSubjectName());
        vo.setClassName(studentUser.getClassName());
        vo.setToken(token);

        return vo;
    }
}
