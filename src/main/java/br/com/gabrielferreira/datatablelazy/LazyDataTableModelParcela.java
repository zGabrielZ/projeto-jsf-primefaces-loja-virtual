package br.com.gabrielferreira.datatablelazy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.gabrielferreira.entidade.Parcela;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.ParcelaService;
import br.com.gabrielferreira.utils.LoginJSF;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazyDataTableModelParcela<T> extends LazyDataModel<Parcela> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ParcelaService parcelaService;
	
	private List<Parcela> parcelas = new ArrayList<Parcela>();
	
	public List<Parcela> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		Usuario usuario = LoginJSF.getRecuperarUsuarioLogada();
		parcelas = parcelaService.getParcelas(usuario.getId(), first, pageSize);
		setPageSize(pageSize);
		setRowCount(parcelaService.quantidadeRegistro(usuario.getId()));
		return parcelas;
	}

}
