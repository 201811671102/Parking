package com.cs.parking.base.utils;



import com.cs.parking.code.Protocol;
import com.cs.parking.manager.ConnManager;
import com.cs.parking.base.DTO.Packet;
import java.io.IOException;
import java.util.List;


/**
 * @ClassName MessageUtil
 * @Description TODO
 * @Author QQ163
 * @Date 2020/7/20 13:43
 **/
public class MessageUtil {

    /**
     * 消息广播
     */
    public static void broadcast(int uri, Object data) throws IOException {
        Packet packet = new Packet(uri,data);
        List<Integer> uidlist = ConnManager.getInstance().getAllUid();
        if (uidlist != null) {
            for (Integer uid : uidlist) {
                ChannelUtil.write(ConnManager.getInstance().getChannel(uid), Packet.forSend(packet));
            }
        }
    }

    /**
     * 按玩家unionId单播
     */
    public static void unicast(Integer uid, int uri, Object data) throws IOException {
        Packet packet = new Packet(uri,data);
        ChannelUtil.write(ConnManager.getInstance().getChannel(uid), Packet.forSend(packet));
    }

    public static void unicast(Integer uid, Protocol protocol) throws IOException {
        Packet packet = new Packet(protocol.getValue(),protocol.getData());
        ChannelUtil.write(ConnManager.getInstance().getChannel(uid), Packet.forSend(packet));
    }

}
