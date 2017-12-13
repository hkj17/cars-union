package com.nbicc.cu.carsunion.configuration;

import com.nbicc.cu.carsunion.model.ErrorInfo;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bigmao on 2017/8/25.
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    //以后可以细分异常，各个处理
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, Exception e) {
        logger.error("GlobalException : ERROR_MESSAGE: " + e.getMessage() + " --URL: " + req.getMethod() + " " + req.getRequestURL().toString() + " --ARGS: " + CommonUtil.getKeyValueForMap(req.getParameterMap()));
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setResult_code(ErrorInfo.ERROR);
        r.setMessage(e.getMessage());
        r.setUrl(req.getRequestURL().toString());
        r.setData("Exception!");
        return r;
    }

}
