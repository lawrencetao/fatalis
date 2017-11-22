package com.lawrence.fatalis.dao;

import com.lawrence.fatalis.model.Commission;

import java.util.List;

public interface CommissionDao {

    void insert(Commission commission);
    Commission select(String sid);
    List<Commission> query();

}
