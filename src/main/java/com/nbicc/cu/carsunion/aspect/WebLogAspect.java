package com.nbicc.cu.carsunion.aspect;


import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.nbicc.cu.carsunion.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        //影响响应速率，暂不记录
//        logger.info("RESPONSE : " + ret);
        StringBuilder sb = new StringBuilder();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        sb.append(request.getMethod()).append(" " + request.getRequestURL().toString()).append(" || ")
                .append("IP : " + request.getRemoteAddr()).append(" || ")
                .append("ARGS : " + CommonUtil.getKeyValueForMap(request.getParameterMap())).append(" || ")
                .append("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()) + "ms");

        logger.info(sb.toString());
    }

}
