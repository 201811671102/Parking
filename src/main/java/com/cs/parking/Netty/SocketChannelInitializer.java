package com.cs.parking.Netty;

import com.cs.parking.handler.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 *
 * @date 2020/5/21
 * @author wangpeng1@huya.com
 *
 */
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 链接idle状态检测，服务端10s内没有收到客户端数据判断为非活跃链接并主动断开链接
        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.MINUTES));
        //编码解码器
        pipeline.addLast(new HttpServerCodec());
        //将多个消息转换成单一的消息对象
        pipeline.addLast(new HttpObjectAggregator(65536));

        pipeline.addLast(new HttpServerHandler());

    }
}
