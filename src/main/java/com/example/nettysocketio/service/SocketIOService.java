package com.example.nettysocketio.service;

import com.example.nettysocketio.common.PushMessage;

/**
 * @Author: xurs
 * @Date: 19-6-18 17:12
 * @Description:
 */
public interface SocketIOService {

    /**
     * @author: xurs
     * @date: 19-6-18
     * @Description: 推送服务器推送事件
     */
    public static final String PUSH_EVENT = "push_event";

    /**
     * @author: xurs
     * @date: 19-6-18
     * @Description: 客户端回复事件
     */
    public static final String REPLY_EVENT = "reply_event";


    /**
     * @author: xurs
     * @date: 19-6-18
     * @description:  启动推送服务
     */
    void start() throws Exception;

    /**
     * @author: xurs
     * @date: 19-6-18
     * @Description:  停止推送服务
     */
    void stop();

    /**
     * @author: xurs
     * @date: 19-6-18
     * @Description:  推送信息
     */
    void pushMessageToUser(PushMessage pushMessage);

    void pushMessageToAll(PushMessage pushMessage);
}
