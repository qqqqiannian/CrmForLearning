package com.demo.crm.workbench.service.impl;

import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.domain.User;
import com.demo.crm.utils.ServiceFactory;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.dao.ActivityRemarkDao;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {

        boolean flag = true;

        int count = activityDao.save(activity);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        if(count1!=count2){

            flag = false;

        }
        //删除市场活动

        int count3 = activityDao.delete(ids);
        if(count3!=ids.length){

            flag = false;

        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList =userDao.getUserList();

        //取activity
        Activity activity = activityDao.getById(id);

        //将uList和activity打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList", uList);
        map.put("activity",activity);

        //返回map

        return map;
    }
}
