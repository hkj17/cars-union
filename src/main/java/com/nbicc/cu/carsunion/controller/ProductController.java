package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.ProductClass;
import com.nbicc.cu.carsunion.model.VehicleProductRelationship;
import com.nbicc.cu.carsunion.service.ProductService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bigmao on 2017/8/18.
 */
@RestController
@RequestMapping("/product")
@Authority
public class ProductController {

    @Autowired
    private ProductService productService;

    //增加商品类别
    //id,path,level都是传父节点的.
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addProductClass",method = RequestMethod.POST)
    public JSONObject addProductClass(@RequestParam(value = "id", required = false) String pid,
                                      @RequestParam(value = "path",required = false) String path,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "level", required = false) Integer level){
        boolean state = productService.addProductClass(pid,path,name,level);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    //删除商品类别,包括它的子节点
    @Authority(value = AuthorityType.AdminValidate)
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

    //查询商品分类byid
    @RequestMapping(value = "getProductClassById",method = RequestMethod.POST)
    public JSONObject getProductClassById(@RequestParam(value = "id") String id){
        List<ProductClass> lists = productService.getProductClassById(id);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
    }

    //添加商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addProduct", method = RequestMethod.POST)
    public JSONObject addProduct(
            @RequestParam(value = "classId") String classId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "specification") String specification,
            @RequestParam(value = "feature") String feature,
            @RequestParam(value = "vehicles",required = false)String vehicles){
        String result = productService.addProduct(classId,name,price,specification,feature,vehicles);
        if("ok".equals(result)){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,result);
        }
    }

    //编辑商品
    @Authority(value = AuthorityType.AdminValidate)
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
//    @RequestMapping(value = "getProductByClassId", method = RequestMethod.POST)
//    public JSONObject getProductByClassId(@RequestParam(value = "classId", required = false) String classId,
//                                          @RequestParam(value = "pageNum", defaultValue = "1") String pageNum,
//                                          @RequestParam(value = "pageSize", defaultValue = "10") String pageSize){
//        Page<Product> lists = productService.getProductByClassIdWithPage(classId, Integer.parseInt(pageNum)-1,Integer.parseInt(pageSize));
//        if(lists != null){
//            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
//        }else {
//            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "can not find this productClass!");
//        }
//    }

    //根据车型，获取商品
//    @RequestMapping(value = "getProductByVehicleId", method = RequestMethod.POST)
//    public JSONObject getProductByVehicleId(@RequestParam(value = "vehicleId", required = false) String vehicleId){
//        List<VehicleProductRelationship> list = productService.getProductByVehicleId(vehicleId);
//        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,list);
//    }

    //根据车型或商品类别获取商品，带分页
    @RequestMapping(value = "getProductByClassAndVehicle",method = RequestMethod.POST)
    public JSONObject getProductByClassAndVehicle(@RequestParam(value = "vehicleId") String vehicleId,
                                                  @RequestParam(value = "classId") String classId,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") String pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") String pageSize){
        Page<Product> lists = productService.getProductByClassIdAndVehicleIdWithPage(classId,vehicleId,Integer.parseInt(pageNum)-1,Integer.parseInt(pageSize));
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
    }


    //根据id获取商品
    @RequestMapping(value = "getProductById", method = RequestMethod.POST)
    public JSONObject getProductByid(@RequestParam(value = "productId") String id){
        Product product = productService.getProductById(id);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,product);
    }

    //删除商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteProduct",method = RequestMethod.POST)
    public JSONObject deleteProductClass(@RequestParam(value = "productId") String productId){
        productService.deleteProduct(productId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
    }

    //商品下架/上架
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "productOnSale", method = RequestMethod.POST)
    public JSONObject productOnSale(@RequestParam(value = "productId") String id,
                                    @RequestParam(value = "state")String state){
        productService.setProductOnSale(id,state);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    //查看商品适用车型
    @RequestMapping(value = "getVehicleRelationship",method = RequestMethod.POST)
    public JSONObject getVehicleRelationship(@RequestParam(value = "productId")String productId){
        List<VehicleProductRelationship> list = productService.getVehicleRelationship(productId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, list);
    }

    //批量添加商品适用车型
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addVehicleRelationship",method = RequestMethod.POST)
    public JSONObject addVehicleRelationship(@RequestBody JSONObject json){
        String productId = json.getString("productId");
        String result;
        List<String> vehicles = json.getObject("vehicles",List.class);
        try {
            result = productService.addVehicleRelationshipBatch(productId, vehicles);
        }catch (RuntimeException e){
            result = "add wrong";
        }
        if("ok".equals(result)) {
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, result);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, result);
        }
    }

    //批量删除商品适用车型
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteVehicleRelationship",method = RequestMethod.POST)
    public JSONObject deleteVehicleRelationship(@RequestBody JSONObject json){
        String productId = json.getString("productId");
        String result;
        List<String> vehicles = json.getObject("vehicles",List.class);
        try {
            result = productService.deleteVehicleRelationshipBatch(productId, vehicles);
        }catch (RuntimeException e){
            result = "delete wrong";
        }
        if("ok".equals(result)) {
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, result);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, result);
        }
    }
}
