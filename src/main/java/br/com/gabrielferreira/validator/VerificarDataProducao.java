package br.com.gabrielferreira.validator;

import java.time.LocalDate;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import com.sun.faces.util.MessageFactory;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class VerificarDataProducao implements Validator{
	
	@Getter
	@Setter
	private String descricaoErro;
	
	@Getter
	@Setter
	private String descricaoCampo;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		LocalDate dataProducao = (LocalDate) value;
		LocalDate dataAtual = LocalDate.now();
		
		Object label = MessageFactory.getLabel(context, component);
		
		if(dataProducao != null) {
			if(dataProducao.isAfter(dataAtual)) {
				descricaoErro = label + ": Não é possível cadastrar esta data de produção, pois a data não pode ser maior que a data atual !";
				descricaoCampo = "Por favor informe a data novamente !";
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,descricaoErro,descricaoCampo);
				throw new ValidatorException(message);
			}
		}
		
	}

}
