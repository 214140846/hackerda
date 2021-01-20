package com.hackerda.platform.domain.student;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Slf4j
@Getter
public class StudentSettingsBO {

    private final StudentAccount studentAccount;

    @Switch(name = "grade")
    private boolean gradePushSwitch;

    @Switch(name = "course")
    private boolean coursePushSwitch;

    @Switch(name = "exam")
    private boolean examPushSwitch;

    @Switch(name = "comment")
    private boolean commentPushSwitch;

    private boolean modify;


    public void updateSwitch(String updateField, boolean value) {

        Map<String, Field> fieldMap = Arrays.stream(this.getClass().getDeclaredFields())
                .filter(x -> x.getDeclaredAnnotation(Switch.class) != null)
                .collect(Collectors.toMap((x -> x.getDeclaredAnnotation(Switch.class).name()), x -> x));

        Field field = fieldMap.get(updateField);

        if(field == null) {
            log.error("update not present field switch {}", updateField);
            return;
        }

        try {
            if(field.get(this).equals(value)) {
                return;
            }
            field.setAccessible(true);
            field.set(this, value);
            modify = true;

        } catch (IllegalAccessException e) {
            log.error("update field switch {} error value {}", updateField, value);
        }
    }


    public static StudentSettingsBO create(StudentAccount studentAccount) {

        return StudentSettingsBO.builder()
                .studentAccount(studentAccount)
                .gradePushSwitch(true)
                .coursePushSwitch(true)
                .examPushSwitch(true)
                .commentPushSwitch(true)
                .build();
    }


}
