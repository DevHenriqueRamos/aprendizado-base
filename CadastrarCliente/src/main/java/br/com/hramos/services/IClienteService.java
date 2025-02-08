package main.java.br.com.hramos.services;

import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.exception.DAOException;

public interface IClienteService extends IGenericService<Cliente, Long> {

    Cliente buscarPorCPF(Long cpf) throws DAOException;

}
