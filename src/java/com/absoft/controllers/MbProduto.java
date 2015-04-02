package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Pessoa;
import com.absoft.entities.Produto;
import com.absoft.util.Mensagem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbProduto")
@SessionScoped
public class MbProduto implements Serializable {

    private Produto produto = new Produto();
    private List<Produto> produtos = new ArrayList<>();
    private List<Pessoa> fornecedores = new ArrayList<>();

    private Long idEmpresaDestino;

    private BigDecimal qtdTransferir;

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbProduto() {
    }

    public void novo() {
        produto = new Produto();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void copiar() {
        if (produto == null) {
            msg.retornaAdvertencia("Selecione um produto!");
        } else {
            produto.setId(null);
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void editar() {
        if (produto == null) {
            msg.retornaAdvertencia("Selecione um produto!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        try {
            if (produto.getId() == null) {
                dao.inserir(produto);
                msg.retornaInfo("Produto cadastrado com sucesso!");
            } else {
                dao.atualizar(produto);
                msg.retornaInfo("Produto atualizado com sucesso!");
            }
            produto = new Produto();
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.addCallbackParam("sucesso", true);
        } catch (Exception ex) {
            msg.retornaErro("Este produto já está cadastrado nesta empresa, favor verifique e tente novamente!");
        }
    }

    public void excluir() {
        try {
            dao.excluir(produto);
            msg.retornaInfo("Produto excluído com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        produto = new Produto();
    }

    public void transferenciaEstoque() {
        if (produto == null) {
            msg.retornaAdvertencia("Selecione um produto!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('transfEstoque').show()"); //Abre o dialogo
        }
    }

    public void transferir() {
        //Verifica produto correspondente na filial selecionada
        Produto produtoDestino = null;
        List<Produto> produtosDestino = dao.listaCondicao(Produto.class, "sku = " + produto.getSku() + " AND empresa.id = " + idEmpresaDestino.toString());
        for (Produto pro : produtosDestino) {
            produtoDestino = pro;
        }

        if (produtoDestino != null) {

            produto.setEstoque(produto.getEstoque().subtract(qtdTransferir));
            dao.atualizar(produto);

            produtoDestino.setEstoque(produtoDestino.getEstoque().add(qtdTransferir));
            dao.atualizar(produtoDestino);

            msg.retornaInfo("A quantidade em estoque foi transferida com sucesso !");
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('transfEstoque').hide()"); //Fecha o dialogo

        } else {
            msg.retornaErro("Não há um produto correspondente na filial selecionada!");
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('transfEstoque').hide()"); //Fecha o dialogo
        }

    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<Produto> getProdutos() {
        return dao.lista(Produto.class);
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public List<Pessoa> getFornecedores() {
        return dao.listaCondicao(Pessoa.class, "cliFor = 'F' OR cliFor = 'A'");
    }

    public void setFornecedores(List<Pessoa> fornecedores) {
        this.fornecedores = fornecedores;
    }

    public Long getIdEmpresaDestino() {
        return idEmpresaDestino;
    }

    public void setIdEmpresaDestino(Long idEmpresaDestino) {
        this.idEmpresaDestino = idEmpresaDestino;
    }

    public BigDecimal getQtdTransferir() {
        return qtdTransferir;
    }

    public void setQtdTransferir(BigDecimal qtdTransferir) {
        this.qtdTransferir = qtdTransferir;
    }

}
