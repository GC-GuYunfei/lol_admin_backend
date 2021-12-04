package com.jiangfendou.loladmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangfendou.loladmin.common.ApiError;
import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysMenu;
import com.jiangfendou.loladmin.entity.SysUser;
import com.jiangfendou.loladmin.enums.DeletedFlag;
import com.jiangfendou.loladmin.enums.ErrorCode;
import com.jiangfendou.loladmin.mapper.SysMenuMapper;
import com.jiangfendou.loladmin.model.request.UpdateMenuRequest;
import com.jiangfendou.loladmin.model.response.GetMenuDetailResponse;
import com.jiangfendou.loladmin.model.response.MenuAuthorityResponse;
import com.jiangfendou.loladmin.model.response.SearchMenusResponse;
import com.jiangfendou.loladmin.service.SysMenuService;
import com.jiangfendou.loladmin.service.SysUserService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("id", userId)
            .eq("is_deleted", DeletedFlag.NOT_DELETED.getValue()));
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

    @Override
    public List<SearchMenusResponse> searchMenus() throws BusinessException {
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("order_num")
            .eq("is_deleted", DeletedFlag.NOT_DELETED.getValue()));
        if (sysMenus.isEmpty()) {
            log.info("menu list is empty");
            throw new BusinessException(HttpStatus.NOT_FOUND,
                new ApiError(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()));
        }
        List<SysMenu> sysMenusTree = buildTreeMenu(sysMenus);
        return convertMenuList(sysMenusTree);
    }

    @Override
    public GetMenuDetailResponse getMenuDetail(Long userId) throws BusinessException {
        GetMenuDetailResponse getMenuDetailResponse = new GetMenuDetailResponse();
        SysMenu sysMenu = this.getOne(new QueryWrapper<SysMenu>()
            .eq("id", userId)
            .eq("is_deleted", DeletedFlag.NOT_DELETED.getValue()));
        if (Objects.isNull(sysMenu)) {
            log.info("getMenuDetail() ---目标数据没有被找到， userId = {}", userId);
            throw new BusinessException(HttpStatus.NOT_FOUND,
                new ApiError(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()));
        }
        BeanUtils.copyProperties(sysMenu, getMenuDetailResponse);
        return getMenuDetailResponse;
    }

    @Override
    public void updateMenu(UpdateMenuRequest updateMenuRequest) throws BusinessException {
        SysMenu sysMenu = this.getOne(new QueryWrapper<SysMenu>()
            .eq("id", updateMenuRequest.getId())
            .eq("is_deleted", DeletedFlag.NOT_DELETED.getValue()));
        if (Objects.isNull(sysMenu)) {
            log.info("updateMenu() ---目标数据没有被找到， userId = {}", updateMenuRequest.getId());
            throw new BusinessException(HttpStatus.NOT_FOUND,
                new ApiError(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()));
        }
        SysMenu perms = this.getOne(new QueryWrapper<SysMenu>()
            .eq("perms", updateMenuRequest.getPerms())
            .eq("is_deleted", DeletedFlag.NOT_DELETED));
        if (perms != null) {
            log.info("updateMenu() ---权限编码已存在， Perms = {}", updateMenuRequest.getPerms());
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                new ApiError(ErrorCode.MENU_PERM_CODE_EXIST.getCode(),
                    String.format(ErrorCode.MENU_PERM_CODE_EXIST.getMessage(), updateMenuRequest.getPerms())));
        }
        // 修改menu数据
        sysMenuMapper.updateMenu(updateMenuRequest);

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

    private List<SearchMenusResponse> convertMenuList(List<SysMenu> sysMenuTrees) {
        List<SearchMenusResponse> menuList = new ArrayList<>();
        sysMenuTrees.forEach(sysMenu -> {
            SearchMenusResponse menuResource = new SearchMenusResponse();
            BeanUtils.copyProperties(sysMenu, menuResource);
            if (sysMenu.getChildren().size() > 0) {
                menuResource.setChildren(convertMenuList(sysMenu.getChildren()));
            }
            menuList.add(menuResource);
        });
        return menuList;
    }
}
