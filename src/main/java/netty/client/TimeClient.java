package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * package: netty.client <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/20 17:15
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        args=new String[]{"127.0.0.1","8080"};
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); //Bootstrap 和ServerBootstrap比较像，只是Bootstrap主要用于客户端或无连接的channel.
            b.group(workerGroup); // 如果你只用了一个EventLoopGroup,那么它将被用在boos和worker上。只是客户端用不上bosss事件池
            b.channel(NioSocketChannel.class); //这里我采用NioSocketChannel而不是NioServerSocketChannel。
            b.option(ChannelOption.SO_KEEPALIVE, true); //注意我们没有使用childOption()因为客户端SocketChannel没有父节点。
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); //我们应该调用connect()方法而不是bind()方法。

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
