package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * package: netty.server <br/>
 * blog:<a href="http://dr-yanglong.github.io/">dr-yanglong.github.io</a><br/>
 * <p>
 * functional describe:
 *
 * @author DR.YangLong [410357434@163.com]
 * @version 1.0    2015/8/20 17:22
 */
public class TimeServer {
    private int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            /**
             * ServerBootstrap是一个用于设置服务器的辅助类。
             * 你可以直接用Channel来设置服务器,但是这样做会比较麻烦，
             * 大多数情况下还是不要这样做。
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(workerGroup)
                    .channel(NioServerSocketChannel.class) //这里我们采用NioServerSocketChannel类来实例化一个进来的连接。
                    .childHandler(new ChannelInitializer<SocketChannel>() { //我们总是为新连接到服务器的handler分配一个新的Channel. ChannelInitializer用于配置新生成的Channel, 就和你通过配置ChannelPipeline来配置Channel是一样的效果。考虑到应用程序的复杂性，你可以采用一个匿名类来向pipeline中添加更多的handler。
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeEncoder(),new TimeServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // 你也可以向指定的Channel设置参数。由于我开发的是TCP/IP服务器，所以我们可以对socket设置诸如tcpNoDelay,keepAlive之类的参数。要了解更多有关ChannelOption的设置请参考相关的api 文档。
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 你是否主要到代码中用到了option(), childOption()两个不同的方法。option() 方法用于设置监听套接字。childOption()则用于设置连接到服务器的客户端套接字。

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); //一切都已准备就绪，接下来就让我们启动服务器。这里我们绑定了主机所有网卡的8080端口。你可以多次调用bind()方法来绑定不同的地址。

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new TimeServer(port).run();
    }
}
