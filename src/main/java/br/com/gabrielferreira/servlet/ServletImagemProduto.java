package br.com.gabrielferreira.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.service.ProdutoService;

@WebServlet("/ServletImagemProduto")
public class ServletImagemProduto extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoService produtoService;
	
	public ServletImagemProduto() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idImagem = req.getParameter("idImagem");
		if(idImagem == null || idImagem.equals("")) {
			return;
		}
		
		Produto produto = produtoService.procurarPorId(Integer.parseInt(idImagem));
		byte[] arrayImagem = produto.getByteImagem();
		resp.getOutputStream().write(arrayImagem);
		
	}

}
