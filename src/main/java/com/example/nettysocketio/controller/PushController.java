package com.example.nettysocketio.controller;

import com.example.nettysocketio.common.PushMessage;
import com.example.nettysocketio.service.SocketIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xurs
 * @Date: 19-6-19 10:55
 * @Description:
 */
@RestController
@RequestMapping("/push")
@Slf4j
public class PushController {

    @Autowired
    private SocketIOService socketIOService;

    @GetMapping("/hello")
    public String helloWorld(){
        return "helloWorld";
    }

    @GetMapping("/toUser")
    public void pushMessage(@RequestParam String userId){
        PushMessage pushMessage = new PushMessage();
        pushMessage.setUserId(userId);
        pushMessage.setContent("这是服务端推送的信息");
        log.info("pushMessage:{}",pushMessage);
        socketIOService.pushMessageToUser(pushMessage);
    }
}
