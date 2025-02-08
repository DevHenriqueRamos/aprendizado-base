package main.java.br.com.hramos.factory;

import main.java.br.com.hramos.domain.Persistente;
import main.java.br.com.hramos.domain.Produto;
import main.java.br.com.hramos.exception.DadosInvalidosException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoFactory implements IFabricaPersistente {
    @Override
    public Persistente createObjectLocal(String[] campos) throws DadosInvalidosException {
        try {
            Produto produto = new Produto();
            return produto;
        } catch (IndexOutOfBoundsException e) {
            throw new DadosInvalidosException("Dados de produto estão inválidos");
        }
    }

    @Override
    public Persistente createObjectFromDB(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong(("ID_PRODUTO")));
        produto.setCodigo(rs.getString(("CODIGO")));
        produto.setNome(rs.getString(("NOME")));
        produto.setDescricao(rs.getString(("DESCRICAO")));
        produto.setValor(rs.getInt(("VALOR")));
        produto.setCor(rs.getString(("COR")));

        return produto;
    }
}
