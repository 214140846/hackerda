package com.hackerda.platform.domain.student;

public interface StudentSettingRepository {

    StudentSettingsBO get(StudentAccount studentAccount);

    void save(StudentSettingsBO studentSettingsBO);

    void update(StudentSettingsBO studentSettingsBO);
}
