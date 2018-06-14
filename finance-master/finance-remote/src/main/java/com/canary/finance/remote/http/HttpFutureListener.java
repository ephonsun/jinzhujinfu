package com.canary.finance.remote.http;

import static com.canary.finance.remote.util.ConstantUtil.LOGGER;
import static com.canary.finance.remote.util.ConstantUtil.CONTENT_TYPE;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class HttpFutureListener implements FutureListener<String> {
	private final ChannelHandlerContext ctx;
	private final String url;
	private final String message;
	
	public HttpFutureListener(ChannelHandlerContext ctx, String url, String message) {
		this.ctx = ctx;
		this.url = url;
		this.message = message;
	}

	@Override
	public void operationComplete(Future<String> future) throws Exception {
		if(future.isSuccess()) {
			String response = future.get();
			LOGGER.info("{} result: {}", this.url, response);
			this.response(ctx, response);
		} else {
			this.response(ctx, this.message);
		}
	}
	
	private void response(final ChannelHandlerContext ctx,  String body) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(body.getBytes(CharsetUtil.UTF_8)));
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, CONTENT_TYPE);
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		ctx.writeAndFlush(response);
	}
}