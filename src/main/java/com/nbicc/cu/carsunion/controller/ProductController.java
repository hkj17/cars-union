package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.ProductClass;
import com.nbicc.cu.carsunion.model.ProductTag;
import com.nbicc.cu.carsunion.model.VehicleProductRelationship;
import com.nbicc.cu.carsunion.service.ProductService;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    HostHolder hostHolder;

    //增加商品类别
    //id,path,level都是传父节点的.
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addProductClass", method = RequestMethod.POST)
    public JSONObject addProductClass(@RequestParam(value = "id", required = false) String pid,
                                      @RequestParam(value = "path", required = false) String path,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "level", required = false) Integer level) {
        productService.addProductClass(pid, path, name, level);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    //删除商品类别,包括它的子节点
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteProductClass", method = RequestMethod.POST)
    public JSONObject deleteProductClass(@RequestParam(value = "id") String id,
                                         @RequestParam(value = "path", required = false) String path) {
        productService.deleteProductClass(id, path);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    //获取商品品类列表
    @RequestMapping(value = "getProductClass", method = RequestMethod.POST)
    public JSONObject getProductClass(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "path", required = false) String path) {
        List<ProductClass> lists = productService.getProductClass(id, path);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",lists);
    }

    //查询商品分类byid
    @RequestMapping(value = "getProductClassById", method = RequestMethod.POST)
    public JSONObject getProductClassById(@RequestParam(value = "id") String id) {
        List<ProductClass> lists = productService.getProductClassById(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",lists);
    }

    //添加商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addProduct", method = RequestMethod.POST)
    public JSONObject addProduct(
            @RequestParam(value = "classId") String classId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "simpleName") String simpleName,
            @RequestParam(value = "promotion") String promotion,
            @RequestParam(value = "photos") String photos,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "specification") String specification,
            @RequestParam(value = "feature") String feature,
            @RequestParam(value = "vehicles", required = false) String vehicles,
            @RequestParam(value = "groupMark", required = false) String groupMark) {
        String result = productService.addProduct(classId, name, simpleName, promotion,photos, price, specification, feature, vehicles, groupMark);
        if ("ok".equals(result)) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, result,null);
        }
    }

    //编辑商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "editProduct", method = RequestMethod.POST)
    public JSONObject editProduct(
            @RequestParam(value = "productId") String productId,
            @RequestParam(value = "classId") String classId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "simpleName") String simpleName,
            @RequestParam(value = "promotion") String promotion,
            @RequestParam(value = "photos") String photos,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "specification") String specification,
            @RequestParam(value = "feature") String feature,
            @RequestParam(value = "groupMark", required = false) String groupMark) {
        String result = productService.editProduct(productId, classId, name,simpleName, promotion, photos, price, specification, feature, groupMark);
        if ("ok".equals(result)) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, result,null);
        }
    }

    //根据车型或商品类别以及关键字查询获取商品，带分页
    @RequestMapping(value = "getProductByClassAndVehicle", method = RequestMethod.POST)
    public JSONObject getProductByClassAndVehicle(@RequestParam(value = "vehicleId") String vehicleId,
                                                  @RequestParam(value = "classId") String classId,
                                                  @RequestParam(value = "searchStr",defaultValue = "") String searchStr,
                                                  @RequestParam(value = "onSale", defaultValue = "0") int onSale,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<Product> lists = productService.getProductByClassIdAndVehicleIdWithPage(classId, vehicleId, searchStr, onSale,
                pageNum - 1, pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功", lists);
    }


    //根据id获取商品
    @Authority
    @RequestMapping(value = "getProductById", method = RequestMethod.POST)
    public JSONObject getProductByid(@RequestParam(value = "productId") String id) {
        Product product = productService.getProductById(id);
        if(hostHolder.getAdmin() != null && hostHolder.getAdmin().getAuthority() == 2) {
            //用户浏览记录跟踪
            String userId = hostHolder.getAdmin().getId();
            userService.addToBrowseHistory(userId, id);
        }
        if (product == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"商品不存在",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",product);
        }
    }

    //删除商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteProduct", method = RequestMethod.POST)
    public JSONObject deleteProductClass(@RequestParam(value = "productId") String productId) {
        productService.deleteProduct(productId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    //商品下架/上架
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "productOnSale", method = RequestMethod.POST)
    public JSONObject productOnSale(@RequestParam(value = "productId") String id,
                                    @RequestParam(value = "state") String state) {
        productService.setProductOnSale(id, state);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    //查看商品适用车型
    @RequestMapping(value = "getVehicleRelationship", method = RequestMethod.POST)
    public JSONObject getVehicleRelationship(@RequestParam(value = "productId") String productId) {
        List<VehicleProductRelationship> list = productService.getVehicleRelationship(productId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",list);
    }

    //批量添加商品适用车型
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addVehicleRelationship", method = RequestMethod.POST)
    public JSONObject addVehicleRelationship(@RequestBody JSONObject json) {
        String productId = json.getString("productId");
        String result;
        List<String> vehicles = json.getObject("vehicles", List.class);
        try {
            result = productService.addVehicleRelationshipBatch(productId, vehicles);
        } catch (RuntimeException e) {
            result = "add wrong";
            logger.error("addVehicleRelationship Exception! ");
        }
        if ("ok".equals(result)) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, result,null);
        }
    }

    //批量删除商品适用车型
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteVehicleRelationship", method = RequestMethod.POST)
    public JSONObject deleteVehicleRelationship(@RequestBody JSONObject json) {
        String productId = json.getString("productId");
        String result;
        List<String> vehicles = json.getObject("vehicles", List.class);
        try {
            result = productService.deleteVehicleRelationshipBatch(productId, vehicles);
        } catch (RuntimeException e) {
            result = "delete wrong";
            logger.error("deleteVehicleRelationship Exception! ");
        }
        if ("ok".equals(result)) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, result,null);
        }
    }

    //添加商品标签
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addProductTag", method = RequestMethod.POST)
    public JSONObject addProductTag(@RequestParam("tagName") String tagName,
                                    @RequestParam("productClassId")String productClassId) {
        String result = productService.addProductTag(tagName,productClassId);
        if("ok".equals(result)){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,result,null);
        }
    }

    //根据商品类别查询商品标签
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "getProductTagByProductClassId", method = RequestMethod.POST)
    public JSONObject getProductTagByProductClassId(@RequestParam("productClassId")String productClassId) {
        List<ProductTag> result = productService.getProductTagByProductClassId(productClassId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "ok",result);
    }

    //根据id查询商品标签
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "getProductTagById", method = RequestMethod.POST)
    public JSONObject getProductTagById(@RequestParam("id")String id) {
        ProductTag productTag = productService.getProductTagById(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "ok",productTag);
    }

    //编辑商品标签
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "editProductTag", method = RequestMethod.POST)
    public JSONObject editProductTag(@RequestParam("productTagId") String productTagId,
                                     @RequestParam("tagName") String tagName,
                                     @RequestParam("productClassId")String productClassId) {
        String result = productService.editProductTag(productTagId,tagName,productClassId);
        if("ok".equals(result)){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,result,null);
        }
    }

    //删除商品标签
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteProductTag", method = RequestMethod.POST)
    public JSONObject deleteProductTag(@RequestParam("productTagId") String productTagId) {
        String result = productService.deleteProductTag(productTagId);
        if("ok".equals(result)){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,result,null);
        }
    }

}
