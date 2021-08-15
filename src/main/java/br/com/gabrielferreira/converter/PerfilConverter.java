package br.com.gabrielferreira.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.service.PerfilService;

@Named
@RequestScoped
public class PerfilConverter implements Converter {

	@Inject
	private PerfilService perfilService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Perfil perfil = null;
		
		if(value != null) {
			if(value.equals("Selecione")) {
				return null;
			} else {
				perfil = perfilService.procurarPorId(new Integer(value));
			}
		}
		
		return perfil;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null) {
			Perfil perfil = (Perfil) value;
			return String.valueOf(perfil.getId());
		}
		
		return null;
	}

}
