package br.com.gabrielferreira.validator;

import java.time.LocalDateTime;

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
public class VerificarLocalDateTime implements Validator{
	
	@Getter
	@Setter
	private String descricaoErro;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		LocalDateTime data = (LocalDateTime) value;
		LocalDateTime dataAtual = LocalDateTime.now();
		
		Object label = MessageFactory.getLabel(context, component);
		
		if(data != null) {
			if(data.isAfter(dataAtual)) {
				descricaoErro = label + ": Não é possível seguir em frente com esta esta data, pois a data não pode ser maior que a data atual !";
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,descricaoErro,null);
				throw new ValidatorException(message);
			}
		}
	}

}
