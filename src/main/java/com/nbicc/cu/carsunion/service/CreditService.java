package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.dao.CreditHistoryDao;
import com.nbicc.cu.carsunion.dao.UserCreditDao;
import com.nbicc.cu.carsunion.dao.UserSignHistoryDao;
import com.nbicc.cu.carsunion.dao.VipLevelDao;
import com.nbicc.cu.carsunion.model.CreditHistory;
import com.nbicc.cu.carsunion.model.UserCredit;
import com.nbicc.cu.carsunion.model.UserSignHistory;
import com.nbicc.cu.carsunion.model.VipLevel;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.nbicc.cu.carsunion.constant.ParameterValues.CONTINUITY_SIGN_DAYS;
import static com.nbicc.cu.carsunion.constant.ParameterValues.SIGN_COMMON_CREDIT;

@Service
public class CreditService {

    @Autowired
    private UserSignHistoryDao userSignHistoryDao;
    @Autowired
    private UserCreditDao userCreditDao;
    @Autowired
    private VipLevelDao vipLevelDao;
    @Autowired
    private CreditHistoryDao creditHistoryDao;


    /**
     * 用户签到，判断是否已签到，连续签到还是非连续签到
     *
     * @param userId
     * @return true/false
     */
    public boolean userSign(String userId) {
        //查询最新一条签到记录
        UserSignHistory userSignHistory = userSignHistoryDao.findFirstByUserIdOrderByDateDesc(userId);
        Date now = new Date();
        int addCredit;
        if (userSignHistory == null) {
            //第一次签到
            addCredit = addNewSignHIstory(userId, now);
        } else if (DateUtils.isSameDay(userSignHistory.getDate(), now)) {
            //当天已签到
            return false;
        } else if (DateUtils.isSameDay(userSignHistory.getDate(), DateUtils.addDays(now, -1))) {
            //连续签到
            addCredit = addContinuousSignHistory(userId, now, userSignHistory);
        } else {
            //非连续签到
            addCredit = addNewSignHIstory(userId, now);
        }
        //积分增加
        addUserCredit(userId, addCredit);
        return true;
    }

    /**
     * //增加用户积分
     *
     * @param userId
     * @param addCredit
     */
    private void addUserCredit(String userId, int addCredit) {
        UserCredit userCredit = userCreditDao.findOne(userId);
        if (userCredit == null) {
            userCredit = new UserCredit(userId, 0, 0, 0, 0);
            userCreditDao.save(userCredit);
        }
        userCredit.setSignCredit(userCredit.getSignCredit() + addCredit);
        userCredit.setTotalCredit(userCredit.getTotalCredit() + addCredit);
        userCreditDao.save(userCredit);
    }

    /**
     * 连续签到记录增加，依据上一条记录累计
     *
     * @param userId
     * @param now
     * @param userSignHistory
     * @return
     */
    private int addContinuousSignHistory(String userId, Date now, UserSignHistory userSignHistory) {
        UserSignHistory todaySign = new UserSignHistory();
        todaySign.setUserId(userId);
        todaySign.setDate(now);
        todaySign.setDays(userSignHistory.getDays() + 1);
        int addCredit = caluclateCredit(userSignHistory.getDays() + 1);
        todaySign.setAddCredit(addCredit);
        userSignHistoryDao.save(todaySign);
        return addCredit;
    }

    /**
     * 增加一条签到记录
     *
     * @param userId
     * @param now
     * @return addCredit
     */
    private int addNewSignHIstory(String userId, Date now) {
        UserSignHistory userSignHistory = new UserSignHistory();
        userSignHistory.setUserId(userId);
        userSignHistory.setDate(now);
        userSignHistory.setDays(1);
        int addCredit = SIGN_COMMON_CREDIT;
        userSignHistory.setAddCredit(addCredit);
        userSignHistoryDao.save(userSignHistory);
        return addCredit;
    }

    /**
     * 计算签到积分,积分规则在此修改
     *
     * @param days
     * @return credit
     */
    private int caluclateCredit(int days) {
        if (days >= CONTINUITY_SIGN_DAYS) {
            //连续签到X天，积分翻倍
            return 2 * SIGN_COMMON_CREDIT;
        } else {
            return SIGN_COMMON_CREDIT;
        }
    }

    public Page<UserSignHistory> signHistory(String userId, String start, String end,int pageNum, int pageSize) throws ParseException {
        String dateFormat = "yyyy-MM-dd";
        Date startDate = DateUtils.parseDate(start,dateFormat);
        Date endDate = DateUtils.addDays(DateUtils.parseDate(end,dateFormat),1);
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return userSignHistoryDao.findByUserIdAndDateBetweenOrderByDateDesc(userId,startDate,endDate,pageable);
    }

    /**
     * 用户积分总览和会员等级信息
     *
     * @param userId
     * @return JSONObject
     */
    public JSONObject overview(String userId) {
        JSONObject result = new JSONObject();
        UserCredit userCredit = userCreditDao.findOne(userId);
        result.put("credit", userCredit);
        VipLevel vipLevel = vipLevelDao.findVipLevelByRange(userCredit.getTotalCredit());
        result.put("vipLevel", vipLevel);
        return result;
    }

    public Page<CreditHistory> getUserCreditHistory(String userId, int source, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        if(source != -1) {
            return creditHistoryDao.findByUserIdAndSourceOrderByDateDesc(userId, source, pageable);
        }else{
            return creditHistoryDao.findByUserIdOrderByDateDesc(userId,pageable);
        }
    }
}
