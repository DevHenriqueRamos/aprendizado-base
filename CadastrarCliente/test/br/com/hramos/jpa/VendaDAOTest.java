package br.com.hramos.jpa;

import br.com.hramos.dao.VendaExclusaoDAO;
import main.java.br.com.hramos.dao.*;
import main.java.br.com.hramos.domain.Cliente;
import main.java.br.com.hramos.domain.Produto;
import main.java.br.com.hramos.domain.Venda;
import main.java.br.com.hramos.exception.DAOException;
import main.java.br.com.hramos.exception.MoreThanOneRegisterException;
import main.java.br.com.hramos.exception.TableException;
import main.java.br.com.hramos.exception.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Collection;
import java.util.Random;

public class VendaDAOTest {

    private IVendaDAO vendaDao;

    private IVendaDAO vendaExclusaoDao;

    private IClienteDAO clienteDao;

    private IProdutoDAO produtoDao;

    private Random random;

    private Cliente cliente;

    private Produto produto;

    public VendaDAOTest() {
        this.vendaDao = new VendaDAO();
        vendaExclusaoDao = new VendaExclusaoDAO();
        this.clienteDao = new ClienteDAO();
        this.produtoDao = new ProdutoDAO();
        random = new Random();
    }

    @Before
    public void init() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        this.cliente = cadastrarCliente();
        this.produto = cadastrarProduto("A1", 10);
    }

    @After
    public void end() throws DAOException {
        excluirVendas();
        excluirProdutos();
        clienteDao.excluir(this.cliente);
    }

    @Test
    public void pesquisar() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        Venda venda = criarVenda("A1");
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        Assert.assertNotNull(vendaConsultada);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MoreThanOneRegisterException, TableException {
        Venda venda = criarVenda("A2");
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);

        Assert.assertEquals(20, (int) venda.getValorTotal());
        Assert.assertEquals(venda.getStatus(), Venda.Status.INICIADA);

        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        Assert.assertNotNull(vendaConsultada.getId());
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        retorno.setStatus(Venda.Status.CANCELADA);
        vendaDao.cancelarVenda(venda);

        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        Assert.assertEquals(codigoVenda, vendaConsultada.getCodigo());
        Assert.assertEquals(Venda.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(produto, 1);

        Assert.assertEquals(3, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(30, (int) vendaConsultada.getValorTotal());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, 50);
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);

        Assert.assertEquals(3, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(70, (int) vendaConsultada.getValorTotal());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
        Venda venda = criarVenda("A6");
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);

        Venda venda1 = criarVenda("A6");
        Venda retorno1 = vendaDao.cadastrar(venda1);
        Assert.assertNull(retorno1);
        Assert.assertEquals(venda.getStatus(), Venda.Status.INICIADA);
    }

    @Test
    public void removerProduto() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, 50);
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertEquals(3, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(70, (int) vendaConsultada.getValorTotal());


        vendaConsultada.removerProduto(prod, 1);
        Assert.assertEquals(2, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(20, (int) vendaConsultada.getValorTotal());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A8";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, 50);
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertEquals(3, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(70, (int) vendaConsultada.getValorTotal());


        vendaConsultada.removerProduto(prod, 1);
        Assert.assertEquals(2, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(20, (int) vendaConsultada.getValorTotal());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, 50);
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertEquals(3, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(70, (int) vendaConsultada.getValorTotal());


        vendaConsultada.removerTodosProdutos();
        Assert.assertEquals(0, (int) vendaConsultada.getQuantidadeTotalProdutos());
        Assert.assertEquals(0, (int) vendaConsultada.getValorTotal());
        Assert.assertEquals(vendaConsultada.getStatus(), Venda.Status.INICIADA);
    }

    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        String codigoVenda = "A11";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());

        vendaConsultada.adicionarProduto(this.produto, 1);

    }


    private void excluirProdutos() throws DAOException {
        Collection<Produto> list = this.produtoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                this.produtoDao.excluir(prod);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    private void excluirVendas() throws DAOException {
        Collection<Venda> list = this.vendaExclusaoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                this.vendaExclusaoDao.excluir(prod);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    private Produto cadastrarProduto(String codigo, int valor) throws TipoChaveNaoEncontradaException, MoreThanOneRegisterException, TableException, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(valor);
        produtoDao.cadastrar(produto);
        return produto;
    }

    private Cliente cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(random.nextLong());
        cliente.setNome("Rodrigo");
        cliente.setCidade("São Paulo");
        cliente.setEndereco("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTelefone(1199999999L);
        clienteDao.cadastrar(cliente);
        return cliente;
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }
}
