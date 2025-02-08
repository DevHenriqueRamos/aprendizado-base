package main.java.br.com.hramos.factory;

import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.domain.Venda;
import main.java.br.com.hramos.exception.DadosInvalidosException;
import main.java.br.com.hramos.domain.Persistente;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VendaFactory implements IFabricaPersistente{

    @Override
    public Persistente createObjectLocal(String[] campos) throws DadosInvalidosException {
        return null;
    }

    @Override
    public Persistente createObjectFromDB(ResultSet rs) throws SQLException {
        IFabricaPersistente fabricaPersistente = new Factory().createFactory(Cliente.class);
        Cliente cliente = (Cliente) fabricaPersistente.createObjectFromDB(rs);
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setId(rs.getLong("ID_VENDA"));
        venda.setCodigo(rs.getString("CODIGO"));
        venda.setValorTotal(rs.getInt("VALOR_TOTAL"));
        venda.setDataVenda(rs.getTimestamp("DATA_VENDA").toInstant());
        venda.setStatus(Venda.Status.getByName(rs.getString("STATUS_VENDA")));
        return venda;
    }
}
