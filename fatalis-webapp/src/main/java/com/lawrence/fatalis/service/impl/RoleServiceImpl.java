package com.lawrence.fatalis.service.impl;

import com.lawrence.fatalis.annotation.ReadDataSource;
import com.lawrence.fatalis.dao.FataRoleDao;
import com.lawrence.fatalis.model.FataRole;
import com.lawrence.fatalis.service.RoleService;
import com.lawrence.fatalis.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private FataRoleDao fataRoleDao;

    /**
     * 根据userId获取角色列表
     *
     * @param userId
     * @return
     */
    @Override
    @ReadDataSource
    public List<FataRole> queryRoleList(String userId) {
        if (StringUtil.isNull(userId)) {

            return null;
        }

        return fataRoleDao.queryRoleByUserId(userId);
    }

}
