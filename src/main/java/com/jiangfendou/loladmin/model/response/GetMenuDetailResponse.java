package com.jiangfendou.loladmin.model.response;

import lombok.Data;

@Data
public class GetMenuDetailResponse {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long parentId;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权（多个用逗号隔开，如：user:list,user:create）
     */
    private String perms;

    private String component;

    /**
     * 类型： 0：目录  1：菜单  2、按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    private Integer lockVersion;

    private Integer status;

}
