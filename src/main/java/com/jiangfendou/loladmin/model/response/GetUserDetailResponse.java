package com.jiangfendou.loladmin.model.response;

import lombok.Data;

/**
 * GetUserDetailResponse.
 * @author jiangmh
 */
@Data
public class GetUserDetailResponse {

    private String username;

    private String password;

    private String phone;

    private String email;

    private String city;

    private Integer status;
}
