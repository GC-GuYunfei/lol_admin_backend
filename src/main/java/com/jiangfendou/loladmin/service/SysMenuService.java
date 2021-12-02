package com.jiangfendou.loladmin.service;

import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangfendou.loladmin.model.response.GetMenuDetailResponse;
import com.jiangfendou.loladmin.model.response.MenuAuthorityResponse;
import com.jiangfendou.loladmin.model.response.SearchMenusResponse;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2021-11-07
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * getMenuNav()
     * @Param principal principal
     * @return MenuAuthorityResponse MenuAuthorityResponse
     * */
    MenuAuthorityResponse getMenuNav(Long userId) throws BusinessException;

    /**
     * searchMenus()
     * @return List<SearchMenusResponse> List<SearchMenusResponse>
     * */
    List<SearchMenusResponse> searchMenus() throws BusinessException;

    /**
     * getMenuDetail()
     * @return List<GetMenuDetailResponse> List<GetMenuDetailResponse>
     * */
    GetMenuDetailResponse getMenuDetail(Long userId) throws BusinessException;

}
