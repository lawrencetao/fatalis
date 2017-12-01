package com.lawrence.fatalis.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FataUser implements Serializable {

    private String userId;
    private String userName;
    private String mobile;
    private String password;
    private String salt;
    private String createTime;
    private String updateTime;
    private String status;

}