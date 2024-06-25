package com.cxs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
@ServerEndpoint("/bindingRecord/{token}")
@Component("bindingRecord")
@Slf4j
public class WebSocketPay {

    private Session session;
    private String token;
    private static CopyOnWriteArraySet<WebSocketPay> webSocketPays = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<String, WebSocketPay> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 建立连接
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        this.token = token;
        webSocketMap.put(token, this);
        log.info("【新建连接】，连接总数:{}", webSocketMap.size());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose(){
        webSocketMap.remove(token);
        log.info("【断开连接】，连接总数:{}", webSocketMap.size());
    }

    /**
     * 接收到信息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        log.info("【收到】，客户端的信息:{}，连接总数:{}", message, webSocketMap.size());
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
    public void sendMessage(String tokena, String message){
        log.info("【广播发送】，信息:{}，总连接数:{}", message, webSocketMap.size());
        try {
            if (tokena == null) {
                sendMessageText(message);
            } else {
                this.token = tokena;
                sendMessageText(message);
            }
        } catch (Exception e) {
            log.info("【广播发送】，信息异常:{}", e.fillInStackTrace());
        }
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessageText(String message){
        log.info("【广播发送】，信息:{}，总连接数:{}", message, webSocketMap.size());
        try {
            WebSocketPay w = webSocketMap.get(token);
            w.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.info("【广播发送】，信息异常:{}", e.fillInStackTrace());
        }
    }

}
