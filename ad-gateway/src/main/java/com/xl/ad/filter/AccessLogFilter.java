package com.xl.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AccessLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        long now = System.currentTimeMillis();
        RequestContext context = RequestContext.getCurrentContext();
        long startTime = (long) context.get("startTime");
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        long duration = now - startTime;
        log.info("uri: "+uri+",time: "+duration/100);
        return null;
    }
}
