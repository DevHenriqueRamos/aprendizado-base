package main.java.br.com.hramos.dao;

import main.java.br.com.hramos.dao.generic.IGenericDAO;
import main.java.br.com.hramos.domain.Venda;
import main.java.br.com.hramos.exception.DAOException;
import main.java.br.com.hramos.exception.TipoChaveNaoEncontradaException;

public interface IVendaDAO extends IGenericDAO<Venda, String> {

    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
    public Venda consultarComCollection(Long id);
}
