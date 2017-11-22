package com.lawrence.fatalis.service;

import com.lawrence.fatalis.dao.CommissionDao;
import com.lawrence.fatalis.model.Commission;

import javax.annotation.Resource;
import java.util.List;

public interface CommissionService {

    public Commission getCommission(String sid);
    public List<Commission> queryCommission();
    public void addCommission(Commission commission);

}
