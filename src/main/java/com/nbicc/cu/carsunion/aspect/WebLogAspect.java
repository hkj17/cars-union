package com.nbicc.cu.carsunion.aspect;


import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bigmao on 2017/8/25.
 * 利用AOP统一处理Web请求日志
 */
@Aspect
@Component
public class WebLogAspect {

    private static Logger logger = LogManager.getLogger(WebLogAspect.class);

    @Autowired
    private HostHolder hostHolder;

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.nbicc.cu.carsunion.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        StringBuilder sb = new StringBuilder();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        sb.append(request.getMethod()).append(" " + request.getRequestURL().toString()).append(" || ")
                .append("IP : " + request.getRemoteAddr()).append(" || ")
                .append("user : " + getUserId()).append(" || ")
                .append("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()) + "ms").append(" || ")
                .append("ARGS : " + CommonUtil.getKeyValueForMap(request.getParameterMap()));
//                .append("Response : " + ret);
        logger.info(sb.toString());
    }

    private String getUserId() {
        Admin admin = hostHolder.getAdmin();
        if(admin == null){
            return "unknow user";
        }else{
            return admin.getId();
        }
    }

}
