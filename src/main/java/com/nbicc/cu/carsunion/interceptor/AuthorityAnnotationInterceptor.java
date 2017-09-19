package com.nbicc.cu.carsunion.interceptor;

import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
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
                        if (AuthorityType.NoValidate == authority.value()) {
                            // 标记为不验证,放行
                            logger.info("---NoValidate---");
                            return true;
                        } else if (AuthorityType.NoAuthority == authority.value()) {
                            // 不验证权限，验证是否登录
                            logger.info("---NoAuthority---");
                            String userId = (String) valueOperations.get("token" + token);
                            Admin admin = adminService.getById(userId);
                            if(admin != null) {
                                hostHolder.setAdmin(admin);
                                return true;
                            }
                        } else if (AuthorityType.UserValidate == authority.value()){
                            // 验证用户权限
                            logger.info("---UserValidate---");

                            String userId = (String) valueOperations.get("token" + token);
                            Admin admin = adminService.getById(userId);
                            if(admin != null && admin.getAuthority() == 2) {
                                hostHolder.setAdmin(admin);
                                return true;
                            }
                        } else if (AuthorityType.MerchantValidate == authority.value()){
                            // 验证商家权限
                            logger.info("---MerchantValidate---");

                            String userId = (String) valueOperations.get("token" + token);
                            Admin admin = adminService.getById(userId);
                            if(admin != null && admin.getAuthority() == 1) {
                                hostHolder.setAdmin(admin);
                                return true;
                            }
                        } else if (AuthorityType.AdminValidate == authority.value()) {
                            //验证超级管理员权限
                            logger.info("---AdminValidate---");
                            String userId = (String) valueOperations.get("token" + token);
                            Admin admin = adminService.getById(userId);
                            if(admin != null && admin.getAuthority() == 0) {
                                hostHolder.setAdmin(admin);
                                return true;
                            }
                        }
                    }
                    // 未通过验证，返回提示json
                    PrintWriter pw = response.getWriter();
                    pw.write(CommonUtil.response(ParameterKeys.NOT_AUTHORIZED,"error:NoAuthority!").toString());
                    return false;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

}
