package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Produto;
import com.absoft.util.Mensagem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbProduto")
@RequestScoped
public class MbProduto {

    private Produto produto = new Produto();
    private List<Produto> produtos = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbProduto() {
    }

    public void novo() {
        produto = new Produto();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (produto == null) {
            msg.retornaAdvertencia("Selecione um produto!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (produto.getId() == null) {
            dao.inserir(produto);
            msg.retornaInfo("Produto cadastrado com sucesso!");
        } else {
            dao.atualizar(produto);
            msg.retornaInfo("Produto atualizado com sucesso!");
        }
        produto = new Produto();
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

}
