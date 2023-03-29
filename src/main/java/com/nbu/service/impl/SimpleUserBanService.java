package com.nbu.service.impl;

import com.nbu.mapper.BlackListMapper;
import com.nbu.mapper.UserInfoMapperForOpenGauss;
import com.nbu.pojo.BlackListItem;
import com.nbu.service.UserBanService;
import com.nbu.util.JsonUtil;
import com.nbu.util.RedisSession;
import com.nbu.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("SimpleUserBanService")
public class SimpleUserBanService implements UserBanService {

    private Logger logger = LoggerFactory.getLogger(SimpleUserBanService.class);

    @Autowired
    @Qualifier("BlackListMapper")
    private BlackListMapper blackListMapper;

    @Autowired
    @Qualifier("OpenGaussSimpleUserInfoMapper")
    private UserInfoMapperForOpenGauss userInfoMapperForOpenGauss;

    @Autowired
    private JsonUtil jsonUtil;
    private final Integer PAGE_OFFSET = 10;
    @Autowired
    private RedisSession redisSession;
    private Integer maxBufferPage = -1;
    @Override
    public Result GetBanUserListLimitTenPages(Integer startPage) {
        BlackListItem[] buffer = null;
        if (maxBufferPage < startPage) maxBufferPage = startPage;
             buffer =(BlackListItem[]) jsonUtil.JsonStringToObject
                ((String) redisSession.StringGet("black_list_page"+startPage),BlackListItem[].class);
        if ( buffer != null) {
            logger.info("黑名单查询，命中缓存");
            return new Result("查询黑名单结果",buffer,true);
        }
        buffer = blackListMapper.SelectBlackListLimit(startPage*PAGE_OFFSET,PAGE_OFFSET);
        redisSession.StringSet("black_list_page"+startPage,buffer);

        return new Result("查询黑名单结果",buffer,true);
    }

    @Override
    public Result BanUser(String username) {
        for (int page = 0;page <= maxBufferPage ;page++) {
            redisSession.StringDel("black_list_page"+page);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int page = 0;page <= maxBufferPage ;page++) {
            redisSession.StringDel("black_list_page"+page);
        }
        Integer banUserId = userInfoMapperForOpenGauss.GetUserInfoIdByUsername(username);
        BlackListItem blackListItem = new BlackListItem();
        blackListItem.setBanUserId(banUserId);
        blackListItem.setId(blackListMapper.GetTheLastId());
        if (blackListMapper.SelectBlackItemIdByUserId(banUserId) != null) return new Result("用户已经被加入黑名单了",false);
        if (blackListItem.getId() == null || blackListItem.getBanUserId() == null) {
            return new Result("插入参数不当",false);
        }
        blackListMapper.InsertBlackList(blackListItem);
        return new Result(username+"被成功封号",true);
    }

    @Override
    public Result ReleaseUser(String username) {
        for (int page = 0;page <= maxBufferPage ;page++) {
            redisSession.StringDel("black_list_page"+page);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int page = 0;page <= maxBufferPage ;page++) {
            redisSession.StringDel("black_list_page"+page);
        }
        Integer userId = userInfoMapperForOpenGauss.GetUserInfoIdByUsername(username);
        blackListMapper.DeleteBlackListItemByUserId(userId);
        return new Result(username+"被成功解封",true);
    }
}
