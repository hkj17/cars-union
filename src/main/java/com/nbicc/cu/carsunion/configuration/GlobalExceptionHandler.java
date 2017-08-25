package com.nbicc.cu.carsunion.configuration;

import com.nbicc.cu.carsunion.model.ErrorInfo;
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

    //以后可以细分异常，各个处理
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, Exception e) {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setResult_code(ErrorInfo.ERROR);
        r.setMessage(e.getMessage());
        r.setUrl(req.getRequestURL().toString());
        r.setData("Exception!");
        return r;
    }
}
