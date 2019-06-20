package com.example.nettysocketio.service.impl;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.nettysocketio.common.PushMessage;
import com.example.nettysocketio.service.SocketIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xurs
 * @Date: 19-6-18 17:30
 * @Description:
 */
@Slf4j
@Service
public class SocketIOServiceImpl implements SocketIOService {

    // 用来存已连接的客户端
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception  {
        stop();
    }


    @Override
    public void start() throws Exception {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String userId = getParamsByClient(client);
            if (userId != null) {
                clientMap.put(userId, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String loginUserNum = getParamsByClient(client);
            if (loginUserNum != null) {
                clientMap.remove(loginUserNum);
                client.disconnect();
            }
        });

        // 处理自定义的事件，与连接监听类似
        //客户端单对单回复事件
        socketIOServer.addEventListener(REPLY_EVENT, PushMessage.class, (client, data, ackSender) -> {
            log.info("data:{}",JSON.toJSON(data));
            pushMessageToUser(data);
        });

        //(循环一个一个推送)
        socketIOServer.addEventListener("chatevent", PushMessage.class,(client, data, ackSender) -> {
            log.info("广播：{}",data);
            pushMessageToAll(data);
//            socketIOServer.getBroadcastOperations().sendEvent("room", data);
        });

        socketIOServer.start();
        log.info("socket.io启动成功！");
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }

    }

    @Override
    public void pushMessageToUser(PushMessage pushMessage) {
        String userId = pushMessage.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            SocketIOClient client = clientMap.get(userId);
            if (client != null){
                client.sendEvent(PUSH_EVENT, pushMessage);
            }
        }

    }

    /**
     * @author: xurs
     * @date: 19-6-19
     * @Description:  推送全体用户(排除pushMessage中的用户)
     */
    @Override
    public void pushMessageToAll(PushMessage pushMessage){
        String userId = pushMessage.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            SocketIOClient client = clientMap.get(userId);
            for (String clientId:clientMap.keySet()){
                if(clientId.equals(userId)){
                    continue;
                }
                SocketIOClient otherClient = clientMap.get(clientId);
                otherClient.sendEvent(PUSH_EVENT, pushMessage);
            }
        }
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private String getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("userId");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
