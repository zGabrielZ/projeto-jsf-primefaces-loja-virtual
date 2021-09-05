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
import javax.servlet.http.HttpSession;

import br.com.gabrielferreira.controller.LoginController;
import br.com.gabrielferreira.entidade.Usuario;

public class FuncionarioFilter implements Filter {

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
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String url = httpServletRequest.getRequestURL().toString();
		HttpSession httpSession = httpServletRequest.getSession();
		
		Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
		
		if(url.contains("/usuario") && usuario.getPerfil().getId().equals(2) && loginController.isPassou()) {
			httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/HomePrincipal.xhtml");
		} else {
			chain.doFilter(httpServletRequest, httpServletResponse);;
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
