package com.lawrence.fatalis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FataAuthority implements Serializable {

    private String authorityId;
    private String authorityName;
    private String mainUrl;
    private String permission;
    private String sort;
    private String createTime;
    private String updateTime;
    private String status;
    private String branchUrl;

}