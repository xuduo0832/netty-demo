package com.example.nettysocketio.service.impl;

import com.example.nettysocketio.common.PushMessage;
import com.example.nettysocketio.service.SocketIOService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SocketIOServiceImplTest {

    @Autowired
    private SocketIOService socketIOService;

    @Test
    public void pushMessageToUser() {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setUserId("88");
        pushMessage.setContent("这是服务端推送的消息");
        socketIOService.pushMessageToUser(pushMessage);
    }
}