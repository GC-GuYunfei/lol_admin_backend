package com.jiangfendou.loladmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangfendou.loladmin.common.ApiError;
import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysMenu;
import com.jiangfendou.loladmin.entity.SysUser;
import com.jiangfendou.loladmin.enums.ErrorCode;
import com.jiangfendou.loladmin.mapper.SysMenuMapper;
import com.jiangfendou.loladmin.model.response.MenuAuthorityResponse;
import com.jiangfendou.loladmin.service.SysMenuService;
import com.jiangfendou.loladmin.service.SysUserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-11-07
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public MenuAuthorityResponse getMenuNav(Long userId) throws BusinessException {
        MenuAuthorityResponse menuAuthorityResponse = new MenuAuthorityResponse();
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            log.info("没有找到的指定用户：userId = {}", userId);
            throw new BusinessException(HttpStatus.NOT_FOUND,
                new ApiError(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()));
        }
        // 获取用户权限信息
        String userAuthorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        if (StringUtils.isBlank(userAuthorityInfo)) {
            menuAuthorityResponse.setAuthorities(new ArrayList<>());
        } else {
            String[] userAuthorityArray = userAuthorityInfo.split(",");
            List<String> userAuthorityList = Arrays.asList(userAuthorityArray);
            menuAuthorityResponse.setAuthorities(userAuthorityList);
        }

        List<Long> navMenuIds = sysMenuMapper.getNavMenuIds(userId);
        List<SysMenu> sysMenus = this.listByIds(navMenuIds);

        if (navMenuIds.isEmpty() || sysMenus.isEmpty()) {
            log.info("没有找到的指定用户菜单：userId = {}", userId);
            throw new BusinessException(HttpStatus.NOT_FOUND,
                new ApiError(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()));
        }
        // 转树状结构
        List<SysMenu> sysMenuTrees = buildTreeMenu(sysMenus);
        // 实体转DTO
        List<MenuAuthorityResponse.Menu> menus = convert(sysMenuTrees);
        menuAuthorityResponse.setNav(menus);
        return menuAuthorityResponse;
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuTrees) {
        List<SysMenu> finalMenus = new ArrayList<>();
        sysMenuTrees.forEach(menu -> {
            sysMenuTrees.forEach(menuChild -> {
                if (Objects.equals(menu.getId(), menuChild.getParentId())) {
                    menu.getChildren().add(menuChild);
                }
            });
            if (Objects.equals(menu.getParentId(), 0L)) {
                System.out.println("menu.getId()" + menu.getId());
                finalMenus.add(menu);
            }
        });
        return finalMenus;
    }

    private List<MenuAuthorityResponse.Menu> convert(List<SysMenu> sysMenuTrees) {
        List<MenuAuthorityResponse.Menu> menuList = new ArrayList<>();
        sysMenuTrees.forEach(sysMenu -> {
            MenuAuthorityResponse.Menu menuResource = new MenuAuthorityResponse.Menu();
            menuResource.setId(sysMenu.getId());
            menuResource.setName(sysMenu.getPerms());
            menuResource.setPath(sysMenu.getPath());
            menuResource.setIcon(sysMenu.getIcon());
            menuResource.setComponent(sysMenu.getComponent());
            menuResource.setTitle(sysMenu.getName());
            if (sysMenu.getChildren().size() > 0) {
                menuResource.setChildren(convert(sysMenu.getChildren()));
            }
            menuList.add(menuResource);
        });

        return menuList;
    }
}
