package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Pessoa;
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
@Named(value = "mbPessoa")
@RequestScoped
public class MbPessoa {

    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> pessoas = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbPessoa() {
    }

    public void novo() {
        pessoa = new Pessoa();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (pessoa == null) {
            msg.retornaAdvertencia("Selecione uma pessoa!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (pessoa.getId() == null) {
            dao.inserir(pessoa);
            msg.retornaInfo("Pessoa cadastrada com sucesso!");
        } else {
            dao.atualizar(pessoa);
            msg.retornaInfo("Pesssoa atualizada com sucesso!");
        }
        pessoa = new Pessoa();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void excluir() {
        try {
            dao.excluir(pessoa);
            msg.retornaInfo("Pessoa excluída com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        pessoa = new Pessoa();
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getPessoas() {
        return dao.lista(Pessoa.class);
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

}
