package br.com.hramos.dao;

import main.java.br.com.hramos.dao.generic.GenericDAO;
import main.java.br.com.hramos.dao.IVendaDAO;
import main.java.br.com.hramos.domain.Venda;
import main.java.br.com.hramos.exception.DAOException;
import main.java.br.com.hramos.exception.TipoChaveNaoEncontradaException;

public class VendaExclusaoDAO extends GenericDAO<Venda, String> implements IVendaDAO {

    public VendaExclusaoDAO() {
        super(Venda.class);
    }

    @Override
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public Venda consultarComCollection(Long id) {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

}
