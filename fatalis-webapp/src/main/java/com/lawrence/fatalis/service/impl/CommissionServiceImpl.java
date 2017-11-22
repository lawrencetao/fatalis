package com.lawrence.fatalis.service.impl;

import com.lawrence.fatalis.annotation.ReadDataSource;
import com.lawrence.fatalis.annotation.WriteDataSource;
import com.lawrence.fatalis.dao.CommissionDao;
import com.lawrence.fatalis.model.Commission;
import com.lawrence.fatalis.service.CommissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommissionServiceImpl implements CommissionService {

    @Resource
    private CommissionDao commissionDao;

    @Override
    public Commission getCommission(String sid) {

        return commissionDao.select(sid);
    }

    @Override
    @ReadDataSource
    public List<Commission> queryCommission() {

        return commissionDao.query();
    }

    @Override
    @WriteDataSource
    public void addCommission(Commission commission) {
        commissionDao.insert(commission);
    }
}
