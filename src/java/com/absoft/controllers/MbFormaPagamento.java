package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.FormaPagamento;
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
@Named(value = "mbFormaPagamento")
@RequestScoped
public class MbFormaPagamento {

    private FormaPagamento formaPagamento = new FormaPagamento();
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbFormaPagamento() {
    }

    public void novo() {
        formaPagamento = new FormaPagamento();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (formaPagamento == null) {
            msg.retornaAdvertencia("Selecione uma Forma de Pagamento!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (formaPagamento.getId() == null) {
            dao.inserir(formaPagamento);
            msg.retornaInfo("Forma de Pagamento cadastrada com sucesso!");
        } else {
            dao.atualizar(formaPagamento);
            msg.retornaInfo("Forma de Pagamento atualizada com sucesso!");
        }
        formaPagamento = new FormaPagamento();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void excluir() {
        try {
            dao.excluir(formaPagamento);
            msg.retornaInfo("Forma de Pagamento excluída com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        formaPagamento = new FormaPagamento();
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public List<FormaPagamento> getFormasPagamento() {
        return dao.lista(FormaPagamento.class);
    }

    public void setFormasPagamento(List<FormaPagamento> formasPagamento) {
        this.formasPagamento = formasPagamento;
    }

}
