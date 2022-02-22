package com.nowcoder.community.config.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.constant.MessageConstant;
import com.nowcoder.community.constant.SystemConstant;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.es.ElasticsearchService;
import com.nowcoder.community.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件消费者:
 *      把事件对象转换成消息插入到数据库
 * @author Alex
 * @version 1.0
 * @date 2022/2/15 12:40
 */
@Component
@Slf4j
public class EventConsumer implements MessageConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    /**
     * 一个方法可以消费kafka多个topic，一个topic可以被多个方法消费
     * @param record
     */
    @KafkaListener(topics = {TOPIC_FOLLOW,TOPIC_COMMENT,TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord record){
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(!checkRecord(record,event)){
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(SystemConstant.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        message.setStatus(0);

        Map<String,Object> map = new HashMap<>();
        map.put("userId",event.getUserId());
        map.put("entityType",event.getEntityType());
        map.put("entityId",event.getEntityId());
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String,Object> entry:event.getData().entrySet()) {
                map.put(entry.getKey(),entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(map));
        messageService.addMessage(message);
    }

    /**
     * 监听发帖事件
     * @param record
     */
    @KafkaListener(topics = {MessageConstant.TOPIC_PUBLISH})
    public void handlePublishEvent(ConsumerRecord record){
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(!checkRecord(record,event)){
            return;
        }

        DiscussPost discussPost = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(discussPost);
    }

    @KafkaListener(topics = {MessageConstant.TOPIC_DELETE})
    public void handleDeleteEvent(ConsumerRecord record){
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(!checkRecord(record,event)){
            return;
        }
        elasticsearchService.deleteDiscusspost(event.getEntityId());
    }

    private boolean checkRecord(ConsumerRecord record,Event event){
        if(CommonUtil.isEmtpy(record) || CommonUtil.isEmtpy(record.value())){
            log.error("消息内容为空.");
            return false;
        }

        if(CommonUtil.isEmtpy(event)){
            log.error("消息格式错误");
            return false;
        }
        return true;
    }




}
