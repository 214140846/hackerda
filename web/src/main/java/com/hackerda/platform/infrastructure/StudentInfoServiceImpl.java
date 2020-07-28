package com.hackerda.platform.infrastructure;

import com.hackerda.platform.infrastructure.dao.WechatOpenIdDao;
import com.hackerda.platform.domain.student.StudentInfoService;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.pojo.StudentUser;
import com.hackerda.platform.pojo.UrpClass;
import com.hackerda.platform.pojo.WechatOpenid;
import com.hackerda.platform.service.ClassService;
import com.hackerda.platform.service.NewUrpSpiderService;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StudentInfoServiceImpl implements StudentInfoService {
    @Autowired
    private NewUrpSpiderService newUrpSpiderService;
    @Autowired
    private WechatOpenIdDao wechatOpenIdDao;
    @Autowired
    private ClassService classService;

    @Override
    public boolean checkPasswordValid(String account, String enablePassword) {
        try {
            newUrpSpiderService.checkStudentPassword(account, enablePassword);
            return true;
        }catch (PasswordUnCorrectException e){
            return false;
        }catch (Throwable throwable){
            log.error("check account {} password error", account, throwable);
            return false;
        }
    }

    @Override
    public boolean checkCanBind(String account, String appId, String openid) {

        WechatOpenid wechatOpenid = wechatOpenIdDao.selectBindUser(Integer.parseInt(account), appId);

        if(wechatOpenid == null) {
            return true;
        }else return wechatOpenid.getOpenid().equals(openid);
    }

    @Override
    public StudentUserBO getStudentInfo(String account, String enablePassword) {

        StudentUser userInfo = newUrpSpiderService.getStudentUserInfo(account, enablePassword);
        UrpClass urpClass = classService.getClassByName(userInfo.getClassName(), userInfo.getAccount().toString());

        StudentUserBO user = new StudentUserBO();

        user.setAccount(userInfo.getAccount());
        user.setPassword(userInfo.getPassword());
        user.setIsCorrect(userInfo.getIsCorrect());
        user.setUrpClassNum(Integer.parseInt(urpClass.getClassNum()));
        user.setAcademyName(userInfo.getAcademyName());
        user.setClassName(userInfo.getClassName());
        user.setEthnic(userInfo.getEthnic());
        user.setSubjectName(userInfo.getSubjectName());
        user.setSex(userInfo.getSex());
        user.setName(userInfo.getName());
        user.setSaveOrUpdate(true);

        return user;
    }
}
