package main.java.br.com.hramos.services.generic;

import main.java.br.com.hramos.domain.Persistente;
import main.java.br.com.hramos.exception.DAOException;
import main.java.br.com.hramos.exception.MoreThanOneRegisterException;
import main.java.br.com.hramos.exception.TableException;
import main.java.br.com.hramos.exception.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericService <T extends Persistente, E extends Serializable>{

    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
    public void excluir(T entity) throws DAOException;
    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;
    public T consultar(E valor) throws MoreThanOneRegisterException, TableException, DAOException;
    public Collection<T> buscarTodos() throws DAOException;
}
