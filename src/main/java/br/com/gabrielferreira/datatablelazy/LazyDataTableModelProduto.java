package br.com.gabrielferreira.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.search.ProdutoSearch;
import br.com.gabrielferreira.service.ProdutoService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazyDataTableModelProduto<T> extends LazyDataModel<Produto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoService produtoService;
	
	private ProdutoSearch produtoSearch;
	
	private List<Produto> produtos = new ArrayList<Produto>();
	
	public List<Produto> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		produtos = produtoService.getFiltrar(produtoSearch, first, pageSize);
		setPageSize(pageSize);
		setRowCount(produtoService.quantidadeRegistro(produtoSearch));
		return produtos;
	}

}
