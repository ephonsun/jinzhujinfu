package com.canary.finance.remote.netty;

import static com.canary.finance.remote.util.ConstantUtil.MAX_FRAME_LENGTH;
import static com.canary.finance.remote.util.ConstantUtil.SO_BACKLOG;
import static com.canary.finance.remote.util.ConstantUtil.LOGGER;

import org.apache.commons.lang.StringUtils;

import com.canary.finance.config.RemoteProperties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RemoteServer {
	private final RemoteProperties properties;
	
	public RemoteServer(RemoteProperties properties) {
		this.properties = properties;
	}
	
	public void start() throws Exception {
		if (StringUtils.isEmpty(this.properties.getServerIp()) || this.properties.getServerPort() <= 0) {
			throw new RuntimeException("remote server starting error: ip or port can not be empty.");
		}
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2+1);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if(bossGroup != null) {
					bossGroup.shutdownGracefully();
				}
				if(workerGroup != null) {
					workerGroup.shutdownGracefully();
				}
			}
		});
		
        ServerBootstrap bootstrap = new ServerBootstrap();  
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        	.option(ChannelOption.SO_BACKLOG, SO_BACKLOG)
        	.handler(new LoggingHandler(LogLevel.INFO))
        	.childOption(ChannelOption.SO_KEEPALIVE, true)
        	.childHandler(new ChannelInitializer<SocketChannel>(){
        		@Override
				protected void initChannel(SocketChannel ch) throws Exception {
        			ch.pipeline().addLast(new HttpServerCodec());
            		ch.pipeline().addLast(new HttpObjectAggregator(MAX_FRAME_LENGTH));
            		ch.pipeline().addLast(new HttpContentDecompressor());
            		ch.pipeline().addLast(new RemoteHandler(properties));
				}
        	});
        LOGGER.info("remote server start on {}:{}", this.properties.getServerIp(), this.properties.getServerPort());
		ChannelFuture channelFuture = bootstrap.bind(this.properties.getServerIp(), this.properties.getServerPort()).sync();
		channelFuture.channel().closeFuture().sync();
    }  
}
