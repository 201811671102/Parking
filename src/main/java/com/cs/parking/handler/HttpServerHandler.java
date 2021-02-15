package com.cs.parking.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.parking.dto.NoticeMessageDTO;
import com.cs.parking.manager.ConnManager;
import com.cs.parking.base.config.WSConfig;
import com.cs.parking.base.utils.MessageUtil;
import com.cs.parking.service.NoticeMessageService;
import com.google.common.base.Preconditions;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 *
 * @date 2020/5/21
 * @author wangpeng1@huya.com
 * @description http协议处理
 */
@Slf4j
@Component
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private JWTVerifier verifier;

    public static HttpServerHandler httpServerHandler;
    @PostConstruct
    public void init(){httpServerHandler = this;}
    @Autowired
    NoticeMessageService noticeMessageService;


    private static final WebsocketHandler websocketHandler = new WebsocketHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FullHttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                HttpHeaders headers = request.headers();
                // 判断ws upgrade
                if (!StringUtils.isEmpty(headers.get(HttpHeaderNames.SEC_WEBSOCKET_KEY))) {
                    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
                    Map<String, List<String>> params = queryStringDecoder.parameters();
                    Preconditions.checkArgument(params.containsKey("jwt"), "jwt参数为空");
                    String jwt = params.get("jwt").get(0);
                    List<NoticeMessageDTO> noticeMessageDTOList = null;
                    try {
                        // jwt校验
                        Algorithm algorithm = Algorithm.HMAC256(WSConfig.secretKey);
                        this.verifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = verifier.verify(jwt);
                        Integer uid = decodedJWT.getClaim("uid").asInt();
                        Preconditions.checkArgument(!StringUtils.isEmpty(uid), "token信息错误，userId:{}", uid);
                        handleHandshake(ctx, request, uid);
                        //接收消息
                        noticeMessageDTOList = httpServerHandler.noticeMessageService.selectUser(uid);
                        if (noticeMessageDTOList != null && noticeMessageDTOList.size() > 0) {
                            noticeMessageDTOList.forEach(noticeMessageDTO -> {
                                try {
                                    MessageUtil.unicast(uid, noticeMessageDTO.getValue(), noticeMessageDTO.getData());
                                    httpServerHandler.noticeMessageService.update(noticeMessageDTO.getId());
                                } catch (IOException e) {
                                    log.info("用户登录接收消息出错！\n " + e.getMessage());
                                }
                            });
                        }
                    }catch (TokenExpiredException e ){
                        ctx.close();
                    }catch (JWTVerificationException e){
                        log.error("token校验失败 jwt:"+jwt, e);
                        ctx.close();
                    }catch (Exception e){
                        log.info(e.getMessage());
                    }
                }else{
                    FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.wrappedBuffer(new String("没有权限").getBytes()));
                    HttpUtil.setContentLength(res, res.content().readableBytes());
                    ctx.channel().writeAndFlush(res);
                }
            }
        }catch (Exception e){
            ctx.close();
            ReferenceCountUtil.release(msg);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handleHandshake(
            final ChannelHandlerContext ctx, HttpRequest req, Integer uid){
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(getWebSocketURL(req), null, true);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            ChannelFuture future = handshaker.handshake(ctx.channel(), req);
            future.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        // 握手成功，升级到websocket协议
                        ctx.pipeline().replace(ctx.handler(), "websocketHandler", websocketHandler);
                        ConnManager.getInstance().onLogin(uid,ctx.channel());
                    }else{
                        // 握手失败
                        ctx.close();
                    }
                }
            });
        }
    }

    private String getWebSocketURL(HttpRequest req) {
        return "ws://" + req.headers().get(HttpHeaderNames.HOST) + req.getUri();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("http channel exception. close connection..." + " " + ctx.channel().id(), cause);
        ctx.close();
    }

}
