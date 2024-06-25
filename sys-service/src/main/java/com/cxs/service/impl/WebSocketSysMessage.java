package com.cxs.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.cxs.base.UserSubject;
import com.cxs.service.UserService;
import com.cxs.vo.message.SysMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

//连接webSocket服务的URL
@ServerEndpoint("/bindingSysMessage/{userId}")
@Component("bindingSysMessage")
@Slf4j
public class WebSocketSysMessage {

    private Session session;
    private String userId;
    private static CopyOnWriteArraySet<WebSocketSysMessage> webSocketSysMessages = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<String, WebSocketSysMessage> webSocketMessageMap = new ConcurrentHashMap<>();

    /**
     * 建立连接
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMessageMap.put(userId, this);
        log.info("【新建系统消息连接】，连接总数:{}", webSocketMessageMap.size());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose(){
        webSocketMessageMap.remove(userId);
        log.info("【断开系统消息连接】，连接总数:{}", webSocketMessageMap.size());
    }

    /**
     * 接收到信息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        log.info("【收到系统消息】，客户端的信息:{}，连接总数:{}", message, webSocketMessageMap.size());
    }

    @OnError
    public void onError(Session session, Throwable t) {
        //什么都不想打印都去掉就好了
        log.info("【websocket消息】出现未知错误 ");
        //打印错误信息，如果你不想打印错误信息，去掉就好了
        //这里打印的也是  java.io.EOFException: null
        t.printStackTrace();
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String userId, SysMessageVO message){
        log.info("【广播发送】，信息:{}，总连接数:{}", message, webSocketMessageMap.size());
        try {
            if (StringUtils.hasLength(userId)) {
                sendMessageText(userId, message);
            }
        } catch (Exception e) {
            log.info("【广播发送】，信息异常:{}", e.fillInStackTrace());
        }
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessageText(String userId, SysMessageVO message){
        log.info("【广播发送】，信息:{}，总连接数:{}", message, webSocketMessageMap.size());
        try {
            WebSocketSysMessage w = webSocketMessageMap.get(userId);
            if (null != w) {
                w.session.getBasicRemote().sendText(JSON.toJSONString(message));
            }
        } catch (Exception e) {
            log.info("【广播发送】，信息异常:{}", e.fillInStackTrace());
        }
    }

}
