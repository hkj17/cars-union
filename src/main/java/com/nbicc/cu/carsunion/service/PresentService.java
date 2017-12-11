package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.PresentDao;
import com.nbicc.cu.carsunion.dao.UserCreditDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.dao.UserPresentHistoryDao;
import com.nbicc.cu.carsunion.model.Present;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserCredit;
import com.nbicc.cu.carsunion.model.UserPresentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PresentService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PresentDao presentDao;
    @Autowired
    private UserCreditDao userCreditDao;
    @Autowired
    private UserPresentHistoryDao userPresentHistoryDao;

    @Transactional(rollbackFor = Exception.class)
    public boolean exchange(String userId, long presentId, int num, String address) {
        User user = userDao.findById(userId);
        Present present = presentDao.findById(presentId);
        if(user == null || present == null || present.getTotalQuantity() < num){
            return false;
        }
        int needCredit = num * present.getCreditValue();
        //检测积分是否足够
        UserCredit userCredit = userCreditDao.findByUserId(userId);
        if((userCredit.getTotalCredit()-userCredit.getUsedCredit()) < needCredit){
            return false;
        }

        //生成兑换记录，扣除积分,减库存
        UserPresentHistory userPresentHistory = new UserPresentHistory(user,present,num,
                needCredit,address,false,new Date());
        userPresentHistoryDao.save(userPresentHistory);
        userCredit.setUsedCredit(userCredit.getUsedCredit() + needCredit);
        userCreditDao.save(userCredit);
        int result = presentDao.updateStock(present.getTotalQuantity() - num,presentId,present.getTotalQuantity());
        System.out.println("-------result: " + result);
        if(result == 1){
            return true;
        }else{
            throw new RuntimeException("兑换失败，请重试！");
        }
    }

    public Page<UserPresentHistory> history(String userId, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return userPresentHistoryDao.findByUserId(userId,pageable);
    }

    public Page<UserPresentHistory> historyByAdmin(Long presentId, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        if(presentId.equals(-1L)){
            return userPresentHistoryDao.findAll(pageable);
        }else{
            return userPresentHistoryDao.findByPresentId(presentId,pageable);
        }
    }

    public boolean sendMark(long id) {
        UserPresentHistory userPresentHistory = userPresentHistoryDao.findOne(id);
        if(userPresentHistory == null){
            return false;
        }
        userPresentHistory.setSendMark(true);
        userPresentHistoryDao.save(userPresentHistory);
        return true;
    }
}
