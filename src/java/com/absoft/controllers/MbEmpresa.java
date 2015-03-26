package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.Empresa;
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
@Named(value = "mbEmpresa")
@RequestScoped
public class MbEmpresa {

    private Empresa empresa = new Empresa();
    private List<Empresa> empresas = new ArrayList<>();

    Mensagem msg = new Mensagem();

    @EJB
    DAOGenerico dao;

    public MbEmpresa() {
    }

    public void novo() {
        empresa = new Empresa();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }

    public void editar() {
        if (empresa == null) {
            msg.retornaAdvertencia("Selecione uma empresa!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }

    public void gravar() {
        if (empresa.getId() == null) {
            dao.inserir(empresa);
            msg.retornaInfo("Empresa cadastrada com sucesso!");
        } else {
            dao.atualizar(empresa);
            msg.retornaInfo("Empresa atualizada com sucesso!");
        }
        empresa = new Empresa();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("sucesso", true);
    }

    public void excluir() {
        try {
            dao.excluir(empresa);
            msg.retornaInfo("Empresa excluída com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        empresa = new Empresa();
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Empresa> getEmpresas() {
        return dao.lista(Empresa.class);
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

}
