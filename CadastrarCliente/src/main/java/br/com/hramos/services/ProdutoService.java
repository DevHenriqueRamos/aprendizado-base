package main.java.br.com.hramos.services;

import main.java.br.com.hramos.dao.IProdutoDAO;
import main.java.br.com.hramos.domain.Produto;
import main.java.br.com.hramos.services.generic.GenericService;

public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {

    public ProdutoService(IProdutoDAO dao) {
        super(dao);
    }

}
