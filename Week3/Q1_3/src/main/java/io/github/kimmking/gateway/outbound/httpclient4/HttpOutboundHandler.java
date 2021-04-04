package io.github.kimmking.gateway.outbound.httpclient4;


import io.github.baojia.homework.OkHttpUtils;
import io.github.kimmking.gateway.filter.HeaderHttpResponseFilter;
import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import java.io.IOException;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {
    
    private String backendUrl;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();

    public HttpOutboundHandler(String proxyServer) {

        this.backendUrl = formatUrl(proxyServer);
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }
    
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        final String url = this.backendUrl;
        FullHttpResponse response = null;
        try {
			String body = OkHttpUtils.getAsString(url);
			byte[] bytesArray = body.getBytes("UTF-8");
			response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytesArray));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", bytesArray.length);
            filter.filter(response);

		} catch (IOException e) {
			e.printStackTrace();
			response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
		} finally {
			if (null == response) {
                response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            }
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
        }
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
