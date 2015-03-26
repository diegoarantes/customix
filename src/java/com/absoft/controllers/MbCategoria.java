package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Categoria;
import com.absoft.util.Mensagem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diego Arantes
 */
@Named(value = "mbCategoria")
@RequestScoped
public class MbCategoria {

    private Categoria categoria = new Categoria();
    private List<Categoria> categorias = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbCategoria() {
    }

    public void novo() {
        categoria = new Categoria();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (categoria == null) {
            msg.retornaAdvertencia("Selecione uma categoria!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (categoria.getId() == null) {
            dao.inserir(categoria);
            msg.retornaInfo("Categoria cadastrada com sucesso!");
        } else {
            dao.atualizar(categoria);
            msg.retornaInfo("Categoria atualizada com sucesso!");
        }
        categoria = new Categoria();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void excluir() {
        try {
            dao.excluir(categoria);
            msg.retornaInfo("Categoria excluída com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        categoria = new Categoria();
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Categoria> getCategorias() {
        return dao.lista(Categoria.class);
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

}
