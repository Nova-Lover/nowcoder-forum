package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.util.CommonUtil;
import com.nowcoder.community.util.ThreadLocalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 关注功能控制层
 * @author Alex
 * @version 1.0
 * @date 2022/2/13 21:03
 */
@Controller
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private ThreadLocalHolder<User> userThreadLocalHolder;

    @RequestMapping(path = "/toFollow",method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public String toFollow(int entityType,int entityId){
        User user = userThreadLocalHolder.getCache();
        followService.toFollow(user.getId(),entityType,entityId);
        return CommonUtil.getJsonString(0,"已关注");
    }

    @RequestMapping(path = "/unFollow",method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public String unFollow(int entityType,int entityId){
        User user = userThreadLocalHolder.getCache();
        followService.unFollow(user.getId(),entityType,entityId);
        return CommonUtil.getJsonString(0,"已取消关注");
    }
}
