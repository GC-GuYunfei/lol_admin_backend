package com.jiangfendou.loladmin.mapper;

import com.jiangfendou.loladmin.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * getNavMenusIds()
     * @Param userId userId
     * @Return List<Long> List<Long>
     * */
    List<Long> getNavMenusIds(@Param("userId") Long userId);
}
