package com.hackerda.platform.domain.student;

import com.hackerda.platform.utils.DESUtil;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;

@Data
public class StudentUserBO {

    private StudentAccount account;

    private String password;

    private String name;

    private String sex;

    private String ethnic;

    private Integer urpClassNum;

    private Boolean isCorrect;

    private String academyName;

    private String subjectName;

    private String className;

    private boolean msgHasCheck;

    @ToStringExclude
    private String key;

    private boolean saveOrUpdate;


    public void updatePassword(String enablePassword) {
        this.password = DESUtil.encrypt(enablePassword, account + key);
        this.isCorrect = true;
        this.msgHasCheck = true;
        this.saveOrUpdate = true;
    }

    public boolean checkEnablePasswordIsCorrect(String enablePassword) {
        return isCorrect && DESUtil.encrypt(enablePassword, account + key).equals(password);
    }

    /**
     * 获取年级
     * @return 2017级返回2017
     */
    public String getGrade(){
        return account.toString().substring(0, 4);
    }


    public String getEnablePassword() {
        return DESUtil.decrypt(this.password, this.account + key);
    }

    public boolean isUsingDefaultPassword() {
        if(msgHasCheck) {
            return "1".equals(getEnablePassword());
        } else {
            return false;
        }
    }

    public boolean canLogin() {
        if(!msgHasCheck) {
            return true;
        } else {
            return isCorrect;
        }
    }

    public boolean isPasswordCorrect () {
        return BooleanUtils.toBoolean(this.isCorrect);
    }


    public void updateClassInfo(Integer urpClassNum, String academyName, String subjectName, String className) {
        this.urpClassNum = urpClassNum;
        this.academyName = academyName;
        this.subjectName = subjectName;
        this.className = className;

        this.saveOrUpdate = true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StudentUserBO that = (StudentUserBO) o;

        return new EqualsBuilder()
                .append(account, that.account)
                .append(password, that.password)
                .append(name, that.name)
                .append(sex, that.sex)
                .append(ethnic, that.ethnic)
                .append(urpClassNum, that.urpClassNum)
                .append(academyName, that.academyName)
                .append(subjectName, that.subjectName)
                .append(className, that.className)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(account)
                .append(password)
                .append(name)
                .append(sex)
                .append(ethnic)
                .append(urpClassNum)
                .append(academyName)
                .append(subjectName)
                .append(className)
                .toHashCode();
    }


    public static void main(String[] args) {
        String decrypt = DESUtil.decrypt("mypa4b5Un3Zjzx3SRSH55vX/+pzgObjf", "2017023756" + "asd.@#$as!@#");

        System.out.println(decrypt);
    }
}
