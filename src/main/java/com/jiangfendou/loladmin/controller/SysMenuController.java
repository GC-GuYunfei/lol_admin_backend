package com.jiangfendou.loladmin.controller;


import com.jiangfendou.loladmin.common.ApiResponse;
import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.model.response.MenuAuthorityResponse;
import com.jiangfendou.loladmin.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-11-07
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/nav")
    public ApiResponse<MenuAuthorityResponse> getMenuNav(Long userId) throws BusinessException {
        MenuAuthorityResponse menuNav = sysMenuService.getMenuNav(userId);
        return ApiResponse.success(menuNav);
    }
}
