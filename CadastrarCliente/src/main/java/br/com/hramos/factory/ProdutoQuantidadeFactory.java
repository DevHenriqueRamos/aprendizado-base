package main.java.br.com.hramos.factory;

import main.java.br.com.hramos.domain.Produto;
import main.java.br.com.hramos.domain.ProdutoQuantidade;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoQuantidadeFactory {

    public static ProdutoQuantidade createObjectFromDB(ResultSet rs) throws SQLException {
        IFabricaPersistente fabricaPersistente = new Factory().createFactory(Produto.class);

        Produto produto = (Produto) fabricaPersistente.createObjectFromDB(rs);
        ProdutoQuantidade produtoQuantidade = new ProdutoQuantidade();

        produtoQuantidade.setId(rs.getLong("ID"));
        produtoQuantidade.setProduto(produto);
        produtoQuantidade.setQuantidade(rs.getInt("QUANTIDADE"));
        produtoQuantidade.setValorTotal(rs.getInt("VALOR_TOTAL"));

        return produtoQuantidade;
    }

    public static ProdutoQuantidade createObjectLocal(Produto produto, Integer quantidade) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ProdutoQuantidade produtoQuantidade = new ProdutoQuantidade();
        produtoQuantidade.setProduto(produto);
        produtoQuantidade.setQuantidade(quantidade);

        return produtoQuantidade;
    }
}
