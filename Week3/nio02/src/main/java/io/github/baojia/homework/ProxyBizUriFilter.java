package io.github.baojia.homework;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class ProxyBizUriFilter implements HttpRequestFilter {

	@Override
	public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
		String uri = fullRequest.uri();
		System.out.println("filter gets request, uri: " + uri);
		if (!uri.endsWith("hello")) {
			throw new RuntimeException("Unsupported uri: " + uri);
		}
	}
}
