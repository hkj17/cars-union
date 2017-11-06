package com.nbicc.cu.carsunion.interceptor;

import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.service.AdminService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by bigmao on 2017/9/15.
 */
@Component
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(AuthorityAnnotationInterceptor.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            HandlerMethod hm = (HandlerMethod) handler;

            Class<?> clazz = hm.getBeanType();
            Method m = hm.getMethod();
            try {
                if (clazz != null && m != null) {
                    boolean isClzAnnotation = clazz.isAnnotationPresent(Authority.class);
                    boolean isMethondAnnotation = m.isAnnotationPresent(Authority.class);
                    Authority authority = null;
                    // 如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
                    if (isMethondAnnotation) {
                        authority = m.getAnnotation(Authority.class);
                    } else if (isClzAnnotation) {
                        authority = clazz.getAnnotation(Authority.class);
                    }
                    if (authority != null) {
                        String token = request.getHeader("token");
                        ValueOperations valueOperations = redisTemplate.opsForValue();
                        String tokenValue = (String) valueOperations.get("token" + token);
                        if(!CommonUtil.isNullOrEmpty(tokenValue)){
                            redisTemplate.expire("token" + token, ParameterValues.TOKEN_EXPIRE_VALUE, TimeUnit.HOURS);
                        }
                        String userId = (String) valueOperations.get("token" + token);
                        Admin admin = adminService.getById(userId);
                        if(admin != null) {
                            hostHolder.setAdmin(admin);
                        }
                        if (AuthorityType.NoValidate == authority.value()) {
                            // 标记为不验证,放行
                            logger.info("---NoValidate---");
                            return true;
                        } else if (AuthorityType.NoAuthority == authority.value()) {
                            // 不验证权限，验证是否登录
                            logger.info("---NoAuthority---");
                            if(admin != null) {
                                return true;
                            }
                        } else if (AuthorityType.UserValidate == authority.value()){
                            // 验证用户权限
                            logger.info("---UserValidate---");
                            if(admin != null && admin.getAuthority() == 2) {
                                return true;
                            }
                        } else if (AuthorityType.MerchantValidate == authority.value()){
                            // 验证商家权限
                            logger.info("---MerchantValidate---");
                            if(admin != null && admin.getAuthority() == 1) {
                                return true;
                            }
                        } else if (AuthorityType.AdminValidate == authority.value()) {
                            //验证超级管理员权限
                            logger.info("---AdminValidate---");
                            if(admin != null && admin.getAuthority() == 0) {
                                return true;
                            }
                        }
                    }
                    // 未通过验证，返回提示json
                    PrintWriter pw = response.getWriter();
                    pw.write(CommonUtil.response(ResponseType.NOT_AUTHORIZED,"该用户没有权限",null).toString());
                    return false;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

}
