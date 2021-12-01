package com.jiangfendou.loladmin.service;

import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangfendou.loladmin.model.response.SysUserResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2021-11-07
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * getByUseName()
     * @Param userName userName
     * @return SysUser SysUser
     * */
    SysUser getByUseName(String userName);

    /**
     * getUserAuthorityInfo()
     * @Param userId userId
     * @return String String
     * */
    String getUserAuthorityInfo(Long userId);

    /**
     * clearUserAuthorityInfo()
     * @Param userId userId
     * */
    void clearUserAuthorityInfo(String username);

    /**
     * clearUserAuthorityInfoByRoleId()
     * @Param rolId rolId
     * */
    void clearUserAuthorityInfoByRoleId(Long rolId);

    /**
     * clearUserAuthorityInfoByMenuId()
     * @Param menuId menuId
     * */
    void clearUserAuthorityInfoByMenuId(Long menuId);

    /**
     * getUserInfo()
     * @Param userId userId
     * */
    SysUserResponse getUserInfo(Long userId) throws BusinessException;
}
