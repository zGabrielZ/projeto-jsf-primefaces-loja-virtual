package br.com.gabrielferreira.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.service.SaldoService;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class LazyDataTableModelSaldo<T> extends LazyDataModel<Saldo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SaldoService saldoService;

	private Integer idUsuario;
	
	private List<Saldo> saldos = new ArrayList<Saldo>();
	
	public List<Saldo> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		saldos = saldoService.getSaldosByUsuario(idUsuario, first, pageSize);
		setPageSize(pageSize);
		setRowCount(saldoService.quantidadeRegistro(idUsuario));
		return saldos;
	}

}
