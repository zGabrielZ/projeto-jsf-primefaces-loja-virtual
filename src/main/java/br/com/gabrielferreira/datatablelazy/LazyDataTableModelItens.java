package br.com.gabrielferreira.datatablelazy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.gabrielferreira.entidade.Itens;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.ItensService;
import br.com.gabrielferreira.utils.SessionUtil;
import lombok.Getter;
import lombok.Setter;

public class LazyDataTableModelItens<T> extends LazyDataModel<Itens> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ItensService itensService;
	
	@Getter
	@Setter
	private List<Itens> itens = new ArrayList<Itens>();
	
	public List<Itens> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		Usuario usuario = (Usuario) SessionUtil.getParam("usuario");
		itens = itensService.getItens(usuario.getId(), first, pageSize);
		setPageSize(pageSize);
		setRowCount(itensService.quantidadeRegistro(usuario.getId()));
		return itens;
	}

}
