package com.imzz.webchat.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imzz.webchat.entity.Mine;
import com.imzz.webchat.entity.Systemmenu;
import com.imzz.webchat.entity.Systemrole;
import com.imzz.webchat.service.MineService;
import com.imzz.webchat.service.SystemmenuService;
import com.imzz.webchat.service.SystemroleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author : 邹智敏
 * data : 2018年-03月-02日 13:58
 * Description :自定义shiro中的realm
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private MineService userBll;

    @Autowired
    private SystemroleService roleBll;

    @Autowired
    private SystemmenuService systemmenuService;

    @Autowired
    private StringRedisTemplate redisTemplate;


   /*** \_____________________________/
    * <p>@author       |   James</p>
    * <p>@param        |   token  </p>
    * <p>@return       |   org.apache.shiro.authc.AuthenticationInfo</P>
    * <p>@date         |   18-12-1 下午5:26</P>
    * <p>@description  |   验证当前登录的用户</p>
    ***/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取当前登入的用户名
        String userNumber = (String)token.getPrincipal();
        log.info("[开\t始\t认\t证\t了]：{}","[账\t户：\t"+userNumber+"]");
        Mine mine = new Mine();
        mine.setUsernumber(userNumber);
        //根据用户名查询此用户是否存在
        Mine user=userBll.getOne(new QueryWrapper<>(mine,"usernumber","userpassword"));
        if( null == user){
            return null;
        }
        AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(user.getUsernumber(),user.getUserpassword(), ByteSource.Util.bytes(userNumber),"ShiroRealm");
        return authcInfo;
    }

     /*** \_____________________________/
      * <p>@author       |   James</p>
      * <p>@param        |   principals  </p>
      * <p>@return       |   org.apache.shiro.authz.AuthorizationInfo</P>
      * <p>@date         |   2018/12/5 9:44</P>
      * <p>@description  |   为当限前登录的用户授予角色和权限</p>
      ***/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取当前登入的用户名
        String userNumber=(String)principals.getPrimaryPrincipal();
        log.info("[开\t始\t授\t权\t了]：{}","[账\t户：\t"+userNumber+"]");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //根据此用户名查询是否拥有   此角色
        authorizationInfo.setRoles(getRoles(userNumber,0));
        //根据用户名查询是否拥有   此权限
        authorizationInfo.setStringPermissions(getRoles(userNumber,1));
        return authorizationInfo;
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |     </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/12/5 9:44</P>
     * <p>@description  |   清除授权缓存</p>
     ***/
    public void clearAuthz(){
        //清空redis缓存
        redisTemplate.delete((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal());
        //清空资源授权缓存
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
        //推出登录
        SecurityUtils.getSubject().logout();
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   userName  </p>
     * <p>@param        |   type  </p>
     * <p>@return       |   java.util.Set<java.lang.String></P>
     * <p>@date         |   18-12-2 上午10:33</P>
     * <p>@description  |   添加说明</p>
     ***/
    private Set<String> getRoles(String userName, Integer type){
        List<Systemrole> role = roleBll.roleList(userName);
        if (role.size()==0){
            return  null;
        }
        Set<String> roles=new HashSet<>();
        for(Systemrole a:role) {
            //0 表示查询角色
            if(type == 0){
                log.info("[所\t属\t角\t色]：{}","["+a.getSystemroleName()+"]");
                roles.add(a.getSystemroleName());
            }else{
                List<Systemmenu> permission = systemmenuService.menuList(a.getSystemroleId());
                if (permission.size() == 0) {
                    return  null;
                }
                for(Systemmenu p:permission) {
                    log.info("[可\t访\t问\t接\t口]：{}","["+p.getSystemmenuAddress()+"]");
                    roles.add(p.getSystemmenuAddress());
                }
            }
        }
        return roles;
    }

}
