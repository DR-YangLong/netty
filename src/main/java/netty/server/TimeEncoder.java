package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import netty.model.UnixTime;

/**
 * package: netty.server <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/21 14:02
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt(m.value());
        ctx.write(encoded, promise); // (1)
    }

    /**
     *public class TimeEncoder extends MessageToByteEncoder<UnixTime> {
     *@Override
     *protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
     *out.writeInt(msg.value());
     *}
     *}
     */
}
