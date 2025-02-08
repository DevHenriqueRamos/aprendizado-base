package main.java.br.com.hramos.factory;

import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.domain.Produto;

public class Factory implements IFactory {

    @Override
    public IFabricaPersistente createFactory(Class clazz) {

        if (clazz == Cliente.class) {
            return new ClienteFactory();
        } else if (clazz == Produto.class) {
            return new ProdutoFactory();
        } else {
            return new VendaFactory();
        }
    }
}
