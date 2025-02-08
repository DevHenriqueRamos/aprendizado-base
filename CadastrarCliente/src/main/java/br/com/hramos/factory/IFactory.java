package main.java.br.com.hramos.factory;

public interface IFactory {
    IFabricaPersistente createFactory(Class clazz);
}
