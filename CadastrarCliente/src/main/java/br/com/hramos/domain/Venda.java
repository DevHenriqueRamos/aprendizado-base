package main.java.br.com.hramos.domain;

import jakarta.persistence.*;
import main.java.br.com.hramos.factory.ProdutoQuantidadeFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "TB_VENDA")
public class Venda implements Persistente {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;

        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "valor_seq")
    @SequenceGenerator(name = "valor_seq", sequenceName = "sq_valor", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "CODIGO", length = 10, nullable = false, unique = true, updatable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_cliente_fk",
        foreignKey = @ForeignKey(name = "fk_venda_cliente"),
        referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private Set<ProdutoQuantidade> produtos;

    @Column(name = "VALOR_TOTAL", nullable = false)
    private Integer valorTotal;

    @Column(name = "DATA_VENDA", nullable = false)
    private Instant dataVenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_VENDA", nullable = false)
    private Status status;

    public Venda() {
        produtos = new HashSet<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<ProdutoQuantidade> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<ProdutoQuantidade> produtos) {
        this.produtos = produtos;
    }

    public Integer getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Integer valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Instant getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Instant dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void adicionarProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getCodigo().equals(produto.getCodigo())).findAny();
        if (op.isPresent()) {
            ProdutoQuantidade produtpQtd = op.get();
            produtpQtd.adicionar(quantidade);
        } else {
            try {
                ProdutoQuantidadeFactory produtoQuantidadeFactory = new ProdutoQuantidadeFactory();
                ProdutoQuantidade prod =
                        (ProdutoQuantidade) produtoQuantidadeFactory.createObjectLocal(produto, quantidade, ProdutoQuantidade.class );
                produtos.add(prod);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        recalcularValorTotalVenda();
    }

    private void validarStatus() {
        if (this.status == Status.CONCLUIDA || this.status == Status.CANCELADA) {
            throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA " + this.status);
        }
    }

    public void removerProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getCodigo().equals(produto.getCodigo())).findAny();

        if (op.isPresent()) {
            ProdutoQuantidade produtpQtd = op.get();
            if (produtpQtd.getQuantidade()>quantidade) {
                produtpQtd.remover(quantidade);
                recalcularValorTotalVenda();
            } else {
                produtos.remove(op.get());
                recalcularValorTotalVenda();
            }

        }
    }

    public void removerTodosProdutos() {
        validarStatus();
        produtos.clear();
        valorTotal = 0;
    }

    public Integer getQuantidadeTotalProdutos() {
        // Soma a quantidade getQuantidade() de todos os objetos ProdutoQuantidade
        return produtos.stream()
                .reduce(0, (partialCountResult, prod) -> partialCountResult + prod.getQuantidade(), Integer::sum);
    }

    public void recalcularValorTotalVenda() {
        Integer valorTotal = 0;
        for (ProdutoQuantidade prod : this.produtos) {
            valorTotal += prod.getValorTotal();
        }
        this.valorTotal = valorTotal;
    }
}
