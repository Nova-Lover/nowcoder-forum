package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.config.event.EventProducer;
import com.nowcoder.community.constant.CommentEntityConstant;
import com.nowcoder.community.constant.MessageConstant;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.ThreadLocalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * 评论管理控制层
 * @author Alex
 * @version 1.0
 * @date 2022/2/8 18:04
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ThreadLocalHolder<User> userThreadLocalHolder;

    @LoginRequired
    @RequestMapping(path = "/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(userThreadLocalHolder.getCache().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);


        // 触发评论事件
        Event event = new Event().setTopic(MessageConstant.TOPIC_COMMENT)
                .setUserId(userThreadLocalHolder.getCache().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);

        if (comment.getEntityType() == CommentEntityConstant.ENTITY_TYPE_POST.getType()) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType() == CommentEntityConstant.ENTITY_TYPE_COMMENT.getType()){
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        // 异步发送消息至kafka topic
        eventProducer.handleEvent(event);
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
