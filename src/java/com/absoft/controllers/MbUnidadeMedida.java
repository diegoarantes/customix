package com.absoft.controllers;

import com.absoft.dao.DAOGenerico;
import com.absoft.entities.UnidadeMedida;
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
@Named(value = "mbUnidadeMedida")
@RequestScoped
public class MbUnidadeMedida {
    
    private UnidadeMedida unidadeMedida = new UnidadeMedida();
    private List<UnidadeMedida> unidades = new ArrayList<>();
    
    Mensagem msg = new Mensagem();
    
    @EJB
    DAOGenerico dao;
    
    public MbUnidadeMedida() {
    }
    
    public void novo() {
        unidadeMedida = new UnidadeMedida();
        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
    }
    
    public void editar() {
        if (unidadeMedida == null) {
            msg.retornaAdvertencia("Selecione uma Unidade de Medida!");
        } else {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('dialogo').show()"); //Abre o dialogo
        }
    }
    
    public void gravar() {
        if (unidadeMedida.getId() == null) {
            dao.inserir(unidadeMedida);
            msg.retornaInfo("Unidade de Medida cadastrada com sucesso!");
        } else {
            dao.atualizar(unidadeMedida);
            msg.retornaInfo("Unidade de Medida atualizado com sucesso!");
        }
        unidadeMedida = new UnidadeMedida();
    }
    
    public void excluir() {
        try {
            dao.excluir(unidadeMedida);
            msg.retornaInfo("Unidade de Medida excluída com sucesso!");
        } catch (Exception ex) {
            msg.retornaErro(ex.getMessage());
        }
        unidadeMedida = new UnidadeMedida();
    }
    
    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }
    
    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    
    public List<UnidadeMedida> getUnidades() {
        return dao.lista(UnidadeMedida.class);
    }
    
    public void setUnidades(List<UnidadeMedida> unidades) {
        this.unidades = unidades;
    }
    
}
