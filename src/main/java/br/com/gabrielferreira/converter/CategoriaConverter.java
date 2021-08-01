package br.com.gabrielferreira.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.service.CategoriaService;

@Named
@RequestScoped
public class CategoriaConverter implements Converter {

	@Inject
	private CategoriaService categoriaService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Categoria categoria = null;
		
		if(value != null) {
			if(value.equals("Selecione")) {
				return null;
			} else {
				categoria = categoriaService.procurarPorIdCategoria(new Integer(value));
			}
		}
		
		return categoria;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null) {
			Categoria categoria = (Categoria) value;
			return String.valueOf(categoria.getId());
		}
		
		return null;
	}

}
