package com.jiangfendou.loladmin.controller;

import com.jiangfendou.loladmin.common.ApiResponse;
import com.jiangfendou.loladmin.common.BusinessException;
import com.jiangfendou.loladmin.entity.SysUser;
import com.jiangfendou.loladmin.service.SysUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public ApiResponse<List<SysUser>> test() {
        return ApiResponse.success(sysUserService.list());
    }


    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/password")
    public ApiResponse<String> password() {
        // 加密后的密码
        String password = bCryptPasswordEncoder.encode("123");
        boolean matches = bCryptPasswordEncoder.matches("123", password);
        System.out.println("匹配结果" + matches);
        System.out.println(password);
        return ApiResponse.success(password);
    }

//    @Autowired
//    private SysUserService sysUserService;

    @GetMapping("/userInfo")
    public ApiResponse getUserInfo(Long userId) throws BusinessException {
        return ApiResponse.success(sysUserService.getUserInfo(userId));
    }
}
