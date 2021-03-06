package com.nbicc.cu.carsunion.configuration;

import com.nbicc.cu.carsunion.interceptor.AuthorityAnnotationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by bigmao on 2017/8/18.
 */
@Component
public class CarUnionConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    AuthorityAnnotationInterceptor authorityAnnotationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authorityAnnotationInterceptor);
        registry.addInterceptor(authorityAnnotationInterceptor)
                .addPathPatterns("/merchant/*")
                .addPathPatterns("/vehicle/*")
                .addPathPatterns("/admin/*")
                .addPathPatterns("/order/*")
                .addPathPatterns("/product/*")
                .addPathPatterns("/user/*")
                .addPathPatterns("/advertise/*")
                .addPathPatterns("/control/*")
                .addPathPatterns("/credit/*")
                .addPathPatterns("/present/*");
        super.addInterceptors(registry);
    }

}
