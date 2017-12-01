package com.lawrence.fatalis.service.impl;

import com.lawrence.fatalis.annotation.ReadDataSource;
import com.lawrence.fatalis.dao.FataUserDao;
import com.lawrence.fatalis.model.FataUser;
import com.lawrence.fatalis.service.UserService;
import com.lawrence.fatalis.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private FataUserDao fataUserDao;

    /**
     * 更新用户信息
     *
     * @param user
     * @return int
     */
    @Override
    public int updateUser(FataUser user) {
        if (user == null || StringUtil.isNull(user.getUserId())) {

            return -1;
        }

        return fataUserDao.updateByPrimaryKeySelective(user);
    }

    /**
     * 由手机号查询用户信息
     *
     * @param mobile
     * @return FataUser
     */
    @Override
    @ReadDataSource
    public FataUser getUserByMobile(String mobile) {
        if (StringUtil.isNull(mobile)) {

            return null;
        }

        List<FataUser> list = fataUserDao.selectUserByMobile(mobile);

        if (list != null && list.size() > 0) {

            return list.get(0);
        }

        return null;
    }
}
