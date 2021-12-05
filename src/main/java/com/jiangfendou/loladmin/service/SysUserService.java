package com.jiangfendou.loladmin.service;

import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangfendou.loladmin.model.response.SysUserResponse;
import org.springframework.stereotype.Repository;

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
     * @param userName userName
     * @return SysUser SysUser
     * */
    SysUser getByUseName(String userName);

    /**
     * getUserAuthorityInfo()
     * @param userId userId
     * @return String String
     * */
    String getUserAuthorityInfo(Long userId);

    /**
     * clearUserAuthorityInfo()
     * @param userId userId
     * @param username username
     * */
    void clearUserAuthorityInfo(String username, Long userId);

    /**
     * clearUserAuthorityInfoByRoleId()
     * @param rolId rolId
     * */
    void clearUserAuthorityInfoByRoleId(Long rolId);

    /**
     * clearUserAuthorityInfoByMenuId()
     * @param menuId menuId
     * */
    void clearUserAuthorityInfoByMenuId(Long menuId);

    /**
     * getUserInfo()
     * @param userId userId
     * @return SysUserResponse SysUserResponse
     * @throws BusinessException BusinessException
     * */
    SysUserResponse getUserInfo(Long userId) throws BusinessException;
}
