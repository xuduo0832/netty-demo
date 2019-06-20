package com.example.nettysocketio.common;

import lombok.Data;

/**
 * @Author: xurs
 * @Date: 19-6-18 17:18
 * @Description:
 */
@Data
public class PushMessage {

    //登录用户编号
    private String userId;

    //推送内容
    private String content;
}
