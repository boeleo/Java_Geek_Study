package io.github.kimmking.gateway.inbound;

import io.github.baojia.homework.ProxyBizUriFilter;
import io.github.kimmking.gateway.filter.HeaderHttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;
    private HttpOutboundHandler handler;
    private HttpRequestFilter headerFilter = new HeaderHttpRequestFilter();
    private HttpRequestFilter uriFilter = new ProxyBizUriFilter();
    
    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        this.handler = new HttpOutboundHandler(this.proxyServer);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            headerFilter.filter(fullRequest, ctx);
            uriFilter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
