package main.java.br.com.hramos.services;

import main.java.br.com.hramos.dao.IClienteDAO;
import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.exception.DAOException;
import main.java.br.com.hramos.exception.MoreThanOneRegisterException;
import main.java.br.com.hramos.exception.TableException;
import main.java.br.com.hramos.services.generic.GenericService;

public class ClienteService extends GenericService<Cliente, Long> implements IClienteService {

    private IClienteDAO dao;

    public ClienteService(IClienteDAO clienteDAO) {
        super(clienteDAO);
    }

    @Override
    public Cliente buscarPorCPF(Long cpf) throws DAOException {
        try {
            return this.dao.consultar(cpf);
        } catch (MoreThanOneRegisterException | TableException e) {
            e.printStackTrace();
        }
        return null;
    }

}
