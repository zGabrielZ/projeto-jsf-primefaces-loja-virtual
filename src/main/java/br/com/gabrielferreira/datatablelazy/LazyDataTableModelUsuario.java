package br.com.gabrielferreira.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.search.UsuarioSearch;
import br.com.gabrielferreira.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;

public class LazyDataTableModelUsuario<T> extends LazyDataModel<Usuario> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private UsuarioSearch usuarioSearch;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Getter
	@Setter
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	
	public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		usuarios = usuarioService.getFiltrar(usuarioSearch, first, pageSize);
		setPageSize(pageSize);
		setRowCount(usuarioService.quantidadeRegistro(usuarioSearch));
		return usuarios;
	}

}
