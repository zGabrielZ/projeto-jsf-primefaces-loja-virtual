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

import br.com.gabrielferreira.entidade.to.UsuarioLoteErrosTo;
import br.com.gabrielferreira.entidade.to.UsuarioLoteTo;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;

public class UsuarioValidacaoArquivo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioRepositorio usuarioRepositorio;
	
	// Validação ao importar excel o nome do usuário
	public void verificarCelulaNome(Cell celula, UsuarioLoteTo usuario) {
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
	public void verificarCelulaEmail(Cell celula, UsuarioLoteTo usuario) {
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
	public void verificarCelulaCpf(Cell celula, UsuarioLoteTo usuario) {
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
	public void verificarCelulaDataNascimento(Cell celula, UsuarioLoteTo usuario) {
		if(CellType.NUMERIC == celula.getCellType()) {
			Date data = DateUtil.getJavaDate(celula.getNumericCellValue());
			LocalDate dataNascimento = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			usuario.setDataNascimento(dataNascimento);
		} else {
			usuario.setDataNascimento(null);
		}
	}
	
	// Validação ao importar excel o perfil do usuário
	public void verificarCelulaPerfil(Cell celula, UsuarioLoteTo usuario) {
		if (CellType.NUMERIC == celula.getCellType()) {
			usuario.setIdPerfil((int)celula.getNumericCellValue());
		} else {
			usuario.setIdPerfil(null);
		}
	}
	
	public void verificarLinhaArquivo(String delimitador[], UsuarioLoteTo usuario) {
		if(delimitador.length != 0) {
			for(String elemento : delimitador) {
				int posicaoElemento = buscarPosicao(delimitador, elemento);
				if(posicaoElemento == 0) {
					String nome = verificarLinha(elemento);
					usuario.setNome(nome);
				} else if (posicaoElemento == 1) {
					String email = verificarLinha(elemento);
					usuario.setEmail(email);
				} else if(posicaoElemento == 2) {
					String cpf = verificarLinha(elemento);
					usuario.setCpf(cpf);
				} else if(posicaoElemento == 3) {
					LocalDate dataNascimento = verificarLinhaDataNascimento(elemento);
					usuario.setDataNascimento(dataNascimento);
				} else if(posicaoElemento == 4) {
					String idPerfil = verificarLinha(elemento);
					usuario.setIdPerfil(Integer.parseInt(idPerfil));
				}
			}
		}
	}
	
	private int buscarPosicao(String delimitador[], String elemento) {
		for(int i = 0; i < delimitador.length; i ++) {
			 if(delimitador[i].equals(elemento)) {
				 return i;
			 }
		}
		return -1;
	}
	
	private String verificarLinha(String elemento) {
		if(elemento != null) {
			return elemento;
		} else {
			return null;
		}
	}
		
	private LocalDate verificarLinhaDataNascimento(String elemento) {
		if(elemento != null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return LocalDate.parse(elemento,dtf);
		} else {
			return null;
		}
	}
	
	public void validarDadosUsuario(List<UsuarioLoteTo> usuarios) throws ParseException {
		for(UsuarioLoteTo usuario : usuarios) {
			validarNomeUsuarioUpload(usuario);
			validarEmailUsuarioUpload(usuario);
			validarCpfUsuarioUpload(usuario);
			validarDataNascimentoUsuarioUpload(usuario);
			validarPerfilUsuarioUpload(usuario);
		}
	}
	
	private void validarNomeUsuarioUpload(UsuarioLoteTo usuario) {
		if(usuario.getNome() == null) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("Nome é obrigatório."));
		} else if(usuario.getNome().length() > 150) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O nome não pode ultrapassar de 150 de caracteres."));
		}
	}
	
	private void validarEmailUsuarioUpload(UsuarioLoteTo usuario) {
		if(usuario.getEmail() == null) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("E-mail é obrigatório."));
		} else if(!validarEmail(usuario.getEmail())) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O E-mail não é válido."));
		} else if(usuarioRepositorio.verificarEmail(usuario.getEmail())) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O E-mail já está cadastrado."));
		}
	}
	
	private void validarCpfUsuarioUpload(UsuarioLoteTo usuario) throws ParseException {
		MaskFormatter cpfFormatacao = new MaskFormatter("###.###.###-##");
		cpfFormatacao.setValueContainsLiteralCharacters(false);
		
		if(usuario.getCpf() == null) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O cpf é obrigatório."));
		} else if(usuario.getCpf().length() != 11) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O cpf tem que ter 11 caracteres."));
		} else if (!StringUtils.isNumeric(usuario.getCpf())) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O cpf tem que ser númerico."));
		} else if (!CpfValidation.isValidCPF(usuario.getCpf())) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O cpf não é válido."));
		} else {
			usuario.setCpf(cpfFormatacao.valueToString(usuario.getCpf()));
			
			if(usuarioRepositorio.verificarCpf(usuario.getCpf())) {
				usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O cpf já está cadastrado."));
			}
			
		}
	}
	
	private void validarDataNascimentoUsuarioUpload(UsuarioLoteTo usuario) {
		LocalDate dataAtual = LocalDate.now();
		
		if(usuario.getDataNascimento() == null) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("A data de nascimento é obrigatório."));
		} else if(usuario.getDataNascimento().isAfter(dataAtual)) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("A data de nascimento não pode ser maior que a data atual."));
		}
	}
	
	private void validarPerfilUsuarioUpload(UsuarioLoteTo usuario) {
		if(usuario.getIdPerfil() == null) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("O perfl é obrigatório."));
		} else if(usuario.getIdPerfil() != 1 && usuario.getIdPerfil() != 2 && usuario.getIdPerfil() != 3) {
			usuario.getUsuarioLoteErrosTos().add(new UsuarioLoteErrosTo("Tem que informar o perfil correto."));
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
