package com.lawrence.fatalis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FataRole implements Serializable {

    private String roleId;
    private String roleName;
    private String roleCode;
    private String createTime;
    private String updateTime;
    private String status;

}