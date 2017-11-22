package com.lawrence.fatalis.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class TestObj implements Serializable {

    private String id;
    private int num;
    private float fee;
    private List<Object> list;
    private Map<String, Object> map;
    private JSONObject json;
    private JSONArray array;

}
