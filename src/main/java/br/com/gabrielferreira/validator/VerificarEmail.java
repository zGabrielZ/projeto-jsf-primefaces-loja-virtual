package br.com.gabrielferreira.validator;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.sun.faces.util.MessageFactory;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class VerificarEmail implements Validator{
	
	@Getter
	@Setter
	private String descricaoErro;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String email = (String) value;
		
		Object label = MessageFactory.getLabel(context, component);
		
		if(email != null) {
			if(!validarEmail(email)) {
				descricaoErro = label + ": Não é possível seguir em frente com este e-mail, pois não é válido.";
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,descricaoErro,null);
				throw new ValidatorException(message);
			}
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
