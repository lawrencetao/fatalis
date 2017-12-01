package com.lawrence.fatalis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRole implements Serializable {

    private String userId;
    private String roleId;
    private String createTime;
    private String updateTime;
    private String status;

}