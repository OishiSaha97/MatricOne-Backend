package com.datasoft.luncheon.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {

	@Value("${cors-allowed-origin}")
	private String corsAllowedOrigin;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		response.setHeader("Access-Control-Allow-Origin", corsAllowedOrigin);
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-xsrf-token, x-requested-with, authorization, Content-Type, Accept");
		response.setHeader("X-Frame-Options", "deny");
		response.setHeader("X-Xss-Protection", "1; mode=block");
		response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubdomains");
		response.setHeader("Content-Security-Policy", "default-src 'self';");
		response.setHeader("X-Permitted-Cross-Domain-Policies", "none");
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setHeader("X-Powered-By", "Datasoft's development team");
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(new XSSRequestWrapper(request), res);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}