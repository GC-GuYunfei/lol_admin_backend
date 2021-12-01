package com.jiangfendou.loladmin.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2021-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    /**
     * 头像
     */
    private String avatar;

    private String email;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLogin;

    private Integer lockVersion;

    private Integer status;

}
