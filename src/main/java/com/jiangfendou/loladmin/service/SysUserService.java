package com.jiangfendou.loladmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiangfendou.loladmin.common.ApiResponse;
import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangfendou.loladmin.model.request.SaveUserRequest;
import com.jiangfendou.loladmin.model.request.SearchUserRequest;
import com.jiangfendou.loladmin.model.request.UpdateUserRequest;
import com.jiangfendou.loladmin.model.response.GetUserDetailResponse;
import com.jiangfendou.loladmin.model.response.SearchUserResponse;
import com.jiangfendou.loladmin.model.response.SysUserResponse;
import java.util.List;
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
     * */
    void clearUserAuthorityInfo(Long userId);

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

    /**
     * searchUser()
     * @param searchUserRequest searchUserRequest
     * @return SearchUserResponse SearchUserResponse
     * */
    IPage<SearchUserResponse> searchUser(SearchUserRequest searchUserRequest);

    /**
     * saveUser()
     * @param saveUserRequest saveUserRequest
     * */
    void saveUser(SaveUserRequest saveUserRequest) throws BusinessException;

    /**
     * detailUser()
     * @param userId userId
     * @return GetUserDetailResponse GetUserDetailResponse
     * */
    GetUserDetailResponse detailUser(Long userId) throws BusinessException;

    /**
     * updateUser()
     * @param updateUserRequest updateUserRequest
     * */
    void updateUser(UpdateUserRequest updateUserRequest) throws BusinessException;
}
