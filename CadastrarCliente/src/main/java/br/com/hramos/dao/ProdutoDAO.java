package main.java.br.com.hramos.dao;

import main.java.br.com.hramos.dao.generic.GenericDAO;
import main.java.br.com.hramos.domain.Produto;

public class ProdutoDAO extends GenericDAO<Produto, String> implements IProdutoDAO {

    public ProdutoDAO() {
        super(Produto.class);
    }
}
