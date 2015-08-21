package netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import netty.model.UnixTime;

import java.util.List;

/**
 * package: netty.client <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/21 11:28
 */
public class TimeDecoder extends ByteToMessageDecoder {//ByteToMessageDecoder是ChannelInboundHandler的具体实现，该类使得处理分片变得更容易。
    @Override
    //ByteToMessageDecoder 调用decode()方法来处理接收缓存中的数据。
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            //当累积缓存中的数据不满足处理要求是decode()不需要向out中添加任何东西。当有更多数据到来时ByteToMessageDecoder会再次调用decode()方法。
            return;
        }
        //当decode()向out添加数据时，表明decoder已成功处理一个消息；这时候ByteToMessageDecoder 会丢弃缓冲区中已处理成功的数据。请记住你不需要处理多个消息，ByteToMessageDecoder 会多次调用decode()方法直到没有消息可以处理。
        out.add(new UnixTime(in.readInt()));
    }
}
