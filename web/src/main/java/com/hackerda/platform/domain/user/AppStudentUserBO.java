package com.hackerda.platform.domain.user;

import com.hackerda.platform.domain.student.StudentAccount;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AppStudentUserBO extends AppUserBO{

    private final StudentAccount account;

    public AppStudentUserBO(StudentAccount studentAccount, String nickname, String password, String avatarPath, PhoneNumber mobile,
                            Gender gender, String introduction) {
        super(UUID.randomUUID().toString(), nickname, password, avatarPath, mobile, gender, introduction);
        super.setUserType(UserType.Student);
        super.setUseDefaultPassword(true);
        this.account = studentAccount;
    }

    public AppStudentUserBO(StudentAccount studentAccount,String userName, String nickname, String password,
                            String salt, String avatarPath,
                            PhoneNumber mobile,
                            Gender gender, String introduction, boolean useDefaultPassword, List<RoleBO> roleList) {
        super(userName, nickname, password, salt, avatarPath, mobile, gender, introduction, useDefaultPassword, roleList);
        super.setUserType(UserType.Student);

        this.account = studentAccount;
    }

    public StudentAccount getAccount() {
        return account;
    }



}
