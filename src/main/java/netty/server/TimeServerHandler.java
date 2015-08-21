package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.model.UnixTime;

/**
 * package: netty.server <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/20 17:03
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    //当客户端和服务器的连接已建立将会调用channelActive()方法。在这方法中我们向客户端发送一个当前时间的32位整数。
    public void channelActive(final ChannelHandlerContext ctx) {
        /*
        //version1.0
        //要发送新消息，我们需要一个新的buffer来存放消息。我们要写入一个32位的整数，因此我们需要一个4字节大小的ByteBuf。我们通过ChannelHandlerContext.alloc()来获取当前的ByteBufAllocator并且申请一个新的buffer。
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        //像以前一样，我们要发送消息，却没有flip操作。通过NIO发送消息之前不都需要调用java.nio.ByteBuffer.flip()方法吗？由于ByteBuf有2个指针所以不需要该方法；一个用于读操作另一个用于写操作。当你执行写操作时只会增加writer index而不会改变reader index。writer index和reader index互不影响。
        final ChannelFuture f = ctx.writeAndFlush(time);//此时消息并没有发送
        f.addListener(new ChannelFutureListener() {//通过对ChannelFuture添加完成监听器，在消息发送后执行相关操作
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        }); //当write操作完成后会如何通知我们？只需要向ChannelFuture添加ChannelFutureListener。上面的例子中我们创建了一个新的匿名ChannelFutureListener来监听操作完成后关闭相应的Channel.另外，我们也可以使用一个预定义的listener来完成上述操作:f.addListener(ChannelFutureListener.CLOSE);*/
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
