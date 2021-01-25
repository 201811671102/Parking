package com.cs.parking.handler;

import com.cs.parking.base.utils.RedisUtil;
import com.cs.parking.manager.ConnManager;
import com.cs.parking.base.DTO.Packet;
import com.cs.parking.code.Protocol;
import com.cs.parking.base.utils.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @date 2020/5/21
 * @author wangpeng1@huya.com
 * @description websocket协议处理器
 *
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebsocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    //解决@Autowired为null
    private static WebsocketHandler websocketHandler;
    @PostConstruct
    public void init(){
        websocketHandler = this;
    }
    @Autowired
    RedisUtil redisUtil;

    // 业务线程池
    private static final ExecutorService bizThreadPool =
            new ThreadPoolExecutor(10, 512, 60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(20000));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            handleWSClose(ctx);
        } else if (msg instanceof CloseWebSocketFrame) {
            handleWSClose(ctx);
        } else if(msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame)msg;
            handleWSTextFrame(ctx,textWebSocketFrame);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state() == IdleState.READER_IDLE) {
            handleWSClose(ctx);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    protected void handleWSTextFrame(ChannelHandlerContext ctx, TextWebSocketFrame data) throws IOException, ClassNotFoundException {
        Packet packet = Packet.forPacket(data.text());
        if(packet != null){
            bizThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        // 拿到协议号
                        Protocol protocol = Protocol.forNumber(packet.getUri());
                        Integer uid = ConnManager.getInstance().getUid(ctx.channel());
                        Object body = packet.getBody();
                        if(protocol == null){
                            handleWSClose(ctx.channel());
                            return;
                        }
                        // 根据不同协议，进入部分逻辑分支处理
                        switch (protocol) {
                            // 客户端心跳请求
                            case HeartbeatReq:
                                    MessageUtil.unicast(uid,protocol.getValue(),"心跳包");
                                    break;
                            case Parking_Number:
                                    String pid = (String) body;
                                    if (websocketHandler.redisUtil.hasKey(pid)){
                                        websocketHandler.redisUtil.incr(pid,1);
                                        long expire = websocketHandler.redisUtil.getExpire(pid);
                                        if (expire < 1000*60*60*24*3){
                                            websocketHandler.redisUtil.expire(pid,1000*60*60*24*10);
                                        }
                                    }else{
                                        websocketHandler.redisUtil.set(pid,1,1000*60*60*24*10);
                                    }
                                break;
                            default:
                                // 未知协议
                                handleWSClose(ctx);
                                break;
                        }
                    }catch (Exception e){
                        log.error("handle frame failed.", e);
                    }
                }
            });
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        handleWSClose(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ConnManager.getInstance().onLogout(ctx.channel());
    }

    public void handleWSClose(ChannelHandlerContext ctx) {
        handleWSClose(ctx.channel());
    }

    public void handleWSClose(Channel channel) {
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.close();
    }


}
