package br.com.gabrielferreira.utils.validation;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;

public class UsuarioValidacaoArquivo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioRepositorio usuarioRepositorio;
	
	// Validação ao importar excel o nome do usuário
	public void verificarCelulaNome(Cell celula, Usuario usuario) {
		if(CellType.STRING == celula.getCellType()) {
			usuario.setNome(celula.getStringCellValue());
		} else if (CellType.NUMERIC == celula.getCellType()) {
			String nome = NumberToTextConverter.toText(celula.getNumericCellValue());
			usuario.setNome(nome);
		} else {
			usuario.setNome(null);
		}
	}
	
	// Validação ao importar excel o email do usuário
	public void verificarCelulaEmail(Cell celula, Usuario usuario) {
		if(CellType.STRING == celula.getCellType()) {
			usuario.setEmail(celula.getStringCellValue());
		} else if (CellType.NUMERIC == celula.getCellType()) {
			String email = NumberToTextConverter.toText(celula.getNumericCellValue());
			usuario.setEmail(email);
		} else {
			usuario.setEmail(null);
		}
	}
	
	// Validação ao importar excel o cpf do usuário
	public void verificarCelulaCpf(Cell celula, Usuario usuario) {
		if(CellType.STRING == celula.getCellType()) {
			usuario.setCpf(celula.getStringCellValue());
		} else if (CellType.NUMERIC == celula.getCellType()) {
			String cpf = NumberToTextConverter.toText(celula.getNumericCellValue());
			usuario.setCpf(cpf);
		} else {
			usuario.setCpf(null);
		}
	}
	
	// Validação ao importar excel a data de nascimento do usuário
	public void verificarCelulaDataNascimento(Cell celula, Usuario usuario) {
		if(CellType.NUMERIC == celula.getCellType()) {
			Date data = DateUtil.getJavaDate(celula.getNumericCellValue());
			LocalDate dataNascimento = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			usuario.setDataNascimento(dataNascimento);
		} else {
			usuario.setDataNascimento(null);
		}
	}
	
	// Validação ao importar excel o perfil do usuário
	public void verificarCelulaPerfil(Cell celula, Usuario usuario, Perfil perfil) {
		if (CellType.NUMERIC == celula.getCellType()) {
			perfil.setId((int)celula.getNumericCellValue());
			usuario.setPerfil(perfil);
		} else {
			perfil.setId(null);
			usuario.setPerfil(perfil);
		}
	}
	
	public void verificarLinhaArquivo(String delimitador[], Usuario usuario, DateTimeFormatter dtf) {
		if(delimitador.length != 0) {
			verificarLinhaNome(delimitador, usuario);
			verificarLinhaEmail(delimitador, usuario);
			verificarLinhaCpf(delimitador, usuario);
			verificarLinhaDataNascimento(delimitador, usuario, dtf);
			verificarLinhaPerfil(delimitador, usuario);
		}
	}
	
	private void verificarLinhaNome(String delimitador[],Usuario usuario) {
		if(!delimitador[0].isEmpty()) {
			usuario.setNome(delimitador[0]);
		} else {
			usuario.setNome(null);
		}
	}
	
	private void verificarLinhaEmail(String delimitador[],Usuario usuario) {
		if(!delimitador[1].isEmpty()) {
			usuario.setEmail(delimitador[1]);
		} else {
			usuario.setEmail(null);
		}
	}
	
	private void verificarLinhaCpf(String delimitador[],Usuario usuario) {
		if(!delimitador[2].isEmpty()) {
			usuario.setCpf(delimitador[2]);
		} else {
			usuario.setCpf(null);
		}
	}
	
	public void verificarLinhaDataNascimento(String delimitador[],Usuario usuario, DateTimeFormatter dtf) {
		if(!delimitador[3].isEmpty()) {
			usuario.setDataNascimento(LocalDate.parse(delimitador[3], dtf));
		} else {
			usuario.setDataNascimento(null);
		}
	}
	
	private void verificarLinhaPerfil(String delimitador[],Usuario usuario) {
		Perfil perfil = new Perfil();
		try {
			perfil.setId(Integer.parseInt(delimitador[4]));
			usuario.setPerfil(perfil);
		} catch (ArrayIndexOutOfBoundsException e) {
			perfil.setId(null);
			usuario.setPerfil(perfil);
		}
	}
	
	public List<UsuarioLoteValidacao> validarUsuarioEmLote(Usuario usuario, List<UsuarioLoteValidacao> usuarioLoteValidacaos) throws ParseException{
		validarNome(usuario.getNome(),usuario.getCodigoUsuario(),usuarioLoteValidacaos);
		validarEmail(usuario.getEmail(),usuario.getCodigoUsuario(),usuarioLoteValidacaos);
		validarCpf(usuario,usuario.getCodigoUsuario(),usuarioLoteValidacaos);
		validarDataNascimento(usuario.getDataNascimento(),usuario.getCodigoUsuario(),usuarioLoteValidacaos);
		validarPerfil(usuario.getPerfil(),usuario.getCodigoUsuario(),usuarioLoteValidacaos);
		return usuarioLoteValidacaos;
	}
	
	private void validarNome(String nome,Integer codigoUsuario,List<UsuarioLoteValidacao> loteValidacaos) {
		if(nome == null) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"Nome é obrigatório."));
		} else if(nome.length() > 150) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O nome não pode ultrapassar de 150 de caracteres."));
		}
	}
	
	private void validarEmail(String email,Integer codigoUsuario,List<UsuarioLoteValidacao> loteValidacaos) {
		if(email == null) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"E-mail é obrigatório."));
		} else if(!validarEmail(email)) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O E-mail não é válido."));
		} else if(usuarioRepositorio.verificarEmail(email)) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O E-mail já está cadastrado."));
		}
	}
	
	private void validarCpf(Usuario usuario,Integer codigoUsuario,List<UsuarioLoteValidacao> loteValidacaos) throws ParseException {
		
		MaskFormatter cpfFormatacao = new MaskFormatter("###.###.###-##");
		cpfFormatacao.setValueContainsLiteralCharacters(false);
		
		if(usuario.getCpf() == null) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O cpf é obrigatório."));
		} else if(usuario.getCpf().length() != 11) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O cpf tem que ter 11 caracteres."));
		} else if (!StringUtils.isNumeric(usuario.getCpf())) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O cpf tem que ser númerico."));
		} else if (!CpfValidation.isValidCPF(usuario.getCpf())) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O cpf não é válido."));
		} else if (usuarioRepositorio.verificarCpf(usuario.getCpf())) {
			loteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O cpf já está cadastrado."));
		} else {
			usuario.setCpf(cpfFormatacao.valueToString(usuario.getCpf()));
		}
	}
	
	private void validarDataNascimento(LocalDate dataNascimento, Integer codigoUsuario,
			List<UsuarioLoteValidacao> usuarioLoteValidacaos) {
		
		LocalDate dataAtual = LocalDate.now();
		
		if(dataNascimento == null) {
			usuarioLoteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"A data de nascimento é obrigatório."));
		} else if(dataNascimento.isAfter(dataAtual)) {
			usuarioLoteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"A data de nascimento não pode ser maior que a data atual."));
		}
	}
	
	private void validarPerfil(Perfil perfil, Integer codigoUsuario,
			List<UsuarioLoteValidacao> usuarioLoteValidacaos) {
		if(perfil.getId() == null) {
			usuarioLoteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"O perfl é obrigatório."));
		} else if(perfil.getId() != 1 && perfil.getId() != 2 && perfil.getId() != 3) {
			usuarioLoteValidacaos.add(new UsuarioLoteValidacao(codigoUsuario,"Tem que informar o perfil correto."));
		}
	}

	
	public boolean validarEmail(String email) {
		boolean resultado = true;
		try {
			InternetAddress internetEmail = new InternetAddress(email);
			internetEmail.validate();
		} catch (AddressException e) {
			resultado = false;
		}
		return resultado;
	}

}
