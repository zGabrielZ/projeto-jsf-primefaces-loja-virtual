package br.com.gabrielferreira.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.gabrielferreira.utils.FacesMessages;

@FacesConverter("dataProducaoConverter")
public class DataProducaoConverter implements Converter{

	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		LocalDate data = null;
		
		if(value != null && !value.equals("")) {
			// DD/MM/YYYY
			try {
				data = LocalDate.parse(value, dtf);
			} catch (Exception e) {
				FacesMessages.adicionarMensagem("cadastroProdutoForm:data", FacesMessage.SEVERITY_ERROR,"Data incorreta (Formato : DD/MM/YYYY) !", null);
			}
		}
		
		return data;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LocalDate data = (LocalDate) value;
		String dataFormatada = dtf.format(data);
		return dataFormatada;
	}

}
