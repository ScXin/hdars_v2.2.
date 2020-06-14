package com.hlsii.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
//import org.crazycake.shiro.RedisSessionDAO;
import org.immutables.value.Value;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//配置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);


        shiroFilterFactoryBean.setLoginUrl("/pub/needLogin");

        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/notPermit");
//设置自定义filter


        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("roleOrFilter", new CustomRolesOrAuthorizationFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//配置拦截器链
//        filterChainDefinitionMap.put("/hdars/user/logout","logout");


        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");

        filterChainDefinitionMap.put("/hdars/api/**", "authc");
//        filterChainDefinitionMap.put("/hdars/download/**", "authc");
        filterChainDefinitionMap.put("/hdars/mgmt/**", "roleOrFilter[admin]");
//    filterChainDefinitionMap.put("/**", re'di's'M"authc");

//        filterChainDefinitionMap.put("/**", "authc");
//        filterChainDefinitionMap.put("/hdars/user/**","roleOrFilter[admin,user]");
//        filterChainDefinitionMap.put("/hdars/userLog/**","roleOrFilter[admin]");
//        filterChainDefinitionMap.put("/hdars/ip/**","roleOrFilter[admin]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();


        //设置realm（推荐放到最后，不然某些情况会不生效）
        securityManager.setRealm(customRealm());

        //如果不是前后端分离，则不必设置下面的sessionManager
        securityManager.setSessionManager(sessionManager());

        //使用自定义的cacheManager
        securityManager.setCacheManager(cacheManager());



        return securityManager;
    }


    /**
     * 自定义realm
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();

        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }


    /**
     * 密码加解密规则
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();

        //设置散列算法：这里使用的MD5算法
        credentialsMatcher.setHashAlgorithmName("md5");

        //散列次数，好比散列2次，相当于md5(md5(xxxx))
        credentialsMatcher.setHashIterations(2);

        credentialsMatcher.setHashSalted(true);

        return credentialsMatcher;
    }


    //自定义sessionManager
//    @Bean
//    public SessionManager sessionManager() {
//
//        CustomSessionManager customSessionManager = new CustomSessionManager();
//
//
//        //超时时间，默认 30分钟，会话超时；方法里面的单位是毫秒
//        customSessionManager.setGlobalSessionTimeout(1800000);
//
//        customSessionManager.setDeleteInvalidSessions(true);
//
//        customSessionManager.setSessionDAO(redisSessionDAO());
//        customSessionManager.setSessionValidationSchedulerEnabled(true);
//        customSessionManager.setDeleteInvalidSessions(true);
////        customSessionManager.setSessionIdCookie();
////        配置session持久化
//        customSessionManager.setSessionDAO(redisSessionDAO());
//        customSessionManager.setSessionIdCookie(getSessionIdCookie());
//
//        return customSessionManager;
//    }
    @Bean
    public SessionManager sessionManager() {

        MySessionManager customSessionManager = new MySessionManager();

        //超时时间，默认 30分钟，会话超时；方法里面的单位是毫秒
        customSessionManager.setGlobalSessionTimeout(1800000);

        customSessionManager.setDeleteInvalidSessions(true);

        //将重写的RedisSessionDao 接入到shiro 中的sessionManager
        customSessionManager.setSessionDAO(redisSessionDAO());
        customSessionManager.setSessionValidationSchedulerEnabled(true);
        customSessionManager.setDeleteInvalidSessions(true);
        //customSessionManager.setSessionIdCookie();

        //配置session持久化
        customSessionManager.setSessionDAO(redisSessionDAO());
//
//        //设置sessionid名字生效
//        customSessionManager.setSessionIdCookie(getSessionIdCookie());

        return customSessionManager;
    }

//
//    @Bean(name="sessionIdCookie")
//    public SimpleCookie getSessionIdCookie(){
//        SimpleCookie simpleCookie = new SimpleCookie(jessionId);
//        return simpleCookie;
//    }

    /**
     * 配置redisManager
     */
    public RedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("192.168.113.36");

        redisManager.setPort(6379);
        return redisManager;
    }

//Failed to read artifact descriptor for org.glassfish:javax.el:jar:3.0.1-b06-SNAPSHOT

    /**
     * 配置具体cache实现类
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());

        //设置过期时间，单位是秒，20s
        redisCacheManager.setExpire(1800);

        return redisCacheManager;
    }


    @Bean
    public SimpleCookie getSessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("nsrlSession");
        return simpleCookie;
    }


    /**
     * 自定义session持久化
     *
     * @return
     */
    @Bean
    public RedisSessionDao redisSessionDAO() {

        RedisSessionDao redisSessionDao = new RedisSessionDao();
        return redisSessionDao;
    }


    /**
     * 管理shiro一些bean的生命周期 即bean初始化 与销毁
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * api controller 层面
     * 加入注解的使用，不加入这个AOP注解不生效(shiro的注解 例如 @RequiresGuest)
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 用来扫描上下文寻找所有的Advistor(通知器),
     * 将符合条件的Advisor应用到切入点的Bean中，需要在LifecycleBeanPostProcessor创建后才可以创建
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }


}
