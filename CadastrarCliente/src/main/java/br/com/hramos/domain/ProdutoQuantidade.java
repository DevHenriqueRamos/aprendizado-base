package main.java.br.com.hramos.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_PRODUTO_QUANTIDADE")
public class ProdutoQuantidade implements Persistente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_qtd_seq")
    @SequenceGenerator(name = "produto_qtd_seq", sequenceName = "sq_produto_qtd", initialValue = 1, allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Produto produto;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "VALOR_TOTAL", nullable = false)
    private Integer valorTotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venda_fk",
        foreignKey = @ForeignKey(name = "fk_produto_venda"),
        referencedColumnName = "id", nullable = false)
    private Venda venda;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Integer valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public ProdutoQuantidade() {
        this.quantidade = 0;
        this.valorTotal = 0;
    }

    public void adicionar(Integer quantidade) {
        this.quantidade += quantidade;
        this.valorTotal += this.produto.getValor() * quantidade;
    }

    public void remover(Integer quantidade) {
        this.quantidade -= quantidade;
        this.valorTotal -= this.produto.getValor() * quantidade;
    }
}
