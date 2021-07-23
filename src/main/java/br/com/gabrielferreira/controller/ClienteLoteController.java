package br.com.gabrielferreira.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.gabrielferreira.entidade.ArquivoUpload;
import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.entidade.enums.TipoArquivo;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.ArquivoUploadService;
import br.com.gabrielferreira.service.ClienteService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class ClienteLoteController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ArquivoUploadService arquivoUploadService;
	
	@Inject
	private ClienteService clienteService;
	
	@Setter
	private StreamedContent arquivo;
	
	@Getter
	@Setter
	private ArquivoUpload arquivoUpload;
	
	@Getter
	@Setter
	private Part arquivoUploadExcelTxt;
	
	@Getter
	@Setter
	private List<Cliente> clientes;
	
	@PostConstruct
	public void inicializar() {
		arquivoUpload = new ArquivoUpload();
		clientes = new ArrayList<>();
	}

	public void uploadArquivoExcel() throws IOException {
	
		// Caso o tipo do arquivo for xlsx
		if(arquivoUploadExcelTxt.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				&& clientes.isEmpty()) {
			
			byte[] arquivoByte = toByteArrays(arquivoUploadExcelTxt.getInputStream());
			arquivoUpload.setArquivo(arquivoByte);
			arquivoUpload.setTipoArquivo(TipoArquivo.EXCEL);
			verficarArquivoExcel(arquivoUploadExcelTxt);
			
		} else {
			FacesMessages.adicionarMensagem("cadastroClienteLoteExcelForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível inserir em lote !",
					null);
		}
		
	}
	
	public void cadastrarClientes() throws RegraDeNegocioException {
		arquivoUploadService.inserirExcel(arquivoUpload);
		clienteService.inserirClienteLote(clientes);
		arquivoUpload = new ArquivoUpload();
		clientes = new ArrayList<>();
		FacesMessages.adicionarMensagem("cadastroClienteLoteExcelForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrados com sucesso !",
				null);
	}
	
	private void verficarArquivoExcel(Part arquivoUploadExcelTxt) throws IOException {
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(arquivoUploadExcelTxt.getInputStream()); // Preparando para ler o excel
		XSSFSheet planilha = xssfWorkbook.getSheetAt(0); // Pegar a primeira planilha do excel
		Iterator<Row> linhasIterator = planilha.iterator(); // Percorrendo nas linhas
			
		while (linhasIterator.hasNext()) { // Enquanto tiver linha no arquivo do excel
			Row linha = linhasIterator.next(); // Dados na linha
			
			if(linha.getRowNum() == 0) {
				continue;
			}
			
			Iterator<Cell> celulaIterator = linha.iterator(); // Enquanto tiver linhas, vai pecorrer as celulas
			
			Cliente cliente = new Cliente();
			
			while (celulaIterator.hasNext()) {
				Cell celula = celulaIterator.next();
				
				switch (celula.getColumnIndex()) {
				case 0:
					cliente.setNome(celula.getStringCellValue());
					break;
				case 1:
					cliente.setEmail(celula.getStringCellValue());
					break;
				case 2:
					cliente.setCpf(celula.getStringCellValue());
					break;
				case 3:
					cliente.setDataNascimento(celula.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					break;
				default:
					break;
				}
				
			} // Fim das celula 
			
			clientes.add(cliente);
			
		}
		
		xssfWorkbook.close();
		
		
	}

	public StreamedContent getArquivo() throws FileNotFoundException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		String caminhoTipo = facesContext.getExternalContext().getRealPath("/resources/modelo-excel/modelo-cliente.xlsx");
		InputStream inputStream = new FileInputStream(caminhoTipo);
		arquivo = new DefaultStreamedContent(inputStream, "application/xlsx","modelo-cliente.xlsx");
		return arquivo;
	}
	
	public byte[] toByteArrays(InputStream inputStream) throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int reads = inputStream.read();
		while (reads != -1) {
			byteArrayOutputStream.write(reads);
			reads = inputStream.read();
		}
		return byteArrayOutputStream.toByteArray();
	}

}
