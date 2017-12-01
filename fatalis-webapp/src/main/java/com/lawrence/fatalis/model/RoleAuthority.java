package com.lawrence.fatalis.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleAuthority implements Serializable {

    private String roleId;
    private String authorityId;
    private String createTime;
    private String updateTime;
    private String status;

}