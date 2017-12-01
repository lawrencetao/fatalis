package com.lawrence.fatalis.service.impl;

import com.lawrence.fatalis.annotation.ReadDataSource;
import com.lawrence.fatalis.dao.FataAuthorityDao;
import com.lawrence.fatalis.model.FataAuthority;
import com.lawrence.fatalis.service.AuthService;
import com.lawrence.fatalis.util.StringUtil;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private FataAuthorityDao fataAuthorityDao;

    /**
     * 根据userId获取权限列表
     *
     * @param userId
     * @return List<FataAuthority>
     */
    @Override
    @ReadDataSource
    public List<FataAuthority> queryAuthList(String userId) {
        if (StringUtil.isNull(userId)) {

            return null;
        }

        return fataAuthorityDao.queryAuthByUserId(userId);
    }

    /**
     * 获取所有权限
     *
     * @return List<FataAuthority>
     */
    @Override
    @ReadDataSource
    public List<FataAuthority> queryAllAuth() {

        return fataAuthorityDao.queryAllAuth();
    }
}
