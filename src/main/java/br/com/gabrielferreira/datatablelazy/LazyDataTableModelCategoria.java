package br.com.gabrielferreira.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;
import br.com.gabrielferreira.service.CategoriaService;
import lombok.Getter;
import lombok.Setter;

public class LazyDataTableModelCategoria<T> extends LazyDataModel<Categoria> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private CategoriaSearch categoriaSearch;
	
	@Inject
	private CategoriaService categoriaService;
	
	@Getter
	@Setter
	private List<Categoria> categorias = new ArrayList<Categoria>();
	
	public List<Categoria> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		categorias = categoriaService.getFiltrar(categoriaSearch, first, pageSize);
		setPageSize(pageSize);
		setRowCount(categoriaService.quantidadeRegistro(categoriaSearch));
		return categorias;
	}

}
