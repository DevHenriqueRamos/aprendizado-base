package main.java.br.com.hramos.dao.generic;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import main.java.br.com.hramos.domain.Persistente;
import main.java.br.com.hramos.exception.DAOException;
import main.java.br.com.hramos.exception.MoreThanOneRegisterException;
import main.java.br.com.hramos.exception.TableException;
import main.java.br.com.hramos.exception.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class GenericDAO <T extends Persistente, E extends Serializable> implements IGenericDAO<T, E> {

    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    private Class<T> persistenteClass;

    public GenericDAO(Class<T> persistenteClass) {
        this.persistenteClass = persistenteClass;
    }

    protected void openConnection() {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("ExemploJPA");
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    protected void closeConnection() {
        entityManager.close();
        entityManagerFactory.close();
    }


    @Override
    public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
        openConnection();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        closeConnection();

        return entity;
    }

    @Override
    public void excluir(T entity) throws DAOException {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
        closeConnection();
    }

    @Override
    public T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
        openConnection();
        entity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        closeConnection();

        return entity;
    }

    @Override
    public T consultar(E valor) throws MoreThanOneRegisterException, TableException, DAOException {
        openConnection();
        T entity = entityManager.find(this.persistenteClass, valor);
        entityManager.getTransaction().commit();
        closeConnection();

        return entity;
    }

    @Override
    public Collection<T> buscarTodos() throws DAOException {
        String sql = "SELECT obj FROM " + this.persistenteClass.getSimpleName() + " obj";
        openConnection();
        List<T> list =
                entityManager.createQuery(sql, this.persistenteClass).getResultList();
        closeConnection();

        return list;
    }
}
