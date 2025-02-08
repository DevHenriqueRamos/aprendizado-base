package main.java.br.com.hramos.factory;

import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.domain.Persistente;
import main.java.br.com.hramos.exception.DadosInvalidosException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteFactory implements IFabricaPersistente {
    @Override
    public Persistente createObjectLocal(String[] campos) throws DadosInvalidosException {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(campos[0].trim());
            cliente.setCpf(Long.parseLong(campos[1].trim()));
            cliente.setTelefone(Long.parseLong(campos[2].trim()));
            cliente.setEndereco(campos[3].trim());
            cliente.setNumero(Integer.parseInt(campos[4].trim()));
            cliente.setCidade(campos[5].trim());
            cliente.setEstado(campos[6].trim());

            return cliente;
        } catch (IndexOutOfBoundsException e) {
            throw new DadosInvalidosException("Dados de cliente estao inválidos");
        }
    }

    @Override
    public Persistente createObjectFromDB(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("ID_CLIENTE"));
        cliente.setNome(rs.getString(("NOME")));
        cliente.setCpf(rs.getLong(("CPF")));
        cliente.setTelefone(rs.getLong(("TELEFONE")));
        cliente.setEndereco(rs.getString(("ENDERECO")));
        cliente.setNumero(rs.getInt(("NUMERO")));
        cliente.setCidade(rs.getString(("CIDADE")));
        cliente.setEstado(rs.getString(("ESTADO")));
        cliente.setDataNascimento(rs.getTimestamp(("DATA_NASCIMENTO")).toLocalDateTime());

        return cliente;
    }
}
