package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.ProductClass;
import com.nbicc.cu.carsunion.service.ProductService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bigmao on 2017/8/18.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //增加商品类别
    //id,path,level都是传父节点的.
    @RequestMapping(value = "addProductClass",method = RequestMethod.POST)
    public JSONObject addProductClass(@RequestParam(value = "id", required = false) String pid,
                                      @RequestParam(value = "path",required = false) String path,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "level", required = false) Integer level){
        boolean state = productService.addProductClass(pid,path,name,level);
        if(state == true){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"wrong");
        }
    }

    //删除商品类别,包括它的子节点
    @RequestMapping(value = "deleteProductClass",method = RequestMethod.POST)
    public JSONObject deleteProductClass(@RequestParam(value = "id", required = false) String id,
                                         @RequestParam(value = "path",required = false) String path){
        boolean state = productService.deleteProductClass(id,path);
        if(state == true){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"wrong");
        }
    }

    //获取商品品类列表
    @RequestMapping(value = "getProductClass",method = RequestMethod.POST)
    public JSONObject getProductClass(@RequestParam(value = "id", required = false) String id,
                                         @RequestParam(value = "path",required = false) String path){
        List<ProductClass> lists = productService.getProductClass(id,path);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
    }

}
