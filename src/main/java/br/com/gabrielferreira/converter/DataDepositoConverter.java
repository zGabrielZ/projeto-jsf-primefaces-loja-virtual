package br.com.gabrielferreira.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.gabrielferreira.utils.FacesMessages;

@FacesConverter("dataDepositoConverter")
public class DataDepositoConverter implements Converter{

	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		LocalDateTime data = null;
		
		if(value != null && !value.equals("")) {
			// DD/MM/YYYY HH:MM:SS
			try {
				data = LocalDateTime.parse(value, dtf);
			} catch (Exception e) {
				FacesMessages.adicionarMensagem("frmCadastroSaldo:dataDeposito", FacesMessage.SEVERITY_ERROR,"Data incorreta !", null);
			}
		}
		
		return data;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LocalDateTime data = (LocalDateTime) value;
		String dataFormatada = dtf.format(data);
		return dataFormatada;
	}

}
