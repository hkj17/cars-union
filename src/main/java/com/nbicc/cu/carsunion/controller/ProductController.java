package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Product;
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
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    //删除商品类别,包括它的子节点
    @RequestMapping(value = "deleteProductClass",method = RequestMethod.POST)
    public JSONObject deleteProductClass(@RequestParam(value = "id") String id,
                                         @RequestParam(value = "path",required = false) String path){
        productService.deleteProductClass(id,path);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    //获取商品品类列表
    @RequestMapping(value = "getProductClass",method = RequestMethod.POST)
    public JSONObject getProductClass(@RequestParam(value = "id", required = false) String id,
                                         @RequestParam(value = "path",required = false) String path){
        List<ProductClass> lists = productService.getProductClass(id,path);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
    }

    //添加商品
    @RequestMapping(value = "addProduct", method = RequestMethod.POST)
    public JSONObject addProduct(
            @RequestParam(value = "classId") String classId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "specification") String specification,
            @RequestParam(value = "feature") String feature){
        String result = productService.addProduct(classId,name,price,specification,feature);
        if("ok".equals(result)){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,result);
        }
    }

    //编辑商品
    @RequestMapping(value = "editProduct", method = RequestMethod.POST)
    public JSONObject editProduct(
            @RequestParam(value = "productId") String productId,
            @RequestParam(value = "classId") String classId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "specification") String specification,
            @RequestParam(value = "feature") String feature){
        String result = productService.editProduct(productId,classId,name,price,specification,feature);
        if("ok".equals(result)){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,result);
        }
    }

    //根据类别，获取商品
    @RequestMapping(value = "getProductByClassId", method = RequestMethod.POST)
    public JSONObject getProductByClassId(@RequestParam(value = "classId", required = false) String classId){
        List<Product> lists = productService.getProductByClassId(classId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
    }

    //根据id获取商品
    @RequestMapping(value = "getProductById", method = RequestMethod.POST)
    public JSONObject getProductByid(@RequestParam(value = "productId") String id){
        Product product = productService.getProductById(id);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,product);
    }

    //删除商品
    @RequestMapping(value = "deleteProduct",method = RequestMethod.POST)
    public JSONObject deleteProductClass(@RequestParam(value = "productId") String productId){
        productService.deleteProduct(productId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
    }
}
