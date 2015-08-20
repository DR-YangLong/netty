package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * package: netty.server <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/20 16:20
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // 这个无效的循环等价于这行代码==>System.out.println(buf.toString(io.netty.util.CharsetUtil.US_ASCII))
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg); //另一种做法就是调用in.release()方法。
        }*/

        /**
         * ChannelHandlerContext实例提供了很多操作可以让你触发一些I/O相关的事件和操作。
         * 此处，我们调用write()方法将收到的数据逐字逐句的写入到网络I/O。
         * 注意我们在这里没有释放收到的消息，这一点和DISCARD例子有所不同，
         * 因为Netty会在write操作完成后自动释放相关的消息。
         */
        ctx.write(msg);
        /**
         * ctx.write()并不会立即完成,而是被缓存起来；
         * 当你调用ctx.flush()方法时消息才会被发送。
         * 另外，你也可以调用ctx.writeAndFlush(msg)方法一次性完成上面两个步骤。
         */
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
