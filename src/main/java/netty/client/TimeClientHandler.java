package netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * package: netty.client <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/20 17:17
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4); //1. ChannelHandler有两个和生命周期相关的listener：handlerAdded() 和 handlerRemoved(). 你可以在这两个方法中执行任何初始化或反初始化方法，但是不能长时间阻塞该方法。
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release(); //1. ChannelHandler有两个和生命周期相关的listener：handlerAdded() 和 handlerRemoved(). 你可以在这两个方法中执行任何初始化或反初始化方法，但是不能长时间阻塞该方法。
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        /*ByteBuf m = (ByteBuf) msg; // (1)
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }*/

        ByteBuf m = (ByteBuf) msg;
        //首先，所有收到的数据都会累积到buf中.
        buf.writeBytes(m);
        m.release();
// 然后，handler必须检查buf中是否有足够的数据可供处理，本例中是4字节，进行相关的逻辑处理。如果不够4个字节，Netty将继续调用channelRead()方法来接收更多的数据。
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (buf.readInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
