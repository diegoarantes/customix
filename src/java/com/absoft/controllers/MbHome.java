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
@Named(value = "mbHome")
@RequestScoped
public class MbHome {

    private List<Produto> estoqueBaixo = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbHome() {
    }

    public List<Produto> getEstoqueBaixo() {
        return dao.listaCondicao(Produto.class, "avisaEstoqueBaixo = true AND estoque < estoqueMinimo");
    }

    public void setEstoqueBaixo(List<Produto> estoqueBaixo) {
        this.estoqueBaixo = estoqueBaixo;
    }

}
