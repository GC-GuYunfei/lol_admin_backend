package com.jiangfendou.loladmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiangfendou.loladmin.common.ApiError;
import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysMenu;
import com.jiangfendou.loladmin.entity.SysUser;
import com.jiangfendou.loladmin.enums.DeletedFlag;
import com.jiangfendou.loladmin.enums.ErrorCode;
import com.jiangfendou.loladmin.mapper.SysMenuMapper;
import com.jiangfendou.loladmin.mapper.SysRoleMapper;
import com.jiangfendou.loladmin.mapper.SysUserMapper;
import com.jiangfendou.loladmin.model.request.SearchUserRequest;
import com.jiangfendou.loladmin.model.response.RoleResponse;
import com.jiangfendou.loladmin.model.response.SearchUserResponse;
import com.jiangfendou.loladmin.model.response.SysUserResponse;
import com.jiangfendou.loladmin.service.SysMenuService;
import com.jiangfendou.loladmin.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangfendou.loladmin.util.RedisUtil;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final String GRANTED_AUTHORITY = "GrantedAuthority:";

    private static final String USER_MENU = "UserMenu:";

    private static final String ROLE = "ROLE_";

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SysUser getByUseName(String userName) {
        return getOne(new QueryWrapper<SysUser>().eq("username", userName));
    }
    @Override
    public String getUserAuthorityInfo(Long userId) {
        // ROLE_admin,sys:user:list .....
        String authority = "";
        SysUser sysUser = this.getOne(new QueryWrapper<SysUser>().eq("id", userId)
            .eq("is_deleted", DeletedFlag.NOT_DELETED));
        if (redisUtil.hasKey(GRANTED_AUTHORITY + sysUser.getUsername())) {
            authority = (String)redisUtil.get(GRANTED_AUTHORITY + sysUser.getUsername());
            log.info("redis获取用户信息 -------{}, Authority = {}", GRANTED_AUTHORITY + sysUser.getUsername(), authority);
        } else {
            // 获取角色
            List<RoleResponse> roles = sysRoleMapper.searchRoleList(userId);
            if (!roles.isEmpty()) {
                 authority = roles.stream().map(role -> ROLE + role.getCode())
                    .collect(Collectors.joining(","));
            }
            // 获取菜单权限
            List<Long> menuIds = sysUserMapper.getNavMenusIds(userId);
            if (!menuIds.isEmpty()) {
                List<SysMenu> sysMenus = sysMenuService.listByIds(menuIds);
                String menuPerms = sysMenus.stream().map(SysMenu::getPerms)
                    .collect(Collectors.joining(","));
                authority = authority.concat(",").concat(menuPerms);
            }
            redisUtil.set(GRANTED_AUTHORITY + sysUser.getUsername(), authority, 60*60);
        }

        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username, Long userId) {
        redisUtil.del(GRANTED_AUTHORITY + username);
        redisUtil.del(USER_MENU + userId);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long rolId) {
        List<SysUser> sysUsers = sysRoleMapper.searchUserList(rolId);
        sysUsers.forEach(sysUser -> {
            redisUtil.del(GRANTED_AUTHORITY + sysUser.getUsername());
        });
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers = sysMenuMapper.searchUserList(menuId);
        sysUsers.forEach(sysUser -> {
            redisUtil.del(GRANTED_AUTHORITY + sysUser.getUsername());
        });
    }

    @Override
    public SysUserResponse getUserInfo(Long userId) throws BusinessException {
        SysUser sysUser = this.getOne(new QueryWrapper<SysUser>().eq("id", userId)
            .eq("is_deleted", DeletedFlag.NOT_DELETED));
        if (Objects.isNull(sysUser)) {
            log.info("没有找到的指定用户信息：userId = {}", userId);
            throw new BusinessException(HttpStatus.NOT_FOUND,
                new ApiError(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()));
        }
        return SysUserResponse.convert(sysUser);
    }

    @Override
    public IPage<SearchUserResponse> searchUser(SearchUserRequest searchUserRequest) {
        // 创建分页参数
        IPage<SearchUserResponse> page = new Page<>(searchUserRequest.getCurrent(),searchUserRequest.getSize());
        IPage<SearchUserResponse> result = sysUserMapper.searchUser(page, searchUserRequest);
        return result;
    }
}
