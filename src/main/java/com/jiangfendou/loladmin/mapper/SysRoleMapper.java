package com.jiangfendou.loladmin.mapper;

import com.jiangfendou.loladmin.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfendou.loladmin.entity.SysUser;
import com.jiangfendou.loladmin.model.response.RoleResponse;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-11-07
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * searchRoleList()
     * @Param userId userId
     * @return List<RoleResponse> List<RoleResponse>
     * */
    List<RoleResponse> searchRoleList(@Param("userId") Long userId);

    /**
     * searchUserList()
     * @Param roleId roleId
     * @return List<SysUser> List<SysUser>
     * */
    List<SysUser> searchUserList(@Param("roleId") Long roleId);
}
