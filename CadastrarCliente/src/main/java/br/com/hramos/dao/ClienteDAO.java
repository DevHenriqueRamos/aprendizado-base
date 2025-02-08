package main.java.br.com.hramos.dao;

import main.java.br.com.hramos.dao.generic.GenericDAO;
import main.java.br.com.hramos.domain.Cliente;

public class ClienteDAO extends GenericDAO<Cliente, Long> implements IClienteDAO {

    public ClienteDAO() {
        super(Cliente.class);
    }
}
