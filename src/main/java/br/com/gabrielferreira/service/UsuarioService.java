package br.com.gabrielferreira.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.search.UsuarioSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.utils.Transacional;
import br.com.gabrielferreira.utils.validation.UsuarioLoteValidacao;
import br.com.gabrielferreira.utils.validation.UsuarioValidacaoArquivo;

public class UsuarioService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SaldoService saldoService;
	
	@Inject
	private UsuarioRepositorio usuarioRepositorio;
	
	@Inject
	private UsuarioValidacaoArquivo usuarioValidacaoArquivo;
	
	@Transacional
	public void inserir(Usuario usuario, List<Saldo> saldos) throws RegraDeNegocioException {
		verificarEmailUsuario(usuario.getEmail());
		verificarCpfUsuario(usuario.getCpf());
		usuarioRepositorio.inserir(usuario);
		verificarSaldo(usuario,saldos);
	}
	
	public List<UsuarioLoteValidacao> validacaoUsuariosLote(List<Usuario> usuarios, 
			List<UsuarioLoteValidacao> usuarioLoteValidacaos) throws ParseException{
		List<UsuarioLoteValidacao> validacaos = new ArrayList<UsuarioLoteValidacao>();
		for(Usuario usuario : usuarios) {
			validacaos = usuarioValidacaoArquivo.validarUsuarioEmLote(usuario, usuarioLoteValidacaos);
		}
		return validacaos;
	}
	
	@Transacional
	public void inserirUsuarioLote(List<Usuario> usuarios) {
		for(Usuario usuario : usuarios) {
			usuarioRepositorio.inserir(usuario);
		}
	}
	
	@Transacional
	public void atualizarUsuario(Usuario usuario) throws RegraDeNegocioException {
		verificarEmailUsuarioAtualizado(usuario.getEmail(), usuario.getId());
		verificarCpfUsuarioAtualizado(usuario.getCpf(),usuario.getId());
		usuarioRepositorio.atualizar(usuario);
	}
	
	@Transacional
	public void removerUsuario(Usuario usuario) {
		usuarioRepositorio.remover(usuario);
	}
	
	@Transacional
	public void atualizarUsuarioSaldo(Usuario usuario) {
		usuarioRepositorio.atualizar(usuario);
	}
	
	public Usuario getDetalhe(Integer id) {
		return usuarioRepositorio.procurarPorId(id);
	}
	
	public List<Usuario> getFiltrar(UsuarioSearch usuarioSearch){
		List<Usuario> usuarios = usuarioRepositorio.filtrar(usuarioSearch);
		return usuarios;
	}
	
	public void verificarSaldo(Usuario usuario,List<Saldo> saldos) {
		if(!saldos.isEmpty()) {
			for(Saldo saldo : saldos) {
				saldo.setUsuario(usuario);
				usuario.getSaldos().add(saldo);
				saldoService.inserir(saldo);
				atualizarUsuarioSaldo(usuario);
			}
		}
	}
	
	public void verificarArquivoTxt(Part arquivoUploadExcelTxt, List<Usuario> usuarios, DateTimeFormatter dtf) throws IOException {
		Scanner scanner = new Scanner(arquivoUploadExcelTxt.getInputStream());
		Integer codigoUsuario = 1;
		while (scanner.hasNext()) {
			String linha = scanner.nextLine();
			
			if(linha != null && !linha.isEmpty()) {
				String[] delimitador = linha.split("\\;");
				Usuario usuario = new Usuario();
				usuario.setCodigoUsuario(codigoUsuario++);
				usuarioValidacaoArquivo.verificarLinhaArquivo(delimitador, usuario, dtf);
				usuarios.add(usuario);
			}
		}
		
		scanner.close();
	}
	
	public void verificarArquivoExcel(Part arquivoUploadExcelTxt, List<Usuario> usuarios) throws IOException, ParseException {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(arquivoUploadExcelTxt.getInputStream()); // Preparando para ler o excel
		XSSFSheet planilha = xssfWorkbook.getSheetAt(0); // Pegar a primeira planilha do excel
		Iterator<Row> linhasIterator = planilha.iterator(); // Percorrendo nas linhas
		Integer codigoUsuario = 1;
		
		while (linhasIterator.hasNext()) { // Enquanto tiver linha no arquivo do excel
			Row linha = linhasIterator.next(); // Dados na linha
			
			if(linha.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> celulaIterator = linha.iterator(); // Enquanto tiver linhas, vai pecorrer as celulas
		
			Usuario usuario = new Usuario();
			Perfil perfil = new Perfil();
			usuario.setCodigoUsuario(codigoUsuario++);
			while (celulaIterator.hasNext()) {
				Cell celula = celulaIterator.next();
				switch (celula.getColumnIndex()) {
				case 0:
					usuarioValidacaoArquivo.verificarCelulaNome(celula,usuario);
					break;
				case 1:
					usuarioValidacaoArquivo.verificarCelulaEmail(celula, usuario);
					break;
				case 2:
					usuarioValidacaoArquivo.verificarCelulaCpf(celula, usuario);
					break;
				case 3:
					usuarioValidacaoArquivo.verificarCelulaDataNascimento(celula, usuario);
					break;
				case 4:
					usuarioValidacaoArquivo.verificarCelulaPerfil(celula, usuario, perfil);
					break;
				default:
					break;
				}
				
			} // Fim das celula 
			usuarios.add(usuario);
		}
		xssfWorkbook.close();
	}
	
	public void verificarCpfUsuario(String cpf) throws RegraDeNegocioException {
		if(usuarioRepositorio.verificarCpf(cpf)) {
			throw new RegraDeNegocioException("Não é possível cadastrar este CPF, pois já está cadastrado.");
		}
	}
	
	public void verificarEmailUsuario(String email) throws RegraDeNegocioException {
		if(usuarioRepositorio.verificarEmail(email)) {
			throw new RegraDeNegocioException("Não é possível cadastrar este e-mail, pois já está cadastrado.");
		}
	}
	
	public void verificarEmailUsuarioAtualizado(String email, Integer id) throws RegraDeNegocioException {
		if(usuarioRepositorio.verificarEmailAtualizado(email, id)) {
			throw new RegraDeNegocioException("Não é possível atualizar este e-mail, pois já está cadastrado.");
		}
	}
	
	public void verificarCpfUsuarioAtualizado(String cpf, Integer id) throws RegraDeNegocioException {
		if(usuarioRepositorio.verificarCpfAtualizado(cpf, id)) {
			throw new RegraDeNegocioException("Não é possível atualizar este CPF, pois já está cadastrado.");
		}
	}
	

}
