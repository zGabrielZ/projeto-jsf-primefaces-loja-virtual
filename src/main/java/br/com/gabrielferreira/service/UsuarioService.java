package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.search.UsuarioSearch;
import br.com.gabrielferreira.entidade.to.UsuarioLoteTo;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.utils.LoginJSF;
import br.com.gabrielferreira.utils.validation.UsuarioValidacaoArquivo;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
	
	public void inserir(Usuario usuario, List<Saldo> saldos) throws RegraDeNegocioException {
		verificarEmailUsuario(usuario.getEmail());
		verificarCpfUsuario(usuario.getCpf());
		String senha = verificarSenha(usuario.getSenha(),usuario.getPerfil());
		if(StringUtils.isNotBlank(senha)) {
			usuario.setSenha(senha);
		}
		usuarioRepositorio.inserir(usuario);
		verificarSaldo(usuario,saldos);
	}
	
	public void inserirUsuarioLote(List<Usuario> usuarios) {
		for(Usuario usuario : usuarios) {
			usuarioRepositorio.inserir(usuario);
		}
	}
	
	public void atualizarUsuario(Usuario usuario) throws RegraDeNegocioException {
		verificarEmailUsuarioAtualizado(usuario.getEmail(), usuario.getId());
		verificarCpfUsuarioAtualizado(usuario.getCpf(),usuario.getId());
		usuarioRepositorio.atualizar(usuario);
	}
	
	public Usuario atualizarSenhaUsuario(Usuario usuario) {
		String senhaCriptografada = usuarioRepositorio.transformarSenha(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		return usuarioRepositorio.atualizar(usuario);
	}
	
	public void removerUsuario(Usuario usuario) throws RegraDeNegocioException {
		verificarUsuarioLogado(usuario);
		usuarioRepositorio.deletarPorId(Usuario.class, usuario.getId());
	}
	
	public void atualizarUsuarioSaldo(Usuario usuario) {
		usuarioRepositorio.atualizar(usuario);
	}
	
	public Usuario getDetalhe(Integer id) {
		return usuarioRepositorio.pesquisarPorId(id, Usuario.class);
	}
	
	public Usuario getEmailUsuario(String email) {
		return usuarioRepositorio.getUsuarioEmail(email);
	}
	
	public List<Usuario> getFiltrar(UsuarioSearch usuarioSearch, int primeiroPosicao, int quantidadeMaxima){
		List<Usuario> usuarios = usuarioRepositorio.filtrar(usuarioSearch,primeiroPosicao,quantidadeMaxima);
		return usuarios;
	}
	
	public Usuario getVerificarEmailSenhaLogin(String email, String senha) {
		return usuarioRepositorio.verificarEmailSenha(email, senha);
	}
	
	public Integer quantidadeRegistro(UsuarioSearch usuarioSearch) {
		return usuarioRepositorio.quantidadeRegistro(usuarioSearch);
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
	
	public List<UsuarioLoteTo> verificarArquivoTxtUpload(Part arquivoUpload){
		List<UsuarioLoteTo> usuarioLoteTxtTos = new ArrayList<>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(arquivoUpload.getInputStream());
			while (scanner.hasNext()) {
				String linha = scanner.nextLine();
				if(linha != null && !linha.isEmpty()) {
					if(linha.contains(";")) {
						String[] delimitador = linha.split("\\;");
						UsuarioLoteTo usuarioLoteTxtTo = new UsuarioLoteTo();
						usuarioValidacaoArquivo.verificarLinhaArquivo(delimitador, usuarioLoteTxtTo);
						usuarioLoteTxtTos.add(usuarioLoteTxtTo);
					}
				}
			}
			
			// Após verificar linha por linha, vai ser necessário validar os dados 
			usuarioValidacaoArquivo.validarDadosUsuario(usuarioLoteTxtTos);
			
		} catch (Exception e) {
			throw new EmptyStackException();
		}
		
		
		scanner.close();		
		return usuarioLoteTxtTos;
	}
	
	public void gerarRelatorioErrosUsuario(List<UsuarioLoteTo> usuarios) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		try {
			
			Map<String, Object> parametros = new LinkedHashMap<>();
			
			String caminho = context.getExternalContext().getRealPath("/resources/relatorio/Erros.jrxml");
			JasperReport compilarRelatorio = JasperCompileManager.compileReport(caminho);
			
			String caminhoErrosJasper = context.getExternalContext().getRealPath("/resources/subrelatorio/ErrosUpload.jasper");
			parametros.put("SUBREPORT_DIR", caminhoErrosJasper);
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usuarios);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compilarRelatorio,parametros,dataSource);
			
			byte [] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			String nomeRelatorio = "Erros Encontrados.pdf";
			response.setHeader("Content-disposition","attachment; filename="+nomeRelatorio);
			response.setContentType("application/pdf"); 
			response.setContentLength(bytes.length);
			
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			throw new EmptyStackException();
		}
	}
	
	public List<UsuarioLoteTo> verificarArquivoExcel(Part arquivoUploadExcel) {
		List<UsuarioLoteTo> usuarios = new ArrayList<>();
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(arquivoUploadExcel.getInputStream()); // Preparando para ler o excel
			XSSFSheet planilha = xssfWorkbook.getSheetAt(0); // Pegar a primeira planilha do excel
			Iterator<Row> linhasIterator = planilha.iterator(); // Percorrendo nas linhas
			Integer codigoUsuario = 1;
			
			while (linhasIterator.hasNext()) { // Enquanto tiver linha no arquivo do excel
				Row linha = linhasIterator.next(); // Dados na linha
				
				if(linha.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> celulaIterator = linha.iterator(); // Enquanto tiver linhas, vai pecorrer as celulas
			
				UsuarioLoteTo usuario = new UsuarioLoteTo();
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
						usuarioValidacaoArquivo.verificarCelulaPerfil(celula, usuario);
						break;
					default:
						break;
					}
					
				} // Fim das celula 
				usuarios.add(usuario);
			}
			
			// Após verificar linha por linha, vai ser necessário validar os dados 
			usuarioValidacaoArquivo.validarDadosUsuario(usuarios);
			
			xssfWorkbook.close();
		} catch (Exception e) {
			throw new EmptyStackException();
		}
		return usuarios;
	}
	
	public boolean verificarEmailLogin(String email) {
		return usuarioRepositorio.verificarEmail(email);
	}
	
	public Usuario procurarEmail(String email) {
		return usuarioRepositorio.procurarPorEmail(email);
	}
	
	public void verificarCpfUsuario(String cpf) throws RegraDeNegocioException {
		if(usuarioRepositorio.verificarCpf(cpf)) {
			throw new RegraDeNegocioException("Não é possível cadastrar este CPF, pois já está cadastrado.");
		}
	}
	
	public String verificarSenha(String senha, Perfil perfil) {
		if(StringUtils.isNotBlank(senha) && perfil != null) {
			String senhaTransformada = usuarioRepositorio.transformarSenha(senha);
			senha = senhaTransformada;
		}
		return senha;
	}
	
	public void verificarUsuarioLogado(Usuario usuarioSelecionado) throws RegraDeNegocioException {
		Usuario usuarioLogado = LoginJSF.getRecuperarUsuarioLogada();
		if(usuarioLogado.getId().equals(usuarioSelecionado.getId())) {
			throw new RegraDeNegocioException("Não é possível excluir o próprio usuário logado.");
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
	
	public void verificarEmailTrocarSenha(Usuario usuario) throws RegraDeNegocioException {
		if(!usuarioRepositorio.verificarEmail(usuario.getEmail())) {
			throw new RegraDeNegocioException("Não existe este e-mail cadastrado no nosso sistema !");
		}
	}
	

}
