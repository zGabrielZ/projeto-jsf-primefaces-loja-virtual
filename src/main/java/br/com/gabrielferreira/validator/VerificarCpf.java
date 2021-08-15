package br.com.gabrielferreira.validator;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import com.sun.faces.util.MessageFactory;

import br.com.gabrielferreira.utils.validation.CpfValidation;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class VerificarCpf implements Validator{
	
	@Getter
	@Setter
	private String descricaoErro;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String cpf = (String) value;
		String cpfSemFormatacao = cpf.replaceAll("[^\\d ]", "");
		
		Object label = MessageFactory.getLabel(context, component);
		
		if(cpf != null) {
			if(!CpfValidation.isValidCPF(cpfSemFormatacao)) {
				descricaoErro = label + ": Não é possível cadastrar este CPF, pois não é válido.";
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,descricaoErro,null);
				throw new ValidatorException(message);
			}
		}
		
	}

}
