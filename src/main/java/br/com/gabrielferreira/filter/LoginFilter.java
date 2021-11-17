package br.com.gabrielferreira.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.gabrielferreira.entidade.Usuario;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) 	response;
		
		HttpSession session = httpServletRequest.getSession();
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
		String url = httpServletRequest.getRequestURL().toString();
		
		if(usuarioLogado == null && !url.contains("Login")) {
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
