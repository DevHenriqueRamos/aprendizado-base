package main.java.br.com.hramos.factory;

import main.java.br.com.hramos.domain.Persistente;
import main.java.br.com.hramos.exception.DadosInvalidosException;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IFabricaPersistente {
    Persistente createObjectLocal(String campos[]) throws DadosInvalidosException;

    Persistente createObjectFromDB(ResultSet rs) throws SQLException;
}
