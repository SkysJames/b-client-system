package com.lx.cus.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lx.cus.util.SecurityUtils;

@WebFilter(urlPatterns = {"*", "/*", ""})
public class AuthFillter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (SecurityUtils.getCurrentUser() != null) {
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestURI = httpServletRequest.getRequestURI();
		
		//登录页面
		if (requestURI.endsWith("login") || requestURI.endsWith("login.html")) {
			chain.doFilter(request, response);
			return;
		}
		
		if (requestURI.endsWith("logout")) {
			chain.doFilter(request, response);
			return;
		}
		
		//静态资源
		if (requestURI.indexOf(".") >= 0 && !requestURI.endsWith(".html")) {
			chain.doFilter(request, response);
			return;
		}
		
		((HttpServletResponse)response).sendRedirect("/login.html");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
