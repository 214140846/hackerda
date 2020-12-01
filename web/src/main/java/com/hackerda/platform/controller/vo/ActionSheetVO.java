package com.hackerda.platform.controller.vo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class ActionSheetVO {

    private String code;
    private String desc;

    public static ActionSheetVO delete = new ActionSheetVO("delete", "删除");
    public static ActionSheetVO top = new ActionSheetVO("top", "置顶");
    public static ActionSheetVO revoke_top = new ActionSheetVO("revoke_top", "置顶");
    public static ActionSheetVO recommend = new ActionSheetVO("recommend", "加入精选");
    public static ActionSheetVO revoke_recommend = new ActionSheetVO("revoke_recommend", "取消推荐");

    public static ImmutableMap.Builder<String, ActionSheetVO> builder = ImmutableMap.builder();

    public static Map<String, ActionSheetVO> codeMap = builder
            .put(delete.code, delete)
            .put(top.code, top)
            .put(revoke_top.code, revoke_top)
            .put(recommend.code, recommend)
            .put(revoke_recommend.code, revoke_recommend)
            .build();

    private ActionSheetVO(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActionSheetVO getByCode(String code){
        return codeMap.get(code);
    }
}
