package com.jiangfendou.loladmin.mapper;

import com.jiangfendou.loladmin.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfendou.loladmin.entity.SysUser;
import com.jiangfendou.loladmin.model.response.MenuAuthorityResponse;
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
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * searchUserList()
     * @Param menuId menuId
     * @Return List<SysUser>  List<SysUser>
     * */
    List<SysUser> searchUserList(Long menuId);

    /**
     * getNavMenuIds()
     * @Param userId userId
     * @return List<Long> List<Long>
     * */
    List<Long> getNavMenuIds(@Param("userId") Long userId);

}
