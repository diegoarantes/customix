package com.absoft.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Diego Arantes
 */
@Entity
@Table(name = "produto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sku", "empresa_id"}, name = "produtoEmpresas")}
//Define que o valor das colunas juntas devem ser único ex: 1 e 1 nao podera repetir 1 e 1 em outro registro
//Trava para não ter o produto com o mesmo sku na mesma empresa
)
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String sku;

    @NotNull
    private String descricao;

    @NotNull
    private BigDecimal estoque;

    @NotNull
    private BigDecimal estoqueMinimo;

    @NotNull
    private boolean controlaEstoque;

    @NotNull
    private boolean avisaEstoqueBaixo;

    @NotNull
    private BigDecimal precoVenda;

    @NotNull
    private BigDecimal custo;

    private String observacao;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne(optional = false)
    private UnidadeMedida unidadeMedida;

    @ManyToOne
    private Pessoa fornecedor;

    @ManyToOne(optional = false)
    private Empresa empresa;

    public Produto() {
        categoria = new Categoria();
        unidadeMedida = new UnidadeMedida();
        fornecedor = new Pessoa();
        empresa = new Empresa();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }

    public boolean isControlaEstoque() {
        return controlaEstoque;
    }

    public void setControlaEstoque(boolean controlaEstoque) {
        this.controlaEstoque = controlaEstoque;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Pessoa getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Pessoa fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public boolean isAvisaEstoqueBaixo() {
        return avisaEstoqueBaixo;
    }

    public void setAvisaEstoqueBaixo(boolean avisaEstoqueBaixo) {
        this.avisaEstoqueBaixo = avisaEstoqueBaixo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.absoft.entities.Produto[ id=" + id + " ]";
    }

}
