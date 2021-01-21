package com.hackerda.platform.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class StudentSettingVO {

    private List<SwitchItem> switchItemList = Collections.emptyList();
    private boolean hasBindPlus = false;


    @Data
    @AllArgsConstructor
    public static class SwitchItem {

        private String key;
        private String name;
        private boolean value;

    }

}
