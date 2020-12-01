package com.hackerda.platform.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsExclude;

@Getter
@ToString
@EqualsAndHashCode
public class PermissionBO {

    public static final String VIEW = "view";

    public static final String COMMENT = "comment";

    public static final String DELETE = "delete";

    public static final String TOP = "top";

    public static final String RECOMMEND = "recommend";

    @Setter
    private Integer id;

    private final String name;

    private final String code;

    private final int priority;


    public PermissionBO( String name, String code, int priority) {
        this.name = name;
        this.code = code;
        this.priority = priority;
    }

    public PermissionBO(Integer id, String name, String code, int priority) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.priority = priority;
    }

    public boolean isNew() {
        return id == null;
    }

}
