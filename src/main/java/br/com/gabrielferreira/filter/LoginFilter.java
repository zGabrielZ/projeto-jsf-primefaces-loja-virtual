package br.com.gabrielferreira.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.gabrielferreira.controller.LoginController;

public class LoginFilter implements Filter {

	@Inject
	private LoginController loginController;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) 	response;
		String url = httpServletRequest.getRequestURL().toString();
		
		if( (url.contains("/categoria") || url.contains("/produto") || url.contains("/saldo") || url.contains("/usuario") || url.contains("/HomePrincipal.xhtml") || url.contains("/endereco") || url.contains("/pedido") ) 
				&& !loginController.isPassou()) {
			httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/login/Login.xhtml");
		} else {
			chain.doFilter(httpServletRequest, httpServletResponse);;
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
