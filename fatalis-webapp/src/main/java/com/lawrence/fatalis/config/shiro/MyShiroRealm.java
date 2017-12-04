package com.lawrence.fatalis.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.fatalis.constant.WebContant;
import com.lawrence.fatalis.model.FataAuthority;
import com.lawrence.fatalis.model.FataRole;
import com.lawrence.fatalis.model.FataUser;
import com.lawrence.fatalis.redis.ClusterOperator;
import com.lawrence.fatalis.redis.RedisOperator;
import com.lawrence.fatalis.service.AuthService;
import com.lawrence.fatalis.service.RoleService;
import com.lawrence.fatalis.service.UserService;
import com.lawrence.fatalis.util.DateUtil;
import com.lawrence.fatalis.util.LogUtil;
import com.lawrence.fatalis.util.StringUtil;
import com.lawrence.fatalis.util.encrypt.MD5Coder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.*;

/**
 * 自定义realm
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private AuthService authService;

    @Resource
    private RedisOperator operator;
    /*@Resource
    private ClusterOperator operator;*/


    private String LOGINCOUNT = "loginCount_";// 登陆次数前缀
    private String ISLOCK = "isLock_";// 是否锁定前缀

    /**
     * 验证用户身份
     *
     * @param authcToken
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @SuppressWarnings("unchecked")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {

        // 获取登陆用户名和密码
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());

        // 登陆次数自增
        long num = operator.leftPushList(LOGINCOUNT + "." + username, Math.random() + "");

        // 登陆不通过超过5次, 锁定用户账户一小时
        if (num > 5) {
            operator.set(ISLOCK + "." + username, "true", 1800);
        }

        //
        if ("true".equals(operator.get(ISLOCK + "." + username))) {
            throw new DisabledAccountException("超过5次登陆验证失败, 账户将锁定1小时");
        }

        // 由username(手机号)获取用户, 校验密码
        FataUser user = userService.getUserByMobile(username);

        // 未查到手机号, 则未注册
        if (null == user) {
            throw new UnknownAccountException("当前用户名/手机号未注册");

        // 查到手机号, 校验密码
        } else if (StringUtil.isNotNull(user.getUserId())) {
            String code = user.getPassword();
            String salt = user.getSalt();

            boolean pass = MD5Coder.verify(password, code, salt);

            // 校验用户名/密码
            if (pass) {

                // 更新updateTime, 用户最后登陆时间
                FataUser updUser = new FataUser();
                updUser.setUserId(user.getUserId());
                updUser.setUpdateTime(DateUtil.getCurrentDateString("yyyy-MM-dd HH:mm:ss"));
                int i = userService.updateUser(updUser);
                if (i == -1) {
                    throw new AccountException("用户登陆更新失败");
                }

                // 清空登录计数
                operator.del(LOGINCOUNT + "." + username);
                operator.del(ISLOCK + "." + username);

            } else {
                throw new CredentialsException("密码错误, 请重试");
            }

        // 其他状态, 拦截
        } else {
            throw new AuthenticationException("身份验证失败");
        }

        LogUtil.info(getClass(), "验证通过, 用户: " + user.toString() + ", 成功登陆");

        return new SimpleAuthenticationInfo(user, password, getName());
    }

    /**
     * 进行用户授权
     *
     * @param principals
     * @return AuthenticationInfo
     */
    @SuppressWarnings("unchecked")
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        FataUser user = (FataUser) SecurityUtils.getSubject().getPrincipal();
        String userId = user.getUserId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        List<Set<String>> roleAuthList = (List<Set<String>>) SecurityUtils.getSubject().getSession().getAttribute(WebContant.SHIRO_ROLE_AUTH);
        if (roleAuthList != null && roleAuthList.size() == 2) {
            info.setRoles(roleAuthList.get(0));// 缓存角色
            info.setStringPermissions(roleAuthList.get(1));// 缓存权限
        } else {

            // 根据userId查询角色, 放入AuthorizationInfo
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", userId);
            List<FataRole> roleList = roleService.queryRoleList(userId);
            Set<String> roleSet = new HashSet<>();
            for(FataRole role : roleList){
                roleSet.add(role.getRoleCode());

                LogUtil.info(getClass(), "用户: " + user.getUserName() + ", 角色: " + role.getRoleCode());
            }
            info.setRoles(roleSet);

            // 根据userId查询权限, 放入AuthorizationInfo
            List<FataAuthority> authList = authService.queryAuthList(userId);
            Set<String> authSet = new HashSet<>();
            for(FataAuthority auth : authList){
                String branchUrl = auth.getBranchUrl();
                JSONObject json = JSONObject.parseObject(branchUrl);
                Set<Map.Entry<String, Object>> set = json.entrySet();
                for (Map.Entry entry : set) {
                    authSet.add(auth.getMainUrl() + entry.getKey());

                    LogUtil.info(getClass(), "用户: " + user.getUserName() + ", 权限: " + auth.getMainUrl() + entry.getKey());
                }
            }

            info.setStringPermissions(authSet);

            SecurityUtils.getSubject().getSession().setAttribute(WebContant.SHIRO_ROLE_AUTH, new ArrayList<Set<String>>(Arrays.asList(roleSet, authSet)));
        }

        return info;
    }

}
